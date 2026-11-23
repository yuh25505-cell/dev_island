package com.islandphone.app.core.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val shape = RoundedCornerShape(26.dp)
    val glass = if (dark) listOf(Color.White.copy(alpha=.14f), Color.White.copy(alpha=.06f)) else listOf(Color.White.copy(alpha=.30f), Color.White.copy(alpha=.10f))
    val edge = if (dark) Color.White.copy(alpha=.20f) else Color.White.copy(alpha=.38f)
    Box(modifier.clip(shape).background(Brush.verticalGradient(glass)).border(1.dp, edge, shape)) { content() }
}
