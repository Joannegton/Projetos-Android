package com.example.diariodememorias.data.repositorio

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@ViewModelScoped
class GerenciadorDeSessaoRepositorio @Inject constructor(@ApplicationContext private val context: Context) {

    private val db = Firebase.firestore // Instância do Firestore
    private val auth = FirebaseAuth.getInstance()

    private val _usuarioLogado = MutableStateFlow<Usuario?>(null)
    val usuarioLogado = _usuarioLogado.asStateFlow()

    // Cria a MasterKey para criptografia
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Cria o EncryptedSharedPreferences
    private val usuarioPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "dados_usuario",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    init {
        // Verifica se há um usuário logado ao iniciar o repositório
        CoroutineScope(Dispatchers.IO).launch {
            tentarRecuperarSessao(
                onSucesso = { usuario ->
                    _usuarioLogado.value = usuario
                },
                onFalha = {
                    // Não faz nada se não houver sessão, mantém _usuarioLogado como null
                }
            )
        }
    }
    private val _parceiroId = MutableStateFlow<String?>(null)
    val parceiroId = _parceiroId.asStateFlow().value



    private fun salvarDadosUsuario(usuario: Usuario) {
        // Salva os dados no EncryptedSharedPreferences
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
            val usuarioId = resultadoAuth.user!!.uid
            val usuario = Usuario(usuarioId, nome, email, senha)
            cadastrarNoBanco(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun cadastrarNoBanco(usuario: Usuario): Result<String> {
        return try {
            usuario.id?.let { db.collection("usuarios").document(it).set(usuario).await() }
            _usuarioLogado.value = usuario // Atualiza o estado do usuário logado
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

    private fun tentarRecuperarSessao(onSucesso: (Usuario) -> Unit, onFalha: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = usuarioPrefs.getString("uid", null)
            val parceiroId = usuarioPrefs.getString("parceiroId", null)
            _parceiroId.value = parceiroId
            if (uid != null) {
                try {
                    val usuario = carregarUsuario(uid)
                    onSucesso(usuario)
                } catch (e: Exception) {
                    // Lidar com erro ao carregar o usuário (ex: usuário não encontrado no Firestore)
                    onFalha()
                }
            } else {
                onFalha()
            }
        }
    }

    private suspend fun carregarUsuario(uid: String): Usuario {
        val usuarioRef = db.collection("usuarios").document(uid).get().await()
        val usuario = usuarioRef.toObject(Usuario::class.java)!!
        salvarDadosUsuario(usuario)
        return usuario // Forçando a conversão para não-nulo, já que esperamos que o usuário exista

    }

    fun obterUidFlow() : String{
        return auth.currentUser?.uid ?: ""
    }

    fun obterUidParceiro(): String? {
        return usuarioPrefs.getString("parceiroId", null)
    }

    fun sair() {
        auth.signOut()
        _usuarioLogado.value = null

        // Limpa os dados do EncryptedSharedPreferences
        with(usuarioPrefs.edit()) {
            clear()
            apply()
        }
    }
}