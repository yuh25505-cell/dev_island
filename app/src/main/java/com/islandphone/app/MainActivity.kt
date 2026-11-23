package com.islandphone.app

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import com.islandphone.app.core.theme.IslandTheme
import com.islandphone.app.core.theme.isDarkFor
import com.islandphone.app.core.theme.islandColorsFor
import com.islandphone.app.home.IslandRoot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themeRepository = (application as IslandApp).themeRepository
        val initialMode = runBlocking { themeRepository.mode.first() }

        setContent {
            val scope = rememberCoroutineScope()
            val mode by themeRepository.mode.collectAsState(initial = initialMode)
            val dark = isDarkFor(mode)

            DisposableEffect(dark) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { dark },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { dark }
                )
                window.setBackgroundDrawable(
                    ColorDrawable(islandColorsFor(dark).background.toArgb())
                )
                onDispose {}
            }

            IslandTheme(mode = mode) {
                IslandRoot(
                    themeMode = mode,
                    onThemeModeChange = { newMode ->
                        scope.launch { themeRepository.setMode(newMode) }
                    }
                )
            }
        }
    }
}
