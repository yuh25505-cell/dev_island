package com.islandphone.app.core.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val FROND_ANGLES = listOf(-168f, -135f, -108f, -72f, -45f, -12f)

internal fun DrawScope.drawSplashIsland(
    p: SplashPalette,
    t: Float,
    w: Float,
    h: Float,
    sec: Float
) {
    val reveal = splashSmooth(t / 0.18f)
    if (reveal <= 0f) return
    val cx = w * ISLAND_X
    val yb = h * ISLAND_Y + (1f - reveal) * h * 0.012f

    withTransform({ scale(1f, -0.5f, Offset(cx, yb)) }) {
        drawIslandBody(p, cx, yb, w, sec, reveal * 0.16f, false)
    }
    drawIslandBody(p, cx, yb, w, sec, reveal, true)

    val glow = splashSmooth((t - 0.50f) / 0.08f) * (0.7f + 0.3f * sin(sec * 2f)) *
        (if (p.isDark) 0.55f else 0.32f)
    if (glow > 0f) {
        val gx = cx + 0.03f * w
        val gy = yb - 0.17f * w
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(p.lantern.mul(glow), Color.Transparent),
                center = Offset(gx, gy),
                radius = 0.17f * w
            ),
            radius = 0.17f * w,
            center = Offset(gx, gy)
        )
    }
}

private fun DrawScope.drawIslandBody(
    p: SplashPalette,
    cx: Float,
    yb: Float,
    u: Float,
    sec: Float,
    a: Float,
    withTree: Boolean
) {
    val lens = Path()
    lens.moveTo(cx - 0.25f * u, yb)
    lens.cubicTo(cx - 0.16f * u, yb - 0.03f * u, cx - 0.08f * u, yb - 0.04f * u, cx, yb - 0.04f * u)
    lens.cubicTo(cx + 0.08f * u, yb - 0.04f * u, cx + 0.16f * u, yb - 0.03f * u, cx + 0.25f * u, yb)
    lens.cubicTo(cx + 0.12f * u, yb + 0.026f * u, cx - 0.12f * u, yb + 0.026f * u, cx - 0.25f * u, yb)
    lens.close()
    drawPath(path = lens, color = p.sandShade.mul(a))

    val beach = Path()
    beach.moveTo(cx - 0.25f * u, yb)
    beach.cubicTo(cx - 0.16f * u, yb - 0.03f * u, cx - 0.08f * u, yb - 0.04f * u, cx, yb - 0.04f * u)
    beach.cubicTo(cx + 0.08f * u, yb - 0.04f * u, cx + 0.16f * u, yb - 0.03f * u, cx + 0.25f * u, yb)
    beach.cubicTo(cx + 0.12f * u, yb + 0.008f * u, cx - 0.12f * u, yb + 0.008f * u, cx - 0.25f * u, yb)
    beach.close()
    drawPath(path = beach, color = p.sand.mul(a))

    val hill = Path()
    hill.moveTo(cx - 0.17f * u, yb - 0.022f * u)
    hill.cubicTo(cx - 0.11f * u, yb - 0.07f * u, cx - 0.06f * u, yb - 0.088f * u, cx, yb - 0.088f * u)
    hill.cubicTo(cx + 0.06f * u, yb - 0.088f * u, cx + 0.11f * u, yb - 0.07f * u, cx + 0.17f * u, yb - 0.022f * u)
    hill.cubicTo(cx + 0.08f * u, yb - 0.026f * u, cx - 0.08f * u, yb - 0.026f * u, cx - 0.17f * u, yb - 0.022f * u)
    hill.close()
    drawPath(path = hill, color = p.grass.mul(a))

    val shade = Path()
    shade.moveTo(cx + 0.01f * u, yb - 0.087f * u)
    shade.cubicTo(cx + 0.07f * u, yb - 0.08f * u, cx + 0.12f * u, yb - 0.06f * u, cx + 0.17f * u, yb - 0.022f * u)
    shade.lineTo(cx + 0.02f * u, yb - 0.0235f * u)
    shade.close()
    drawPath(path = shade, color = p.grassDark.mul(a * 0.55f))

    if (!withTree) return

    val tx = cx + 0.025f * u
    val gy = yb - 0.084f * u
    val topX = tx + 0.006f * u
    val topY = gy - 0.105f * u

    val trunk = Path()
    trunk.moveTo(tx, gy)
    trunk.cubicTo(tx + 0.012f * u, gy - 0.035f * u, tx - 0.004f * u, gy - 0.07f * u, topX, topY)
    drawPath(
        path = trunk,
        color = p.trunk.mul(a),
        style = Stroke(width = 0.009f * u, cap = StrokeCap.Round)
    )

    FROND_ANGLES.forEachIndexed { i, base ->
        val rad = (base + 4f * sin(sec * 1.3f + i)) * PI.toFloat() / 180f
        val dx = cos(rad) * 0.07f * u
        val dy = sin(rad) * 0.07f * u
        val frond = Path()
        frond.moveTo(topX, topY)
        frond.cubicTo(
            topX + dx * 0.4f, topY + dy * 0.4f - 0.02f * u,
            topX + dx * 0.8f, topY + dy * 0.8f - 0.005f * u,
            topX + dx, topY + dy + 0.025f * u
        )
        drawPath(
            path = frond,
            color = p.leaf.mul(a),
            style = Stroke(width = 0.012f * u, cap = StrokeCap.Round)
        )
    }
}
