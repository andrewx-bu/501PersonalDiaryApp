package com.example.personaldiaryapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme")
        val FONT_SIZE_KEY = intPreferencesKey("font_size")
    }

    val themeFlow: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[THEME_KEY] }

    val fontSizeFlow: Flow<Int?> = context.dataStore.data
        .map { prefs -> prefs[FONT_SIZE_KEY] }

    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme
        }
    }

    suspend fun saveFontSize(size: Int) {
        context.dataStore.edit { prefs ->
            prefs[FONT_SIZE_KEY] = size
        }
    }
}
