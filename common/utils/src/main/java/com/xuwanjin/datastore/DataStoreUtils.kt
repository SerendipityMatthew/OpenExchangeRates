package com.xuwanjin.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "OERDataStore")

/**
 * the datastore for all module
 */
object DataStoreUtils {
    private lateinit var dataStore: DataStore<Preferences>

    private const val CURRENCY_LAST_UPDATED_TIME: String = "currency_last_updated_time"

    fun init(context: Context) {
        if (!this::dataStore.isInitialized) {
            dataStore = context.dataStore
        }
    }

    fun getLastUpdatedTime(): Long {
        return readLongData(CURRENCY_LAST_UPDATED_TIME, 0)
    }

    fun setCurrencyUpdatedTime(timestampInSeconds: Long = System.currentTimeMillis().div(1000)) {
        saveSyncLongData(CURRENCY_LAST_UPDATED_TIME, timestampInSeconds)
    }

    /**
     *  the generic method for fetching all data
     */
    @Suppress("UNCHECKED_CAST")
    fun <Data> getSyncData(key: String, default: Data): Data {
        val res = when (default) {
            is Long -> readLongData(key, default)
            is String -> readStringData(key, default)
            is Int -> readIntData(key, default)
            is Boolean -> readBooleanData(key, default)
            is Float -> readFloatData(key, default)
            else -> throw IllegalArgumentException("This type can not be fetched")
        }
        return res as Data
    }

    /**
     *  the generic method for saving all data
     */
    fun <Data> putSyncData(key: String, value: Data) {
        when (value) {
            is Long -> saveSyncLongData(key, value)
            is String -> saveSyncStringData(key, value)
            is Int -> saveSyncIntData(key, value)
            is Boolean -> saveSyncBooleanData(key, value)
            is Float -> saveSyncFloatData(key, value)
            else -> throw IllegalArgumentException("This type can not be saved into DataStore")
        }
    }

    fun readBooleanData(key: String, default: Boolean = false): Boolean = runBlocking {
        var value = false
        dataStore.data.first {
            value = it[booleanPreferencesKey(key)] ?: default
            true
        }
        value
    }

    fun readIntData(key: String, default: Int = 0): Int = runBlocking {
        var value = 0
        dataStore.data.first {
            value = it[intPreferencesKey(key)] ?: default
            true
        }
        value
    }

    fun readStringData(key: String, default: String = ""): String = runBlocking {
        var value = ""
        dataStore.data.first {
            value = it[stringPreferencesKey(key)] ?: default
            true
        }
        value
    }

    fun readFloatData(key: String, default: Float = 0f): Float = runBlocking {
        var value = 0f
        dataStore.data.first {
            value = it[floatPreferencesKey(key)] ?: default
            true
        }
        value
    }

    fun readLongData(key: String, default: Long = 0L): Long = runBlocking {
        var value = 0L
        dataStore.data.first {
            value = it[longPreferencesKey(key)] ?: default
            true
        }
        value
    }

    suspend fun saveBooleanData(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    fun saveSyncBooleanData(key: String, value: Boolean) =
        runBlocking { saveBooleanData(key, value) }

    suspend fun saveIntData(key: String, value: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }

    fun saveSyncIntData(key: String, value: Int) = runBlocking { saveIntData(key, value) }

    suspend fun saveStringData(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    fun saveSyncStringData(key: String, value: String) = runBlocking { saveStringData(key, value) }

    suspend fun saveFloatData(key: String, value: Float) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[floatPreferencesKey(key)] = value
        }
    }

    fun saveSyncFloatData(key: String, value: Float) = runBlocking { saveFloatData(key, value) }

    suspend fun saveLongData(key: String, value: Long) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[longPreferencesKey(key)] = value
        }
    }

    fun saveSyncLongData(key: String, value: Long) = runBlocking { saveLongData(key, value) }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}
