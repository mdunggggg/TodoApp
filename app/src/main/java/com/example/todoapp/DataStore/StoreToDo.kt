package com.example.todoapp.DataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class StoreToDo(private val context: Context) {
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ToDoStore")
        const val KEY_FIRST_TIME_LAUNCH = "KEY_FIRST_TIME_LAUNCH"
        const val KEY_USER_NAME = "KEY_USER_NAME"
        const val KEY_USER_EMAIL = "KEY_USER_EMAIL"
        const val KEY_AVATAR = "KEY_AVATAR"
        const val KEY_COVER_IMAGE = "KEY_COVER_IMAGE"
        const val KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER"
        const val KEY_BIRTHDAY = "KEY_BIRTHDAY"
    }
    suspend fun write(key : String, value : Boolean){
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
    suspend fun read(key : String, value: Boolean) : Boolean{
        val dataStoreKey = booleanPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[dataStoreKey] ?: value
    }
    suspend fun write(key : String, value : String){
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
    suspend fun readString(key : String, value: String) : String{
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[dataStoreKey] ?: value
    }

}