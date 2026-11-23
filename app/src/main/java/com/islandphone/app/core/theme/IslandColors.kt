package com.islandphone.app.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class IslandColors(
    val isDark: Boolean,
    val background: Color,
    val backgroundGrouped: Color,
    val backgroundElevated: Color,
    val backgroundTint: Color,
    val onBackground: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textQuaternary: Color,
    val separator: Color,
    val glassFill: Color,
    val glassHighlight: Color,
    val glassEdgeHighlight: Color,
    val glassBorder: Color,
    val dialogSurface: Color,
    val accent: Color,
    val onAccent: Color,
    val accentSoft: Color,
    val danger: Color,
    val orange: Color,
    val yellow: Color,
    val green: Color,
    val gray: Color,
    val gray2: Color,
    val gray3: Color,
    val gray4: Color,
    val gray5: Color,
    val gray6: Color
)

val LightIslandColors = IslandColors(
    isDark = false,
    background = Color(0xFFFFFFFF),
    backgroundGrouped = Color(0xFFF2F2F7),
    backgroundElevated = Color(0xFFFFFFFF),
    backgroundTint = Color(0xFFF2F2F7),
    onBackground = Color(0xFF000000),
    textSecondary = Color(0x993C3C43),
    textTertiary = Color(0x4D3C3C43),
    textQuaternary = Color(0x2E3C3C43),
    separator = Color(0x4A3C3C43),
    glassFill = Color(0xB3FFFFFF),
    glassHighlight = Color(0x66FFFFFF),
    glassEdgeHighlight = Color(0xB3FFFFFF),
    glassBorder = Color(0x503C3C43),
    dialogSurface = Color(0xD9FFFFFF),
    accent = Color(0xFF0088FF),
    onAccent = Color(0xFFFFFFFF),
    accentSoft = Color(0x290088FF),
    danger = Color(0xFFFF383C),
    orange = Color(0xFFFF8D28),
    yellow = Color(0xFFFFCC00),
    green = Color(0xFF34C759),
    gray = Color(0xFF8E8E93),
    gray2 = Color(0xFFAEAEB2),
    gray3 = Color(0xFFC7C7CC),
    gray4 = Color(0xFFD1D1D6),
    gray5 = Color(0xFFE5E5EA),
    gray6 = Color(0xFFF2F2F7)
)

val DarkIslandColors = IslandColors(
    isDark = true,
    background = Color(0xFF000000),
    backgroundGrouped = Color(0xFF1C1C1E),
    backgroundElevated = Color(0xFF2C2C2E),
    backgroundTint = Color(0xFF1C1C1E),
    onBackground = Color(0xFFFFFFFF),
    textSecondary = Color(0x99EBEBF5),
    textTertiary = Color(0x4DEBEBF5),
    textQuaternary = Color(0x29EBEBF5),
    separator = Color(0x99545458),
    glassFill = Color(0x35FFFFFF),
    glassHighlight = Color(0x24FFFFFF),
    glassEdgeHighlight = Color(0x55FFFFFF),
    glassBorder = Color(0x32FFFFFF),
    dialogSurface = Color(0xC92C2C2E),
    accent = Color(0xFF0091FF),
    onAccent = Color(0xFFFFFFFF),
    accentSoft = Color(0x330091FF),
    danger = Color(0xFFFF4245),
    orange = Color(0xFFFF9230),
    yellow = Color(0xFFFFD600),
    green = Color(0xFF30D158),
    gray = Color(0xFF8E8E93),
    gray2 = Color(0xFF636366),
    gray3 = Color(0xFF48484A),
    gray4 = Color(0xFF3A3A3C),
    gray5 = Color(0xFF2C2C2E),
    gray6 = Color(0xFF1C1C1E)
)

val LocalIslandColors = staticCompositionLocalOf { LightIslandColors }
