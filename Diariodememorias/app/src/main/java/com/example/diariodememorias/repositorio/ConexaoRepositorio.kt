package com.example.diariodememorias.repositorio

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// Repositório para gerenciar conexões entre parceiros
class ConexaoRepositorio @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun conectarParceiro(usuarioId: String, emailParceiro: String): Result<Unit> {
        Log.d("conectarParceiro", "Função conectarParceiro chamada") // Log de depuração

        return try {
            val parceiroIdQuery = db.collection("usuarios")
                .whereEqualTo("email", emailParceiro).get().await()

            val parceiroId = parceiroIdQuery.documents[0].id

            // Referências aos documentos do usuário e do parceiro
            val userRef = db.collection("usuarios").document(usuarioId)
            val parceiroRef = db.collection("usuarios").document(parceiroId)

            // Executa a transação para conectar os parceiros
            db.runTransaction { transacao ->
                transacao.update(userRef, "parceiroId", parceiroId)
                transacao.update(parceiroRef, "parceiroId", usuarioId)
            }.await() // Usando await() para lidar com a operação assíncrona
            Result.success(Unit) // Retorna sucesso sem dados específicos
        } catch (e: Exception) {
            Log.e("tag", "Erro ao conectar parceiro: ${e.message}")
            Result.failure(e) // Retorna a exceção em caso de erro
        }
    }
}