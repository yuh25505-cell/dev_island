package com.islandphone.app.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightPalette = lightColorScheme(
    primary = Color(0xFF526B83), secondary = Color(0xFF7C91A5),
    background = Color(0xFFF2F4F7), surface = Color(0xFFF8F9FB),
    onBackground = Color(0xFF1B2027), onSurface = Color(0xFF1B2027)
)
private val DarkPalette = darkColorScheme(
    primary = Color(0xFFB4C9DC), secondary = Color(0xFF91A9BF),
    background = Color(0xFF101318), surface = Color(0xFF1A1F26),
    onBackground = Color(0xFFE9EDF2), onSurface = Color(0xFFE9EDF2)
)
@Composable
fun IslandTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) DarkPalette else LightPalette, content = content)
}
