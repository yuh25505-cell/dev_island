package com.island.phone.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.island.phone.core.ui.appearance.AppearanceStore

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
private val LocalFolder = staticCompositionLocalOf { DayFolder }
private val LocalWidget = staticCompositionLocalOf { DayWidget }

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
    val folder: FolderTokens
        @Composable @ReadOnlyComposable get() = LocalFolder.current
    val widget: WidgetTokens
        @Composable @ReadOnlyComposable get() = LocalWidget.current
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
        LocalFolder provides (if (dark) NightFolder else DayFolder),
        LocalWidget provides (if (dark) NightWidget else DayWidget),
        content = content
    )
}

@Composable
fun IslandDesktopTheme(content: @Composable () -> Unit) {
    val luma = AppearanceStore.get(LocalContext.current).wallpaperLuma
    val dark = IslandTheme.dark
    val base = IslandTheme.icon
    val tokens = if (luma == null) {
        base
    } else {
        val effective = if (dark) luma * 0.7f else luma
        if (effective < 0.55f) {
            base.copy(label = Color.White, labelShadow = Color(0x66000000))
        } else {
            base.copy(label = Color(0xFF2B2F38), labelShadow = Color.Transparent)
        }
    }
    CompositionLocalProvider(LocalIcon provides tokens, content = content)
}
