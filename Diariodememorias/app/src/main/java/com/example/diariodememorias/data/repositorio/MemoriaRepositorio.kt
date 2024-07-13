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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class MemoriaRepositorio @Inject constructor() {
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    private val _memories = MutableStateFlow<List<Memoria>>(emptyList())
    val memories = _memories.asStateFlow()

    init {
        // Registra um SnapshotListener para observar mudanças na coleção "memories"
        db.collection("memories")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("TAG", "Erro ao observar memórias", exception)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val memorias = snapshot.toObjects(Memoria::class.java)
                    _memories.value = memorias
                }
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
