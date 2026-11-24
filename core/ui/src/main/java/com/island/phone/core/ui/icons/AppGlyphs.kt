package com.island.phone.core.ui.icons

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import com.island.phone.core.ui.apps.AppId
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun DrawScope.drawAppGlyph(app: AppId, color: Color) {
    scale(scale = size.minDimension / 100f, pivot = Offset.Zero) {
        val c = color
        when (app) {
            AppId.AUTH -> {
                box(c, 32f, 48f, 68f, 78f, 8f, false)
                drawArc(c, 180f, 180f, false, Offset(38f, 26f), Size(24f, 30f), style = pen())
                line(c, 38f, 41f, 38f, 48f)
                line(c, 62f, 41f, 62f, 48f)
            }
            AppId.CHAT -> {
                box(c, 22f, 26f, 78f, 64f, 16f, false)
                shape(c, false) { moveTo(34f, 60f); lineTo(32f, 78f); lineTo(52f, 62f); close() }
            }
            AppId.APPEARANCE -> {
                ring(c, 42f, 44f, 18f)
                ring(c, 58f, 56f, 18f)
            }
            AppId.GROWTH -> {
                line(c, 50f, 78f, 50f, 48f, 5f)
                shape(c, false) {
                    moveTo(50f, 56f); cubicTo(50f, 40f, 62f, 32f, 74f, 32f)
                    cubicTo(74f, 46f, 66f, 56f, 50f, 56f); close()
                }
                shape(c, false) {
                    moveTo(50f, 66f); cubicTo(50f, 54f, 40f, 46f, 28f, 46f)
                    cubicTo(28f, 58f, 36f, 66f, 50f, 66f); close()
                }
            }
            AppId.ALBUM -> {
                val petal = c.copy(alpha = c.alpha * 0.8f)
                for (i in 0 until 8) {
                    val a = i * PI / 4
                    disc(petal, 50f + (17 * cos(a)).toFloat(), 50f + (17 * sin(a)).toFloat(), 11f)
                }
            }
            AppId.CALENDAR -> {
                box(c, 24f, 28f, 76f, 76f, 10f, true)
                line(c, 24f, 44f, 76f, 44f)
                line(c, 38f, 20f, 38f, 32f)
                line(c, 62f, 20f, 62f, 32f)
                disc(c, 38f, 60f, 3.5f)
                disc(c, 50f, 60f, 3.5f)
                disc(c, 62f, 60f, 3.5f)
            }
            AppId.MEMO -> {
                box(c, 30f, 22f, 70f, 78f, 8f, true)
                line(c, 38f, 40f, 62f, 40f)
                line(c, 38f, 52f, 62f, 52f)
                line(c, 38f, 64f, 54f, 64f)
            }
            AppId.WEATHER -> {
                disc(c.copy(alpha = c.alpha * 0.7f), 36f, 38f, 10f)
                disc(c, 40f, 62f, 13f)
                disc(c, 57f, 54f, 17f)
                disc(c, 71f, 64f, 11f)
                box(c, 32f, 62f, 78f, 75f, 6f, false)
            }
            AppId.CLOCK -> {
                ring(c, 50f, 50f, 27f)
                line(c, 50f, 50f, 50f, 33f)
                line(c, 50f, 50f, 62f, 57f)
            }
            AppId.SETTINGS -> {
                ring(c, 50f, 50f, 15f)
                for (i in 0 until 8) {
                    val a = i * PI / 4
                    line(
                        c,
                        50f + (22 * cos(a)).toFloat(), 50f + (22 * sin(a)).toFloat(),
                        50f + (31 * cos(a)).toFloat(), 50f + (31 * sin(a)).toFloat(),
                        8f
                    )
                }
            }
            AppId.PHONE -> {
                shape(c, true, 13f) { moveTo(32f, 30f); cubicTo(30f, 52f, 48f, 70f, 70f, 68f) }
            }
            AppId.COUNTDOWN -> {
                shape(c, false) { moveTo(34f, 26f); lineTo(66f, 26f); lineTo(50f, 50f); close() }
                shape(c, false) { moveTo(50f, 50f); lineTo(66f, 74f); lineTo(34f, 74f); close() }
                line(c, 30f, 24f, 70f, 24f)
                line(c, 30f, 76f, 70f, 76f)
            }
            AppId.WORLDBOOK -> {
                box(c, 26f, 30f, 74f, 72f, 6f, true)
                line(c, 50f, 32f, 50f, 72f)
            }
            AppId.MUSIC -> {
                line(c, 58f, 28f, 58f, 64f)
                disc(c, 48f, 66f, 10f)
                shape(c, true) { moveTo(58f, 28f); cubicTo(66f, 32f, 72f, 38f, 72f, 50f) }
            }
            AppId.MEET -> {
                shape(c, true) {
                    moveTo(50f, 78f)
                    cubicTo(50f, 78f, 30f, 58f, 30f, 44f)
                    arcTo(Rect(30f, 24f, 70f, 64f), 180f, 180f, false)
                    cubicTo(70f, 58f, 50f, 78f, 50f, 78f)
                }
                ring(c, 50f, 44f, 8f)
            }
            AppId.LINK -> {
                ring(c, 40f, 50f, 17f)
                ring(c, 60f, 50f, 17f)
            }
            AppId.FORUM -> {
                line(c, 42f, 26f, 38f, 74f)
                line(c, 62f, 26f, 58f, 74f)
                line(c, 28f, 40f, 74f, 40f)
                line(c, 26f, 60f, 72f, 60f)
            }
            AppId.NOVEL -> {
                box(c, 30f, 24f, 70f, 76f, 6f, true)
                line(c, 40f, 24f, 40f, 76f)
                line(c, 50f, 38f, 60f, 38f)
                line(c, 50f, 50f, 60f, 50f)
            }
            AppId.COUPLE -> {
                shape(c, false) {
                    moveTo(50f, 76f)
                    cubicTo(18f, 54f, 26f, 26f, 50f, 42f)
                    cubicTo(74f, 26f, 82f, 54f, 50f, 76f)
                    close()
                }
            }
            AppId.PRESET -> {
                line(c, 28f, 34f, 72f, 34f)
                line(c, 28f, 50f, 72f, 50f)
                line(c, 28f, 66f, 72f, 66f)
                disc(c, 40f, 34f, 8f)
                disc(c, 60f, 50f, 8f)
                disc(c, 46f, 66f, 8f)
            }
            AppId.SIDELINE -> {
                box(c, 24f, 32f, 76f, 68f, 8f, true)
                shape(c, true) { moveTo(26f, 36f); lineTo(50f, 54f); lineTo(74f, 36f) }
            }
            AppId.SHOP -> {
                box(c, 30f, 40f, 70f, 76f, 8f, true)
                drawArc(c, 180f, 180f, false, Offset(40f, 26f), Size(20f, 28f), style = pen())
            }
            AppId.DREAM -> {
                val moon = Path.combine(
                    PathOperation.Difference,
                    circlePath(48f, 50f, 24f),
                    circlePath(60f, 42f, 20f)
                )
                drawPath(moon, c)
                disc(c, 70f, 30f, 3.5f)
            }
        }
    }
}

private fun pen(w: Float = 6f) = Stroke(width = w, cap = StrokeCap.Round, join = StrokeJoin.Round)

private fun circlePath(x: Float, y: Float, r: Float) =
    Path().apply { addOval(Rect(Offset(x, y), r)) }

private fun DrawScope.line(c: Color, x1: Float, y1: Float, x2: Float, y2: Float, w: Float = 6f) =
    drawLine(c, Offset(x1, y1), Offset(x2, y2), strokeWidth = w, cap = StrokeCap.Round)

private fun DrawScope.ring(c: Color, x: Float, y: Float, r: Float, w: Float = 6f) =
    drawCircle(c, r, Offset(x, y), style = pen(w))

private fun DrawScope.disc(c: Color, x: Float, y: Float, r: Float) =
    drawCircle(c, r, Offset(x, y))

private fun DrawScope.box(
    c: Color, l: Float, t: Float, r: Float, b: Float, rad: Float, stroke: Boolean,
) = drawRoundRect(
    c, Offset(l, t), Size(r - l, b - t), CornerRadius(rad),
    style = if (stroke) pen() else Fill
)

private fun DrawScope.shape(c: Color, stroke: Boolean, w: Float = 6f, build: Path.() -> Unit) =
    drawPath(Path().apply(build), c, style = if (stroke) pen(w) else Fill)
