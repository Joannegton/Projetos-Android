package com.example.diariodememorias.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class GerenciadorDeSessao @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("dados_usuario")
    private val UID_KEY = stringPreferencesKey("uid")

    fun salvarUid(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { preferences ->
                preferences[UID_KEY] = uid
            }
        }
    }

    fun obterUidFlow(): Flow<String?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    flowOf(null)
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[UID_KEY]
            }
    }

    fun logout() {
        // Implemente a lógica de logout aqui, como:
        // Firebase.auth.signOut()
        // Limpar o UID do DataStore:
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { preferences ->
                preferences.remove(UID_KEY)
            }
        }
    }

    // Outros métodos relacionados à sessão, como verificar se o usuário está logado, etc.
}