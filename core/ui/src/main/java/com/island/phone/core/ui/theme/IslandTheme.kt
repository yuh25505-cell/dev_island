package com.island.phone.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

enum class ThemeMode { FOLLOW_SYSTEM, DAY, NIGHT }

@Composable
fun ThemeMode.resolveDark(): Boolean = when (this) {
    ThemeMode.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    ThemeMode.DAY -> false
    ThemeMode.NIGHT -> true
}

private val LocalDark = staticCompositionLocalOf { false }
private val LocalGlass = staticCompositionLocalOf { DayGlass }
private val LocalWallpaper = staticCompositionLocalOf { DayWallpaper }
private val LocalIcon = staticCompositionLocalOf { DayIcon }
private val LocalDots = staticCompositionLocalOf { DayDots }

object IslandTheme {
    val dark: Boolean
        @Composable @ReadOnlyComposable get() = LocalDark.current
    val glass: GlassTokens
        @Composable @ReadOnlyComposable get() = LocalGlass.current
    val wallpaper: WallpaperTokens
        @Composable @ReadOnlyComposable get() = LocalWallpaper.current
    val icon: IconTokens
        @Composable @ReadOnlyComposable get() = LocalIcon.current
    val dots: PageDotTokens
        @Composable @ReadOnlyComposable get() = LocalDots.current
}

@Composable
fun IslandTheme(mode: ThemeMode, content: @Composable () -> Unit) {
    val dark = mode.resolveDark()
    CompositionLocalProvider(
        LocalDark provides dark,
        LocalGlass provides (if (dark) NightGlass else DayGlass),
        LocalWallpaper provides (if (dark) NightWallpaper else DayWallpaper),
        LocalIcon provides (if (dark) NightIcon else DayIcon),
        LocalDots provides (if (dark) NightDots else DayDots),
        content = content
    )
}
