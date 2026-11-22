package com.islandphone.app.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    background = Color(0xFFF2F5F7),
    surface = Color(0xFFF2F5F7),
    onBackground = Color(0xFF101417),
    onSurface = Color(0xFF101417)
)

private val DarkColors = darkColorScheme(
    background = Color(0xFF0E1114),
    surface = Color(0xFF0E1114),
    onBackground = Color(0xFFEAF0F3),
    onSurface = Color(0xFFEAF0F3)
)

@Composable
fun IslandTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
