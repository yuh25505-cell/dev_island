package com.island.phone.feature.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.theme.IslandTheme

@Composable
fun VinylWidget(modifier: Modifier = Modifier) {
    val store = VinylStore.get(LocalContext.current)
    val label = IslandTheme.icon.label
    GlassSurface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        elevation = 10.dp
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val u = maxWidth.value
            val record = (u * 0.76f).dp
            val shift = Modifier
                .align(Alignment.Center)
                .offset(y = (u * 0.04f).dp)
            Canvas(modifier = shift.size(record)) {
                val r = size.width / 2f
                val c = Offset(r, r)
                drawCircle(color = label.copy(alpha = 0.08f), radius = r, center = c)
                for (i in 0..6) {
                    drawCircle(
                        color = label.copy(alpha = 0.18f),
                        radius = r * (0.76f + i * 0.033f),
                        center = c,
                        style = Stroke(width = 0.7.dp.toPx())
                    )
                }
                drawCircle(
                    color = label.copy(alpha = 0.5f),
                    radius = r - 1.dp.toPx(),
                    center = c,
                    style = Stroke(width = 1.6.dp.toPx())
                )
            }
            val cover = store.cover
            val coverModifier = shift
                .size((u * 0.76f * 0.72f).dp)
                .clip(CircleShape)
                .background(label.copy(alpha = 0.22f))
            if (cover != null) {
                Image(
                    bitmap = cover,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = coverModifier
                )
            } else {
                androidx.compose.foundation.layout.Box(modifier = coverModifier)
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val pivot = Offset(w * 0.84f, w * 0.17f)
                drawCircle(
                    color = label.copy(alpha = 0.9f),
                    radius = w * 0.05f,
                    center = pivot,
                    style = Stroke(width = w * 0.016f)
                )
                drawCircle(color = label.copy(alpha = 0.9f), radius = w * 0.027f, center = pivot)
                val arm = Path().apply {
                    moveTo(w * 0.84f, w * 0.22f)
                    lineTo(w * 0.84f, w * 0.62f)
                    lineTo(w * 0.73f, w * 0.77f)
                }
                drawPath(
                    path = arm,
                    color = label.copy(alpha = 0.9f),
                    style = Stroke(width = w * 0.022f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                drawLine(
                    color = label.copy(alpha = 0.9f),
                    start = Offset(w * 0.73f, w * 0.77f),
                    end = Offset(w * 0.705f, w * 0.805f),
                    strokeWidth = w * 0.045f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
