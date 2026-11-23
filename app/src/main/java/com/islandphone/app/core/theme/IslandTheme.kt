package com.islandphone.app.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun isDarkFor(mode: ThemeMode): Boolean = when (mode) {
    ThemeMode.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}

fun islandColorsFor(dark: Boolean): IslandColors =
    if (dark) DarkIslandColors else LightIslandColors

private fun buildColorScheme(c: IslandColors): ColorScheme =
    if (c.isDark) {
        darkColorScheme(
            primary = c.accent,
            onPrimary = c.onAccent,
            background = c.background,
            onBackground = c.onBackground,
            surface = c.background,
            onSurface = c.onBackground,
            onSurfaceVariant = c.textSecondary
        )
    } else {
        lightColorScheme(
            primary = c.accent,
            onPrimary = c.onAccent,
            background = c.background,
            onBackground = c.onBackground,
            surface = c.background,
            onSurface = c.onBackground,
            onSurfaceVariant = c.textSecondary
        )
    }

object IslandTheme {
    val colors: IslandColors
        @Composable
        @ReadOnlyComposable
        get() = LocalIslandColors.current
}

@Composable
fun IslandTheme(
    mode: ThemeMode = ThemeMode.FOLLOW_SYSTEM,
    content: @Composable () -> Unit
) {
    val colors = islandColorsFor(isDarkFor(mode))
    CompositionLocalProvider(LocalIslandColors provides colors) {
        MaterialTheme(colorScheme = buildColorScheme(colors), content = content)
    }
}
