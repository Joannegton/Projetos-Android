package com.example.diariodememorias.data.repositorio

import android.net.Uri
import android.util.Log
import com.example.diariodememorias.data.models.Memoria
import com.example.diariodememorias.util.Resultado
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
    suspend fun pegarMemorias(usuarioId: String, parceiroId: String ): List<Memoria> {
        return try {
            val queryUsuario = db.collection("memories")
                .whereEqualTo("compartilhadoCom", usuarioId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            val memoriasUsuario = queryUsuario.documents.mapNotNull { it.toObject(
                Memoria::class.java) }

            val queryParceiro = db.collection("memories")
                .whereEqualTo("compartilhadoCom", parceiroId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            val memoriasParceiro = queryParceiro.documents.mapNotNull {
                it.toObject(Memoria::class.java)
            }

            val memoriasCompartilhadas = memoriasUsuario + memoriasParceiro


            Log.i("tag", "memoriasCompartilhadas: ${memoriasCompartilhadas.size}")
            memoriasCompartilhadas.distinctBy { it.timestamp }

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
