package com.islandphone.app.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.islandphone.app.core.theme.IslandTheme
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sin

private val STARS = listOf(
    0.08f to 0.10f, 0.18f to 0.34f, 0.31f to 0.08f, 0.44f to 0.26f,
    0.58f to 0.12f, 0.67f to 0.36f, 0.80f to 0.07f, 0.92f to 0.24f,
    0.51f to 0.44f, 0.12f to 0.54f, 0.87f to 0.50f, 0.37f to 0.52f
)

private class Cloud(val base: Float, val y: Float, val size: Float, val speed: Float)

private val CLOUDS = listOf(
    Cloud(0.10f, 0.11f, 0.10f, 0.012f),
    Cloud(0.55f, 0.25f, 0.13f, 0.008f),
    Cloud(0.85f, 0.07f, 0.08f, 0.015f)
)

private class Streak(val x: Float, val y: Float, val len: Float, val speed: Float)

private val STREAKS = listOf(
    Streak(0.05f, 0.12f, 0.10f, 0.8f), Streak(0.40f, 0.20f, 0.14f, 1.0f),
    Streak(0.75f, 0.10f, 0.09f, 0.9f), Streak(0.20f, 0.34f, 0.16f, 1.2f),
    Streak(0.62f, 0.38f, 0.12f, 1.1f), Streak(0.90f, 0.30f, 0.15f, 1.3f),
    Streak(0.10f, 0.52f, 0.18f, 1.4f), Streak(0.48f, 0.56f, 0.13f, 1.2f),
    Streak(0.80f, 0.60f, 0.17f, 1.5f), Streak(0.30f, 0.72f, 0.20f, 1.6f),
    Streak(0.66f, 0.76f, 0.15f, 1.4f), Streak(0.02f, 0.84f, 0.18f, 1.7f),
    Streak(0.52f, 0.90f, 0.22f, 1.8f), Streak(0.85f, 0.94f, 0.16f, 1.6f)
)

@Composable
internal fun SplashScene(time: SplashTime, modifier: Modifier = Modifier) {
    val colors = IslandTheme.colors
    val p = remember(colors.isDark) { splashPalette(colors.isDark) }
    Canvas(modifier = modifier.graphicsLayer { alpha = splashSmooth(time.value / 0.06f) }) {
        val t = time.value
        val w = size.width
        val h = size.height
        val sec = t * SPLASH_TOTAL_MS / 1000f
        val horizon = h * 0.50f

        drawSky(p, t, w, h, horizon, sec)
        drawSea(p, w, h, horizon)
        drawWaveBands(p, w, h, horizon, sec, 0, 2)
        drawSplashIsland(p, t, w, h, sec)
        drawWaveBands(p, w, h, horizon, sec, 2, 5)
        drawFlow(p, w, h, horizon, sec)
        drawSplashBoat(p, t, w, h, sec)

        val mistA = 0.22f + 0.55f * (1f - splashSmooth(t / 0.3f))
        val mistTop = horizon - h * 0.10f
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, p.mist.mul(mistA), Color.Transparent),
                startY = mistTop,
                endY = horizon + h * 0.10f
            ),
            topLeft = Offset(0f, mistTop),
            size = Size(w, h * 0.20f)
        )
    }
}

private fun DrawScope.drawSky(
    p: SplashPalette,
    t: Float,
    w: Float,
    h: Float,
    horizon: Float,
    sec: Float
) {
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(p.skyTop, p.skyBottom),
            startY = 0f,
            endY = horizon
        ),
        size = Size(w, horizon)
    )

    val rise = 1f - splashSmooth(t / 0.2f)
    val cx = w * 0.24f
    val cy = h * 0.17f + rise * h * 0.02f
    val r = w * 0.075f
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(p.sunGlow.mul(0.55f), Color.Transparent),
            center = Offset(cx, cy),
            radius = r * 3.4f
        ),
        radius = r * 3.4f,
        center = Offset(cx, cy)
    )
    drawCircle(color = p.sun, radius = r, center = Offset(cx, cy))

    if (p.isDark) {
        STARS.forEachIndexed { i, s ->
            val twinkle = 0.5f + 0.5f * sin(sec * 1.6f + i * 1.7f)
            drawCircle(
                color = p.sun.mul(0.25f + 0.5f * twinkle),
                radius = (0.9f + (i % 3) * 0.4f).dp.toPx(),
                center = Offset(w * s.first, horizon * s.second)
            )
        }
    }

    val cloudColor = p.cloud.mul(if (p.isDark) 0.14f else 0.8f)
    CLOUDS.forEach { c ->
        val x = ((c.base + sec * c.speed) % 1.4f - 0.2f) * w
        drawCloud(cloudColor, x, h * c.y, w * c.size)
    }
}

private fun DrawScope.drawCloud(color: Color, cx: Float, cy: Float, s: Float) {
    drawRoundRect(
        color = color,
        topLeft = Offset(cx - s, cy),
        size = Size(2f * s, 0.34f * s),
        cornerRadius = CornerRadius(0.17f * s, 0.17f * s)
    )
    drawCircle(color = color, radius = 0.28f * s, center = Offset(cx - 0.35f * s, cy))
    drawCircle(color = color, radius = 0.36f * s, center = Offset(cx + 0.10f * s, cy - 0.08f * s))
}

private fun DrawScope.drawSea(p: SplashPalette, w: Float, h: Float, horizon: Float) {
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(p.waterTop, p.waterBottom),
            startY = horizon,
            endY = h
        ),
        topLeft = Offset(0f, horizon),
        size = Size(w, h - horizon)
    )
}

private fun DrawScope.drawWaveBands(
    p: SplashPalette,
    w: Float,
    h: Float,
    horizon: Float,
    sec: Float,
    from: Int,
    to: Int
) {
    val depth = h - horizon
    for (i in from until to) {
        val baseY = horizon + depth * (0.07f + i * 0.2f)
        val amp = h * (0.004f + i * 0.0022f)
        val k = 2f * PI.toFloat() * (1.4f + i * 0.45f) / w
        val speed = 0.9f + i * 0.3f
        val crest = Path()
        crest.moveTo(0f, baseY + amp * sin(-speed * sec + i))
        var x = 0f
        while (x < w) {
            x = min(w, x + 18f)
            crest.lineTo(x, baseY + amp * sin(k * x - speed * sec + i))
        }
        val fill = Path()
        fill.addPath(crest)
        fill.lineTo(w, h)
        fill.lineTo(0f, h)
        fill.close()
        drawPath(
            path = fill,
            color = if (i % 2 == 0) p.waveLight.mul(0.10f) else p.waveDeep.mul(0.08f)
        )
        drawPath(
            path = crest,
            color = p.waveLight.mul(0.22f),
            style = Stroke(width = 1.2.dp.toPx())
        )
    }
}

private fun DrawScope.drawFlow(p: SplashPalette, w: Float, h: Float, horizon: Float, sec: Float) {
    val depth = h - horizon
    val bend = 3.dp.toPx()
    STREAKS.forEachIndexed { i, s ->
        val y = horizon + depth * s.y
        val travel = s.speed * (0.5f + s.y) * 0.07f
        val x = ((s.x + sec * travel) % 1.3f - 0.15f) * w
        val len = w * s.len
        val lift = bend * sin(sec * 1.5f + i)
        val path = Path()
        path.moveTo(x, y)
        path.cubicTo(x + len * 0.33f, y - lift, x + len * 0.66f, y + lift, x + len, y)
        drawPath(
            path = path,
            color = p.flow.mul(0.26f),
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
