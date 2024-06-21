package com.example.diariodememorias.data.repositorio

import android.content.Context
import android.content.SharedPreferences
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
class GerenciadorDeSessao @Inject constructor(@ApplicationContext private val context: Context) {

    private val db = Firebase.firestore // Instância do Firestore

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

    // Função para salvar o ID do usuário no SharedPreferences
    fun salvarUid(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            prefs.edit().putString("uid", uid).apply() // Salva o uid
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
        val query = db.collection("usuarios").whereIn("uid", listOf(usuarioId)).get().await()
        return if (query.documents.isNotEmpty()){
            query.documents[0].data?.get("parceiroId")?.toString()
        } else{
            null
        }
    }

    // Função para lidar com o logout
    fun logout() {
        // Implemente a lógica de logout aqui, como:
        // Firebase.auth.signOut()

        // Limpa o ID do usuário do SharedPreferences
        CoroutineScope(Dispatchers.IO).launch {
            prefs.edit().remove("uid").apply() // Remove o uid
        }
    }

    // Outros métodos relacionados à sessão, como verificar se o usuário está logado, etc.
}