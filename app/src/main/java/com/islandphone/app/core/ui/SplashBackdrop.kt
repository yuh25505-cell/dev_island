package com.islandphone.app.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme
import kotlin.math.PI
import kotlin.math.sin

internal typealias SplashTime = Animatable<Float, AnimationVector1D>

internal fun splashSmooth(x: Float): Float {
    val v = x.coerceIn(0f, 1f)
    return v * v * (3f - 2f * v)
}

private val DOTS = listOf(
    0.12f to 0.18f, 0.86f to 0.13f, 0.72f to 0.40f, 0.20f to 0.47f,
    0.90f to 0.58f, 0.34f to 0.70f, 0.78f to 0.84f, 0.06f to 0.90f
)

@Composable
internal fun SplashLines(time: SplashTime, modifier: Modifier = Modifier) {
    val colors = IslandTheme.colors
    val lineColor = colors.textSecondary.copy(alpha = 0.14f)
    val dotColor = colors.textSecondary.copy(alpha = 0.35f)
    Canvas(
        modifier = modifier.graphicsLayer { alpha = splashSmooth(time.value / 0.15f) }
    ) {
        val w = size.width
        val h = size.height
        val phase = time.value * 6f
        for (i in 0 until 7) {
            val base = h * (0.12f + i * 0.125f)
            val amp = h * (0.012f + (i % 3) * 0.006f)
            val freq = (1.2f + i * 0.35f) * (2f * PI.toFloat()) / w
            val dir = if (i % 2 == 0) 1f else -1f
            val path = Path()
            path.moveTo(0f, base + amp * sin(phase * dir + i))
            var x = 0f
            while (x < w) {
                x += 20f
                path.lineTo(x, base + amp * sin(x * freq + phase * dir + i))
            }
            drawPath(path = path, color = lineColor, style = Stroke(width = 1.dp.toPx()))
        }
        DOTS.forEachIndexed { index, d ->
            val dx = sin(phase * 0.7f + index) * 6.dp.toPx()
            val dy = sin(phase * 0.5f + index * 2f) * 6.dp.toPx()
            drawCircle(
                color = dotColor,
                radius = 1.6f.dp.toPx(),
                center = Offset(w * d.first + dx, h * d.second + dy)
            )
        }
    }
}

@Composable
internal fun SplashMarquee(
    text: String,
    yFraction: Float,
    direction: Float,
    time: SplashTime
) {
    val colors = IslandTheme.colors
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = maxHeight * yFraction)
                .clipToBounds()
        ) {
            Text(
                text = text,
                maxLines = 1,
                softWrap = false,
                fontSize = 22.sp,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.Medium,
                color = colors.textSecondary.copy(alpha = 0.16f),
                modifier = Modifier
                    .wrapContentWidth(Alignment.Start, unbounded = true)
                    .graphicsLayer {
                        val travel = 240.dp.toPx()
                        val startShift = if (direction > 0f) -travel else 0f
                        translationX = startShift + direction * time.value * travel
                        alpha = splashSmooth(time.value / 0.15f)
                    }
            )
        }
    }
}

private class LabelSpec(val xf: Float, val yf: Float, val rotation: Float)

private val LABEL_SPECS = listOf(
    LabelSpec(0.40f, 0.17f, 1.5f),
    LabelSpec(0.08f, 0.22f, -4f),
    LabelSpec(0.60f, 0.27f, 4f),
    LabelSpec(0.10f, 0.60f, 0f),
    LabelSpec(0.52f, 0.71f, -3f),
    LabelSpec(0.08f, 0.79f, 3f),
    LabelSpec(0.46f, 0.80f, -2f)
)

@Composable
internal fun SplashLabels(time: SplashTime) {
    val colors = IslandTheme.colors
    val labels = stringArrayResource(R.array.splash_labels)
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        labels.forEachIndexed { i, label ->
            val spec = LABEL_SPECS.getOrNull(i) ?: return@forEachIndexed
            Text(
                text = label,
                maxLines = 1,
                fontSize = 10.sp,
                letterSpacing = 1.4f.sp,
                fontWeight = FontWeight.Medium,
                color = colors.textSecondary,
                modifier = Modifier
                    .offset(x = maxWidth * spec.xf, y = maxHeight * spec.yf)
                    .graphicsLayer {
                        val a = splashSmooth((time.value - (0.05f + i * 0.035f)) / 0.12f)
                        alpha = a
                        translationY = (1f - a) * 8.dp.toPx()
                        rotationZ = spec.rotation
                    }
            )
        }
    }
}

private fun Modifier.vertical(rotateUp: Boolean): Modifier = layout { measurable, _ ->
    val p = measurable.measure(Constraints())
    layout(p.height, p.width) {
        p.placeWithLayer(
            x = -(p.width - p.height) / 2,
            y = (p.width - p.height) / 2
        ) { rotationZ = if (rotateUp) -90f else 90f }
    }
}

@Composable
internal fun SplashEdgeLabels(time: SplashTime) {
    val colors = IslandTheme.colors
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.splash_vert_left),
            maxLines = 1,
            softWrap = false,
            fontSize = 10.sp,
            letterSpacing = 1.4f.sp,
            fontWeight = FontWeight.Medium,
            color = colors.textSecondary,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 10.dp)
                .offset(y = (-20).dp)
                .vertical(rotateUp = true)
                .graphicsLayer { alpha = splashSmooth((time.value - 0.2f) / 0.12f) }
        )
        Text(
            text = stringResource(R.string.splash_vert_right),
            maxLines = 1,
            softWrap = false,
            fontSize = 10.sp,
            letterSpacing = 1.4f.sp,
            fontWeight = FontWeight.Medium,
            color = colors.textSecondary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
                .offset(y = (-20).dp)
                .vertical(rotateUp = false)
                .graphicsLayer { alpha = splashSmooth((time.value - 0.28f) / 0.12f) }
        )
    }
}
