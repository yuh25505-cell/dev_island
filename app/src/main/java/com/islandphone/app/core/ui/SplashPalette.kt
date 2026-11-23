package com.islandphone.app.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.sin

internal typealias SplashTime = Animatable<Float, AnimationVector1D>

internal fun splashSmooth(x: Float): Float {
    val v = x.coerceIn(0f, 1f)
    return v * v * (3f - 2f * v)
}

internal fun splashWindow(t: Float, start: Float, end: Float): Float =
    ((t - start) / (end - start)).coerceIn(0f, 1f)

internal fun splashLerp(a: Float, b: Float, f: Float): Float = a + (b - a) * f

internal const val SPLASH_TOTAL_MS = 7200

internal const val BOAT_START = 0.03f
internal const val BOAT_END = 0.55f
internal const val CURTAIN_START = 0.64f
internal const val CURTAIN_END = 0.82f

internal const val ISLAND_X = 0.76f
internal const val ISLAND_Y = 0.585f
internal const val BOAT_LANE_Y = 0.655f
internal const val BOAT_STOP_X = 0.46f
internal const val BOAT_SIZE = 0.19f

internal fun splashLaneWave(x: Float, w: Float, h: Float, sec: Float): Float =
    h * 0.007f * sin(x / w * 2f * PI.toFloat() * 1.6f - sec * 1.8f)

internal class SplashPalette(
    val isDark: Boolean,
    val skyTop: Color,
    val skyBottom: Color,
    val sun: Color,
    val sunGlow: Color,
    val cloud: Color,
    val waterTop: Color,
    val waterBottom: Color,
    val waveLight: Color,
    val waveDeep: Color,
    val flow: Color,
    val mist: Color,
    val sand: Color,
    val sandShade: Color,
    val grass: Color,
    val grassDark: Color,
    val trunk: Color,
    val leaf: Color,
    val paperLight: Color,
    val paperMid: Color,
    val paperDark: Color,
    val lantern: Color
)

private val LightSplashPalette = SplashPalette(
    isDark = false,
    skyTop = Color(0xFFBFE4FF),
    skyBottom = Color(0xFFF3FAFF),
    sun = Color(0xFFFFF3C4),
    sunGlow = Color(0xFFFFE9A0),
    cloud = Color(0xFFFFFFFF),
    waterTop = Color(0xFF8AD2FF),
    waterBottom = Color(0xFF2AA0F0),
    waveLight = Color(0xFFFFFFFF),
    waveDeep = Color(0xFF0A7BD0),
    flow = Color(0xFFFFFFFF),
    mist = Color(0xFFFFFFFF),
    sand = Color(0xFFF6E2B3),
    sandShade = Color(0xFFE6C98E),
    grass = Color(0xFF66C987),
    grassDark = Color(0xFF43A868),
    trunk = Color(0xFF9C6D47),
    leaf = Color(0xFF3FB36F),
    paperLight = Color(0xFFFFFFFF),
    paperMid = Color(0xFFE9F2FA),
    paperDark = Color(0xFFC4D6E6),
    lantern = Color(0xFFFFD27A)
)

private val DarkSplashPalette = SplashPalette(
    isDark = true,
    skyTop = Color(0xFF070D16),
    skyBottom = Color(0xFF182C42),
    sun = Color(0xFFE9EFF8),
    sunGlow = Color(0xFF7FA6D6),
    cloud = Color(0xFF6C86A6),
    waterTop = Color(0xFF173A56),
    waterBottom = Color(0xFF08151F),
    waveLight = Color(0xFF7FB8E8),
    waveDeep = Color(0xFF030A10),
    flow = Color(0xFF9CCBF2),
    mist = Color(0xFF2A4560),
    sand = Color(0xFFB9A57E),
    sandShade = Color(0xFF8C7A58),
    grass = Color(0xFF2F7F57),
    grassDark = Color(0xFF225E41),
    trunk = Color(0xFF6E4E33),
    leaf = Color(0xFF2E8B5A),
    paperLight = Color(0xFFF3F6FA),
    paperMid = Color(0xFFC8D4E0),
    paperDark = Color(0xFF92A4B8),
    lantern = Color(0xFFFFC46B)
)

internal fun splashPalette(isDark: Boolean): SplashPalette =
    if (isDark) DarkSplashPalette else LightSplashPalette

internal fun Color.mul(a: Float): Color = copy(alpha = alpha * a)
