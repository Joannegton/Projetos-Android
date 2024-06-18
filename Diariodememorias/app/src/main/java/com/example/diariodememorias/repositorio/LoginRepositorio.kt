package com.example.diariodememorias.repositorio

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LoginRepositorio @Inject constructor() {

    private val auth = Firebase.auth

    fun entrar(email: String, senha: String, resultado: (Boolean, String?) -> Unit){
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultado(true, null)
                } else {
                    resultado(false, "Email ou senha incorretos")
                }
            }
    }
}