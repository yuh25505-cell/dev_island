package com.island.phone

import android.content.Context
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.theme.ThemeMode

object ThemeController {
    private var store: AppearanceStore? = null

    val mode: ThemeMode
        get() = store?.themeMode ?: ThemeMode.FOLLOW_SYSTEM

    fun init(context: Context) {
        store = AppearanceStore.get(context)
    }

    fun set(context: Context, newMode: ThemeMode) {
        AppearanceStore.get(context).setThemeMode(newMode)
    }
}
