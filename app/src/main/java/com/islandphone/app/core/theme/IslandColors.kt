package com.islandphone.app.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class IslandColors(
    val isDark: Boolean,
    val background: Color,
    val backgroundTint: Color,
    val onBackground: Color,
    val textSecondary: Color,
    val glassFill: Color,
    val glassHighlight: Color,
    val glassBorder: Color,
    val dialogSurface: Color,
    val accent: Color,
    val onAccent: Color,
    val accentSoft: Color,
    val danger: Color
)

val LightIslandColors = IslandColors(
    isDark = false,
    background = Color(0xFFFFFFFF),
    backgroundTint = Color(0xFFE3F3FF),
    onBackground = Color(0xFF14181D),
    textSecondary = Color(0xFF6B7684),
    glassFill = Color(0xB3FFFFFF),
    glassHighlight = Color(0x80FFFFFF),
    glassBorder = Color(0x330099FF),
    dialogSurface = Color(0xF7FFFFFF),
    accent = Color(0xFF0099FF),
    onAccent = Color(0xFFFFFFFF),
    accentSoft = Color(0x290099FF),
    danger = Color(0xFFFF3B30)
)

val DarkIslandColors = IslandColors(
    isDark = true,
    background = Color(0xFF0E1013),
    backgroundTint = Color(0xFF12202D),
    onBackground = Color(0xFFF2F4F7),
    textSecondary = Color(0xFF9AA5B1),
    glassFill = Color(0x1FFFFFFF),
    glassHighlight = Color(0x14FFFFFF),
    glassBorder = Color(0x2EFFFFFF),
    dialogSurface = Color(0xF21A1E25),
    accent = Color(0xFF3DB2FF),
    onAccent = Color(0xFF06121F),
    accentSoft = Color(0x4D3DB2FF),
    danger = Color(0xFFFF6B61)
)

val LocalIslandColors = staticCompositionLocalOf { LightIslandColors }
