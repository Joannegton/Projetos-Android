package com.example.diariodememorias.data.repositorio

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.diariodememorias.data.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@ViewModelScoped
class GerenciadorDeSessaoRepositorio @Inject constructor(@ApplicationContext private val context: Context) {

    private val db = Firebase.firestore // Instância do Firestore
    private val auth = FirebaseAuth.getInstance()

    // Usando SharedPreferences para armazenar os dados do usuário
    private val prefs = context.getSharedPreferences("dados_usuario", Context.MODE_PRIVATE)

    // Hilt Module para fornecer o SharedPreferences
    @Module
    @InstallIn(SingletonComponent::class)
    object SharedPreferencesModule {
        @Provides
        @Singleton
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences("dados_usuario", Context.MODE_PRIVATE)
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

    fun entrar(email: String, senha: String, resultado: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    uid?.let {
                        salvarUid(it)
                        Log.d("TAG", "UidLogin: $it")
                        resultado(true, null)
                    } ?: resultado(false, "Erro ao obter UID")
                } else {
                    resultado(false, "Email ou senha incorretos")
                }
            }
    }

    // Função para salvar o ID do usuário no SharedPreferences
    fun salvarUid(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            prefs.edit().putString("uid", uid).apply()
            val parceiroUid = obterUidParceiro(uid)
            prefs.edit().putString("uid", uid).apply()
            Log.i("tag", "Salvou UID: $uid")
            Log.i("tag", "Salvou UID: $parceiroUid")
//            prefs.edit().putString("nomeUsuario", nome).apply()
//            prefs.edit().putString("emailUsuario", email).apply()
//            prefs.edit().putString("parceiroId", parceiroId).apply()
        }
    }

    // Função para obter o ID do usuário como um Flow do SharedPreferences
    fun obterUidFlow(): Flow<String?> {
        return flow {
            emit(prefs.getString("uid", null)) // Emite o uid, ou null se não existir
        }
    }

    // Função para obter o ID do usuário do parceiro do Firestore (assumindo um campo 'parceiroId')
    suspend fun obterUidParceiro(usuarioId: String): String? {
        val query = db.collection("usuarios").document(usuarioId).get().await()
        return if (query.exists()){
            query.data?.get("parceiroId") as String?
        } else{
            null
        }
    }

    // Função para lidar com o logout
    fun sair() {
        // Implemente a lógica de logout aqui, como:
        auth.signOut()

        // Limpa o ID do usuário do SharedPreferences
        CoroutineScope(Dispatchers.IO).launch {
            prefs.edit().remove("uid").apply() // Remove o uid
        }

    }

    // Outros métodos relacionados à sessão, como verificar se o usuário está logado, etc.
}