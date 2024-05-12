package com.sugarygary.instory.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    suspend fun setApiToken(token: String) {
        dataStore.edit { preferences ->
            preferences[STORY_JWT_TOKEN] = token
        }
    }

    suspend fun removeToken() {
        dataStore.edit { prefs ->
            prefs.remove(STORY_JWT_TOKEN)
        }
    }


    val storyJwtToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[STORY_JWT_TOKEN]
    }

    companion object {
        private val Context.dataStore by preferencesDataStore("settings")
        val STORY_JWT_TOKEN = stringPreferencesKey("story_jwt_token")
    }

}