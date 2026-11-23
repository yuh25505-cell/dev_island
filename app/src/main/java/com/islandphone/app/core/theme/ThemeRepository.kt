package com.islandphone.app.core.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_settings")

private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")

class ThemeRepository(private val context: Context) {

    val mode: Flow<ThemeMode> = context.themeDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { prefs -> ThemeMode.fromKey(prefs[KEY_THEME_MODE]) }

    suspend fun setMode(mode: ThemeMode) {
        context.themeDataStore.edit { prefs -> prefs[KEY_THEME_MODE] = mode.key }
    }
}
