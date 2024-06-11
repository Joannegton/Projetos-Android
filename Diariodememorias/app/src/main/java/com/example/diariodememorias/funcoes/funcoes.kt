package com.example.diariodememorias.funcoes

import android.net.Uri
import com.example.diariodememorias.models.Memoria
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume

private val auth = Firebase.auth
private val db = Firebase.firestore


fun criarConta(email: String, senha: String, resultado: (Boolean, String?) -> Unit) {
    auth.createUserWithEmailAndPassword(email, senha)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val usuario = auth.currentUser
                val usuarioRef = db.collection("usuarios").document(usuario?.uid.toString())
                val usuarioData = hashMapOf(
                    "email" to email,
                    "senha" to senha,
                    "parceiroId" to null
                )
                usuarioRef.set(usuarioData)
                    .addOnSuccessListener {
                        resultado(true, null)
                    }
                    .addOnFailureListener{e ->
                        resultado(false, e.message)
                    }
            } else {
                resultado(false, task.exception?.message)
            }
        }
}

fun entrar(email: String, senha: String, resultado: (Boolean, String?) -> Unit){
    auth.signInWithEmailAndPassword(email, senha)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                resultado(true, null)
            } else {
                resultado(false, task.exception?.message)
            }
        }
}

fun conectarparceiro(usuarioId: String?, parceiroId: String, resultado: (Boolean, String?) -> Unit){
    val userRef = db.collection("usuarios").document(usuarioId.toString())
    val parceiroRef = db.collection("usuarios").document(parceiroId)

    db.runTransaction { transacao ->
        transacao.update(userRef, "parceiroId", parceiroId)
        transacao.update(parceiroRef, "parceiroId", usuarioId)
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            resultado(true, null)
        } else {
            resultado(false, task.exception?.message)
        }
    }
}

fun adicionarMemoria(memoria: Memoria, usuarioId: String, compartilhadoCom: String?, resultado: (Boolean, String?) -> Unit){
    val memoriaRef = db.collection("memories").document()
    val memoriaData = memoria.copy(
        compartilhadoCom = compartilhadoCom
    )

    memoriaRef.set(memoriaData)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                resultado(true, null)
            } else {
                resultado(false, task.exception?.message)
            }
        }
}

fun pegarMemorias(usuarioId: String, parceiroId: String?, resultado: (List<Memoria>) -> Unit){
    db.collection("memories")
        .whereIn("compartilhadoCom", listOf(null, parceiroId))
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val memorias = task.result?.documents?.map { it.toObject(Memoria::class.java) }?.filterNotNull() ?: listOf()
                resultado(memorias)
            }
        }
    db.collection("memories")
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val memorias = task.result?.documents?.map { it.toObject(Memoria::class.java) }?.filterNotNull() ?: listOf()
                resultado(memorias)
            }
        }
}

// Função para enviar mídia para o Firebase
fun enviarMidia(uri: Uri, resultado: (String) -> Unit){
    // Cria uma referência para o arquivo no Firebase Storage
    val storageRef = Firebase.storage.reference.child("media/${UUID.randomUUID()}")
    // Inicia o upload do arquivo
    val envioTask = storageRef.putFile(uri)

    envioTask.continueWithTask { task ->
        // Se o upload falhou, lança a exceção
        if (!task.isSuccessful){
            task.exception?.let { throw it }
        }
        // Se o upload foi bem-sucedido, obtém a URL de download do arquivo
        storageRef.downloadUrl
    }.addOnCompleteListener { task ->
        // Se a obtenção da URL de download foi bem-sucedida, retorna a URL
        // Se falhou, retorna "null"
        if (task.isSuccessful){
            val downloadUri = task.result
            resultado(downloadUri?.toString() ?: "null")
        } else{
            resultado("null")
        }
    }
}

suspend fun fetchMemories(usuarioId: String, parceiroId: String?): List<Memoria> {
    return suspendCancellableCoroutine { continuacao ->
        pegarMemorias(usuarioId, parceiroId){listaMemorias ->
            continuacao.resume(listaMemorias)
        }
    }
}