package com.island.phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import com.island.phone.core.ui.theme.IslandTheme
import com.island.phone.core.ui.theme.resolveDark
import com.island.phone.feature.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ThemeController.init(applicationContext)
        setContent {
            val mode = ThemeController.mode
            val dark = mode.resolveDark()
            DisposableEffect(dark) {
                enableEdgeToEdge(
                    statusBarStyle = transparentBarStyle(dark),
                    navigationBarStyle = transparentBarStyle(dark)
                )
                onDispose { }
            }
            IslandTheme(mode = mode) {
                HomeScreen()
            }
        }
    }
}

private fun transparentBarStyle(dark: Boolean): SystemBarStyle {
    val transparent = android.graphics.Color.TRANSPARENT
    return if (dark) {
        SystemBarStyle.dark(transparent)
    } else {
        SystemBarStyle.light(transparent, transparent)
    }
}
