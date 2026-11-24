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

val DayGlass = GlassTokens(
    tint = Color(0x33FFFFFF),
    tintFallback = Color(0x66FFFFFF),
    highlight = Color(0x66FFFFFF),
    rimLight = Color(0xCCFFFFFF),
    rimDark = Color(0x33FFFFFF),
    shadow = Color(0x33000000),
)

val NightGlass = GlassTokens(
    tint = Color(0x1AFFFFFF),
    tintFallback = Color(0x40FFFFFF),
    highlight = Color(0x33FFFFFF),
    rimLight = Color(0x80FFFFFF),
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
