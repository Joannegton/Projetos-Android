package com.example.diariodememorias.data.repositorio

import android.net.Uri
import android.util.Log
import com.example.diariodememorias.data.models.Memoria
import com.example.diariodememorias.util.Resultado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class MemoriaRepositorio @Inject constructor() {
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    // Função para buscar memórias do Firestore
    suspend fun pegarMemorias(usuarioId: String, parceiroId: String): List<Memoria> {
        return try {
            val compartilhadasQuery = db.collection("memories")
                .whereIn("compartilhadoCom", listOf(usuarioId, parceiroId))
               // .orderBy("timestamp", Query.Direction.DESCENDING) ta dando erro aqui
                .get()
                .await()

            val memoriasCompartilhadas = compartilhadasQuery.documents.mapNotNull { it.toObject(
                Memoria::class.java) }

            val todasAsMemoriasQuery = db.collection("memories")
                .whereEqualTo("usuarioId", usuarioId)
                //.orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val todasAsMemorias = todasAsMemoriasQuery.documents.mapNotNull { it.toObject(Memoria::class.java) }

            val todasMemorias = (memoriasCompartilhadas + todasAsMemorias).distinctBy { it.timestamp }

            todasMemorias
        } catch (e: Exception) {
            Log.e("TAG", "Error fetching memories", e)
            emptyList()
        }
    }

    // Função para adicionar uma memória no Firestore
    suspend fun adicionarMemoria(memoria: Memoria): Result<Unit> {
        return try {
            val memoriaRef = db.collection("memories").document()
            val memoriaData = memoria.copy()
            memoriaRef.set(memoriaData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("TAG", "Não adicionou memoria" + e.message)
            Result.failure(e)
        }
    }

    // Função para enviar mídia para o Firebase Storage e obter a URL de download
    suspend fun enviarMidia(uri: Uri): Resultado<String> {
        return try {
            val storageRef = storage.reference.child("media/${UUID.randomUUID()}")
            storageRef.putFile(uri).await()
            val downloadUri = storageRef.downloadUrl.await()
            Resultado.Sucesso(downloadUri.toString())
        } catch (e: Exception) {
            Resultado.Falha(e.message.toString())
        }
    }
}
