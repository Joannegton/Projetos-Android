package com.example.diariodememorias.data.repositorio

import android.content.Context
import android.content.SharedPreferences
import com.example.diariodememorias.data.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class GerenciadorDeSessaoRepositorio @Inject constructor(@ApplicationContext private val context: Context) {

    private val db = Firebase.firestore // Instância do Firestore
    private val auth = FirebaseAuth.getInstance()

    private val _verificarUsuarioLogado = MutableStateFlow(false)
    private val verificarUsuarioLogado = _verificarUsuarioLogado.asStateFlow()

    private val _usuarioAtual = MutableStateFlow<String?>(null)
    val usuarioAtual = _usuarioAtual.asStateFlow()


    private val usuarioPrefs: SharedPreferences = context.getSharedPreferences(
        "dados_usuario",
        Context.MODE_PRIVATE
    )

    private fun salvarDadosUsuario(usuario: Usuario) {
        with(usuarioPrefs.edit()) {
            putString("uid", usuario.id)
            putString("nome", usuario.nome)
            putString("email", usuario.email)
            putString("parceiroId", usuario.parceiroId)
            apply()
        }
    }

    suspend fun cadastrar(nome: String, email: String, senha: String): Result<String> {
        return try {
            val resultadoAuth = auth.createUserWithEmailAndPassword(email, senha).await()
            resultadoAuth.user?.uid?.let { uid ->
                val usuario = Usuario(uid, nome, email, senha)
                cadastrarNoBanco(usuario)
            } ?: Result.failure(Exception("Erro ao obter UID"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun cadastrarNoBanco(usuario: Usuario): Result<String> {
        return try {
            db.collection("usuarios").document(usuario.id!!).set(usuario).await()
            Result.success("Cadastrado com sucesso")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun entrar(email: String, senha: String, resultado: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    _usuarioAtual.value = uid
                    uid?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                carregarUsuario(it)
                                resultado(true, null)
                            } catch (e: Exception) {
                                resultado(false, "Erro ao carregar usuário: ${e.message}")
                            }
                        }
                    } ?: resultado(false, "Erro ao obter UID")
                } else {
                    resultado(false, "Email ou senha incorretos")
                }
            }
    }

    private suspend fun carregarUsuario(uid: String) {
        val usuarioRef = db.collection("usuarios").document(uid).get().await()
        val usuario = usuarioRef.toObject(Usuario::class.java)!!
        salvarDadosUsuario(usuario)
    }

    fun usuarioLogado(): Flow<Boolean> {
        val usuario = FirebaseAuth.getInstance().currentUser
        _verificarUsuarioLogado.value = usuario != null
        return  verificarUsuarioLogado
    }

    fun obterUid() : String{
        return auth.currentUser?.uid ?: ""
    }

    fun obterUidParceiro(): String? {
        return usuarioPrefs.getString("parceiroId", null)
    }

    fun sair() {
        auth.signOut()
        usuarioPrefs.edit().clear().apply()
    }
}