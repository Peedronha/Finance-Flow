package br.com.utfpr.financeflow.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    companion object {
        val META_PERSONALIZADA = doublePreferencesKey("meta_personalizada")
        val MODO_ESCURO = booleanPreferencesKey("modo_escuro")
    }

    val metaPersonalizada: Flow<Double> = context.dataStore.data
        .map { preferences ->
            preferences[META_PERSONALIZADA] ?: 0.0
        }

    val modoEscuro: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[MODO_ESCURO] ?: false
        }

    suspend fun salvarMeta(valor: Double) {
        context.dataStore.edit { preferences ->
            preferences[META_PERSONALIZADA] = valor
        }
    }

    suspend fun salvarModoEscuro(ativo: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MODO_ESCURO] = ativo
        }
    }
}