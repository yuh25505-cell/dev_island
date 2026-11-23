package com.islandphone.app.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class IslandColors(
    val isDark: Boolean,
    val background: Color,
    val onBackground: Color,
    val textSecondary: Color,
    val glassFill: Color,
    val glassBorder: Color,
    val accent: Color,
    val accentSoft: Color
)

val LightIslandColors = IslandColors(
    isDark = false,
    background = Color(0xFFF7F6EE),
    onBackground = Color(0xFF2B2522),
    textSecondary = Color(0xFF7A6B64),
    glassFill = Color(0x99FFFFFF),
    glassBorder = Color(0x33B89488),
    accent = Color(0xFFB89488),
    accentSoft = Color(0x47B89488)
)

val DarkIslandColors = IslandColors(
    isDark = true,
    background = Color(0xFF14161A),
    onBackground = Color(0xFFF1ECE6),
    textSecondary = Color(0xFFB5ABA4),
    glassFill = Color(0x1FFFFFFF),
    glassBorder = Color(0x2EFFFFFF),
    accent = Color(0xFFD9B3A5),
    accentSoft = Color(0x4DD9B3A5)
)

val LocalIslandColors = staticCompositionLocalOf { LightIslandColors }
