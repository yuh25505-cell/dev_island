package com.islandphone.app.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.Shape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.islandphone.app.core.theme.IslandTheme

fun Modifier.glassSurface(
    shape: Shape,
    elevation: androidx.compose.ui.unit.Dp = 4.dp
): Modifier {
    val colors = IslandTheme.colors
    val highlight = Brush.verticalGradient(
        listOf(
            colors.glassHighlight,
            Color.Transparent,
            colors.glassHighlight.copy(alpha = colors.glassHighlight.alpha * 0.18f)
        )
    )
    val rim = Brush.verticalGradient(
        listOf(colors.glassEdgeHighlight, colors.glassBorder, colors.glassEdgeHighlight.copy(alpha = 0.35f))
    )
    return this
        .shadow(elevation, shape, clip = false)
        .clip(shape)
        .background(colors.glassFill)
        .background(highlight)
        .border(BorderStroke(1.dp, rim), shape)
}
