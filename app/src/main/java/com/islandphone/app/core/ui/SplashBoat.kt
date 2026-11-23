package com.islandphone.app.core.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sin

internal fun DrawScope.drawSplashBoat(
    p: SplashPalette,
    t: Float,
    w: Float,
    h: Float,
    sec: Float
) {
    val s = w * BOAT_SIZE
    val q = splashWindow(t, BOAT_START, BOAT_END)
    val progress = 1f - (1f - q).pow(2.2f)
    val x = splashLerp(-0.2f * w, BOAT_STOP_X * w, progress)
    val bob = h * 0.004f * sin(sec * 2.2f)
    val y = h * BOAT_LANE_Y + splashLaneWave(x, w, h, sec) + bob
    val slope = (splashLaneWave(x + 6f, w, h, sec) - splashLaneWave(x - 6f, w, h, sec)) / 12f
    val degrees = atan(slope) * 57.2958f + 2.5f * sin(sec * 1.7f)
    val wake = if (q < 1f) (1f - q).pow(0.5f) else 0f

    for (k in 0..3) {
        val a = 0.35f * wake * (1f - k * 0.22f)
        if (a <= 0f) continue
        val rx = s * (0.25f + 0.08f * k)
        drawOval(
            color = p.flow.mul(a),
            topLeft = Offset(x - s * (0.5f + 0.35f * k) - rx, y - 0.02f * s),
            size = Size(rx * 2f, s * 0.09f),
            style = Stroke(width = 1.4.dp.toPx())
        )
    }

    drawOval(
        color = p.waveDeep.mul(0.22f),
        topLeft = Offset(x - 0.40f * s, y - 0.02f * s),
        size = Size(0.80f * s, 0.08f * s)
    )

    drawPaperBoat(Offset(x, y), s, degrees, p.paperLight, p.paperMid, p.paperDark)

    drawOval(
        color = p.waveLight.mul(0.35f),
        topLeft = Offset(x - 0.42f * s, y - 0.03f * s),
        size = Size(0.84f * s, 0.07f * s)
    )

    val arrive = splashWindow(t, BOAT_END - 0.02f, BOAT_END + 0.22f)
    for (r in 0..2) {
        val ph = arrive * 1.6f - r * 0.35f
        if (ph <= 0f || ph >= 1f) continue
        val rx = splashLerp(0.3f * s, 1.3f * s, ph)
        drawOval(
            color = p.flow.mul((1f - ph) * 0.4f),
            topLeft = Offset(x - rx, y - rx * 0.09f),
            size = Size(rx * 2f, rx * 0.18f),
            style = Stroke(width = 1.4.dp.toPx())
        )
    }
}

internal fun DrawScope.drawPaperBoat(
    origin: Offset,
    s: Float,
    degrees: Float,
    light: Color,
    mid: Color,
    dark: Color
) {
    withTransform({
        translate(origin.x, origin.y)
        rotate(degrees, Offset.Zero)
    }) {
        val sailLeft = Path()
        sailLeft.moveTo(-0.30f * s, -0.24f * s)
        sailLeft.lineTo(0f, -0.74f * s)
        sailLeft.lineTo(0f, -0.24f * s)
        sailLeft.close()
        drawPath(path = sailLeft, color = light)

        val sailRight = Path()
        sailRight.moveTo(0f, -0.74f * s)
        sailRight.lineTo(0.30f * s, -0.24f * s)
        sailRight.lineTo(0f, -0.24f * s)
        sailRight.close()
        drawPath(path = sailRight, color = mid)

        val hullLeft = Path()
        hullLeft.moveTo(-0.56f * s, -0.26f * s)
        hullLeft.lineTo(0f, -0.26f * s)
        hullLeft.lineTo(0f, 0f)
        hullLeft.lineTo(-0.36f * s, 0f)
        hullLeft.close()
        drawPath(path = hullLeft, color = mid)

        val hullRight = Path()
        hullRight.moveTo(0f, -0.26f * s)
        hullRight.lineTo(0.56f * s, -0.26f * s)
        hullRight.lineTo(0.36f * s, 0f)
        hullRight.lineTo(0f, 0f)
        hullRight.close()
        drawPath(path = hullRight, color = light)

        val rim = Path()
        rim.moveTo(-0.56f * s, -0.26f * s)
        rim.lineTo(0.56f * s, -0.26f * s)
        rim.lineTo(0.52f * s, -0.21f * s)
        rim.lineTo(-0.52f * s, -0.21f * s)
        rim.close()
        drawPath(path = rim, color = dark)
    }
}
