package com.island.phone.core.ui.theme

import androidx.compose.ui.graphics.Color

data class GlassTokens(
    val tint: Color,
    val tintFallback: Color,
    val highlight: Color,
    val rimLight: Color,
    val rimDark: Color,
    val shadow: Color,
)

data class WallpaperTokens(
    val top: Color,
    val bottom: Color,
    val blobA: Color,
    val blobB: Color,
    val blobC: Color,
)

data class IconTokens(
    val glyph: Color,
    val glyphShadow: Color,
    val tileTint: Color,
    val label: Color,
    val labelShadow: Color,
)

val DayGlass = GlassTokens(
    tint = Color(0x33FFFFFF),
    tintFallback = Color(0x66FFFFFF),
    highlight = Color(0x40FFFFFF),
    rimLight = Color(0xB3FFFFFF),
    rimDark = Color(0x26FFFFFF),
    shadow = Color(0x33000000),
)

val NightGlass = GlassTokens(
    tint = Color(0x1AFFFFFF),
    tintFallback = Color(0x40FFFFFF),
    highlight = Color(0x24FFFFFF),
    rimLight = Color(0x66FFFFFF),
    rimDark = Color(0x14FFFFFF),
    shadow = Color(0x66000000),
)

val DayWallpaper = WallpaperTokens(
    top = Color(0xFFC9D6E8),
    bottom = Color(0xFFEAE3F0),
    blobA = Color(0xFFF4B6C2),
    blobB = Color(0xFF9CC5E8),
    blobC = Color(0xFFFFE2A8),
)

val NightWallpaper = WallpaperTokens(
    top = Color(0xFF070B16),
    bottom = Color(0xFF1B2540),
    blobA = Color(0xFF3B4C9B),
    blobB = Color(0xFF6A3C8F),
    blobC = Color(0xFF1E6F7A),
)

val DayIcon = IconTokens(
    glyph = Color(0xF23F4B6B),
    glyphShadow = Color(0x66FFFFFF),
    tileTint = Color(0x26FFFFFF),
    label = Color(0xFF2B2F38),
    labelShadow = Color(0x00000000),
)

val NightIcon = IconTokens(
    glyph = Color(0xF2FFFFFF),
    glyphShadow = Color(0x66000000),
    tileTint = Color(0x12FFFFFF),
    label = Color(0xFFFFFFFF),
    labelShadow = Color(0x66000000),
)
