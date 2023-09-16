package com.example.todoapp.DataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class StoreToDo(private val context: Context) {
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ToDoStore")
        const val KEY_FIRST_TIME_LAUNCH = "KEY_FIRST_TIME_LAUNCH"
        const val KEY_DARK_MODE = "KEY_DARK_MODE"
    }
    val getDarkMode : Flow<Boolean?> = context.dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(KEY_DARK_MODE)] ?: false
    }
    suspend fun write(key : String, value : Boolean){
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
    suspend fun read(key : String, value : Boolean) : Boolean{
        val dataStoreKey = booleanPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[dataStoreKey] ?: value
    }
}