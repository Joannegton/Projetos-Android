package com.example.diariodememorias.repositorio

import android.util.Log
import com.example.diariodememorias.models.Usuario
import com.example.diariodememorias.util.Resultado
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class LoginRepositorio @Inject constructor() {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun entrar(email: String, senha: String, resultado: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultado(true, null)
                } else {
                    resultado(false, "Email ou senha incorretos")
                }
            }
    }

    suspend fun cadastrar(
        nome: String,
        email: String,
        senha: String,
    ): Result<String> {
        return try {
            val resultadoAuth = auth.createUserWithEmailAndPassword(email, senha).await()
            val usuarioId = resultadoAuth.user!!.uid
            val usuario = Usuario(usuarioId, nome, email, senha)
            cadastrarNoBanco(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private suspend fun cadastrarNoBanco(usuario: Usuario): Result<String> {
        return try {
            db.collection("usuarios").document(usuario.id).set(usuario).await()
            Result.success("Cadastrado com sucesso")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
