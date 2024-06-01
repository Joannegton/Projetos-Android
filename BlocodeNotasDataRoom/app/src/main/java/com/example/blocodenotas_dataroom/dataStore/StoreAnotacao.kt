package com.example.blocodenotas_dataroom.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreAnotacao(private val context: Context) {

    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuracoes")
        val ANOTACAO_KET = stringPreferencesKey("anotacao")
    }

    val getAnotacao: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[ANOTACAO_KET] ?: ""
        }

    suspend fun salvarAnotacao(anotacao: String){
        context.dataStore.edit { preferences ->
            preferences[ANOTACAO_KET] = anotacao
        }

    }
}