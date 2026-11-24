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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class BackdropState internal constructor(
    val raw: GraphicsLayer,
    val blurred: GraphicsLayer,
    val blurSupported: Boolean,
) {
    var origin by mutableStateOf(Offset.Zero)
}

val LocalBackdrop = staticCompositionLocalOf<BackdropState?> { null }

@Composable
fun rememberBackdropState(blurRadius: Dp = 28.dp): BackdropState {
    val raw = rememberGraphicsLayer()
    val blurred = rememberGraphicsLayer()
    val density = LocalDensity.current
    return remember(raw, blurred, density, blurRadius) {
        val supported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        if (supported) {
            val px = with(density) { blurRadius.toPx() }
            blurred.renderEffect = BlurEffect(px, px, TileMode.Clamp)
        }
        BackdropState(raw, blurred, supported)
    }
}

fun Modifier.backdropSource(state: BackdropState): Modifier = this
    .onGloballyPositioned { state.origin = it.positionInRoot() }
    .drawWithContent {
        state.raw.record { this@drawWithContent.drawContent() }
        state.blurred.record { drawLayer(state.raw) }
        drawLayer(state.raw)
    }

@Composable
fun OwnBackdrop(
    modifier: Modifier = Modifier,
    blurRadius: Dp = 28.dp,
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
