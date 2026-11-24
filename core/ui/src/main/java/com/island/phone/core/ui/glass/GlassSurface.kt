package com.island.phone.core.ui.glass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawOutline
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.theme.GlassTokens
import com.island.phone.core.ui.theme.IslandTheme

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
    content: @Composable BoxScope.() -> Unit = {},
) {
    val backdrop = LocalBackdrop.current
    val glass = IslandTheme.glass
    var origin by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier = modifier
            .shadow(14.dp, shape, clip = false, ambientColor = glass.shadow, spotColor = glass.shadow)
            .clip(shape)
            .onGloballyPositioned { origin = it.positionInRoot() }
            .drawBehind { drawGlass(shape, backdrop, origin, glass) },
        content = content
    )
}

private fun DrawScope.drawGlass(
    shape: Shape,
    backdrop: BackdropState?,
    origin: Offset,
    glass: GlassTokens,
) {
    val blurOn = backdrop != null && backdrop.blurSupported
    if (backdrop != null && blurOn) {
        val delta = origin - backdrop.origin
        translate(-delta.x, -delta.y) { drawLayer(backdrop.blurred) }
    }
    drawRect(if (blurOn) glass.tint else glass.tintFallback)
    drawRect(
        Brush.verticalGradient(
            colors = listOf(glass.highlight, Color.Transparent),
            startY = 0f,
            endY = size.height * 0.55f
        )
    )
    val outline = shape.createOutline(size, layoutDirection, this)
    val rim = Brush.linearGradient(
        colors = listOf(glass.rimLight, glass.rimDark, glass.rimLight),
        start = Offset.Zero,
        end = Offset(size.width, size.height)
    )
    drawOutline(outline, rim, alpha = 0.35f, style = Stroke(10.dp.toPx()))
    drawOutline(outline, rim, style = Stroke(2.dp.toPx()))
}
