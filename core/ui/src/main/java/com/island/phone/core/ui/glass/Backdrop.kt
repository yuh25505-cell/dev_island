package com.island.phone.core.ui.glass

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.theme.IslandTheme

class BackdropState internal constructor(
    val raw: GraphicsLayer,
    val blurred: GraphicsLayer,
    val blurSupported: Boolean,
    internal val settings: GlassSettings,
    internal val fixedBlur: Dp?,
) {
    var origin by mutableStateOf(Offset.Zero)

    private var appliedBlur = -1f
    private var appliedSaturation = -1f

    internal fun applyEffects(density: Density) {
        if (!blurSupported) return
        val fixed = fixedBlur
        val px = with(density) { if (fixed != null) fixed.toPx() else settings.blur.dp.toPx() }
        if (px != appliedBlur) {
            appliedBlur = px
            blurred.renderEffect = if (px > 0.5f) BlurEffect(px, px, TileMode.Clamp) else null
        }
        val saturation = settings.saturation
        if (saturation != appliedSaturation) {
            appliedSaturation = saturation
            blurred.colorFilter = if (saturation == 1f) {
                null
            } else {
                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(saturation) })
            }
        }
    }
}

val LocalBackdrop = staticCompositionLocalOf<BackdropState?> { null }

@Composable
fun rememberBackdropState(blurRadius: Dp? = null): BackdropState {
    val raw = rememberGraphicsLayer()
    val blurred = rememberGraphicsLayer()
    val settings = GlassSettings.get(LocalContext.current)
    return remember(raw, blurred, settings, blurRadius) {
        BackdropState(raw, blurred, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S, settings, blurRadius)
    }
}

fun Modifier.backdropSource(state: BackdropState): Modifier = this
    .onGloballyPositioned { state.origin = it.positionInRoot() }
    .drawWithContent {
        state.applyEffects(this)
        state.raw.record { this@drawWithContent.drawContent() }
        state.blurred.record { drawLayer(state.raw) }
        drawLayer(state.raw)
    }

fun Modifier.backdropRecordOnly(state: BackdropState): Modifier = this
    .onGloballyPositioned { state.origin = it.positionInRoot() }
    .drawWithContent {
        state.applyEffects(this)
        state.raw.record { this@drawWithContent.drawContent() }
        state.blurred.record { drawLayer(state.raw) }
    }

@Composable
fun OwnBackdrop(
    modifier: Modifier = Modifier,
    blurRadius: Dp? = null,
    source: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val state = rememberBackdropState(blurRadius)
    Box(modifier = modifier) {
        Box(modifier = Modifier.matchParentSize().backdropSource(state), content = source)
        CompositionLocalProvider(LocalBackdrop provides state) {
            content()
        }
    }
}

@Composable
fun PopupBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val base = IslandTheme.wallpaper.top
    val state = rememberBackdropState()
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .backdropRecordOnly(state)
                .drawBehind {
                    drawRect(base)
                    val lightBase = base.luminance() > 0.5f
                    val soft = if (lightBase) Color(0x305A6C8A) else Color(0x1FFFFFFF)
                    val strong = if (lightBase) Color(0x80FFFFFF) else Color(0x40000000)
                    val r = size.width * 0.6f
                    val c1 = Offset(size.width * 0.2f, size.height * 0.25f)
                    val c2 = Offset(size.width * 0.85f, size.height * 0.5f)
                    val c3 = Offset(size.width * 0.35f, size.height * 0.85f)
                    drawCircle(Brush.radialGradient(listOf(soft, Color.Transparent), center = c1, radius = r), radius = r, center = c1)
                    drawCircle(Brush.radialGradient(listOf(strong, Color.Transparent), center = c2, radius = r), radius = r, center = c2)
                    drawCircle(Brush.radialGradient(listOf(soft, Color.Transparent), center = c3, radius = r), radius = r, center = c3)
                }
        )
        CompositionLocalProvider(LocalBackdrop provides state) {
            content()
        }
    }
}
