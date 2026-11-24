package com.island.phone.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.island.phone.core.ui.theme.IslandTheme

@Composable
fun Wallpaper(modifier: Modifier = Modifier) {
    val tokens = IslandTheme.wallpaper
    Box(
        modifier = modifier.drawBehind {
            drawRect(Brush.verticalGradient(listOf(tokens.top, tokens.bottom)))
            val base = size.minDimension
            blob(tokens.blobA, Offset(size.width * 0.2f, size.height * 0.22f), base * 0.75f)
            blob(tokens.blobB, Offset(size.width * 0.85f, size.height * 0.5f), base * 0.8f)
            blob(tokens.blobC, Offset(size.width * 0.3f, size.height * 0.85f), base * 0.7f)
        }
    )
}

private fun DrawScope.blob(color: Color, center: Offset, radius: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(color.copy(alpha = 0.85f), color.copy(alpha = 0f)),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
}
