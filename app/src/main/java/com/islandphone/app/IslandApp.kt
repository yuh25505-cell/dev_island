package com.islandphone.app

import android.app.Application
import com.islandphone.app.core.theme.ThemeRepository

class IslandApp : Application() {
    val themeRepository: ThemeRepository by lazy { ThemeRepository(this) }
}
