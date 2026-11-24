package com.island.phone

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.island.phone.core.ui.theme.ThemeMode

object ThemeController {
    private const val FILE = "island_settings"
    private const val KEY = "theme_mode"

    var mode by mutableStateOf(ThemeMode.FOLLOW_SYSTEM)
        private set

    fun init(context: Context) {
        val saved = context.getSharedPreferences(FILE, Context.MODE_PRIVATE).getString(KEY, null)
        mode = ThemeMode.entries.firstOrNull { it.name == saved } ?: ThemeMode.FOLLOW_SYSTEM
    }

    fun set(context: Context, newMode: ThemeMode) {
        mode = newMode
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY, newMode.name)
            .apply()
    }
}
