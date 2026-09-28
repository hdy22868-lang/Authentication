package com.example.authentication.auth.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("auth_datastore")

class AuthPreferences(private val appContext: Context) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
    val accessTokenFlow: Flow<String?> = appContext.dataStore.data
        .map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }

    // جلب الـ Refresh Token كـ Flow
    val refreshTokenFlow: Flow<String?> = appContext.dataStore.data
        .map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }

    // حفظ التوكنات بعد تسجيل الدخول الناجح
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    // مسح التوكنات عند تسجيل الخروج (Logout)
    suspend fun clearTokens() {
        appContext.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
}
