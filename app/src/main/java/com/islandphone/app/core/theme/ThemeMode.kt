package com.islandphone.app.core.theme

enum class ThemeMode(val key: String) {
    FOLLOW_SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromKey(key: String?): ThemeMode =
            entries.firstOrNull { it.key == key } ?: FOLLOW_SYSTEM
    }
}
