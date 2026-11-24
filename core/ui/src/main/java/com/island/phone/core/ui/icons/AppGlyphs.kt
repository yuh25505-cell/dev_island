package com.island.phone.core.ui.icons

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
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
                stroked(c) {
                    moveTo(39f, 47f)
                    lineTo(39f, 38f)
                    arcTo(Rect(39f, 27f, 61f, 49f), 180f, 180f, false)
                    lineTo(61f, 47f)
                }
                solid(c, roundPath(30f, 46f, 70f, 80f, 10f))
            }
            AppId.CHAT -> {
                val tail = Path().apply {
                    moveTo(30f, 58f)
                    cubicTo(31f, 70f, 29f, 76f, 22f, 80f)
                    cubicTo(36f, 80f, 46f, 74f, 54f, 64f)
                    close()
                }
                solid(c, union(roundPath(20f, 24f, 80f, 66f, 18f), tail))
            }
            AppId.APPEARANCE -> {
                ring(c, 41f, 43f, 19f)
                ring(c, 59f, 57f, 19f)
            }
            AppId.GROWTH -> {
                line(c, 50f, 82f, 50f, 56f)
                solid(c, Path().apply {
                    moveTo(50f, 58f)
                    cubicTo(50f, 44f, 60f, 33f, 78f, 32f)
                    cubicTo(79f, 48f, 68f, 59f, 50f, 58f)
                    close()
                })
                solid(c, Path().apply {
                    moveTo(50f, 68f)
                    cubicTo(50f, 56f, 40f, 47f, 24f, 46f)
                    cubicTo(23f, 60f, 34f, 69f, 50f, 68f)
                    close()
                })
            }
            AppId.ALBUM -> {
                var petals = circlePath(50f + 19f, 50f, 12f)
                for (i in 1 until 8) {
                    val a = i * PI / 4
                    petals = Path.combine(
                        PathOperation.Union,
                        petals,
                        circlePath(50f + (19 * cos(a)).toFloat(), 50f + (19 * sin(a)).toFloat(), 12f)
                    )
                }
                val flower = Path.combine(PathOperation.Difference, petals, circlePath(50f, 50f, 6.5f))
                drawPath(flower, c)
            }
            AppId.CALENDAR -> {
                box(c, 22f, 28f, 78f, 80f, 13f)
                line(c, 24f, 46f, 76f, 46f)
                line(c, 37f, 20f, 37f, 34f)
                line(c, 63f, 20f, 63f, 34f)
                disc(c, 38f, 63f, 3.6f)
                disc(c, 50f, 63f, 3.6f)
                disc(c, 62f, 63f, 3.6f)
            }
            AppId.MEMO -> {
                box(c, 28f, 20f, 72f, 80f, 11f)
                line(c, 39f, 39f, 61f, 39f)
                line(c, 39f, 51f, 61f, 51f)
                line(c, 39f, 63f, 53f, 63f)
            }
            AppId.WEATHER -> {
                disc(c.copy(alpha = c.alpha * 0.7f), 32f, 34f, 9f)
                val cloud = union(
                    roundPath(24f, 58f, 84f, 78f, 10f),
                    circlePath(40f, 61f, 14f),
                    circlePath(58f, 52f, 18f),
                    circlePath(72f, 64f, 12f)
                )
                drawPath(cloud, c)
            }
            AppId.CLOCK -> {
                ring(c, 50f, 50f, 29f)
                stroked(c) {
                    moveTo(50f, 32f)
                    lineTo(50f, 50f)
                    lineTo(63f, 58f)
                }
            }
            AppId.SETTINGS -> {
                ring(c, 50f, 50f, 14f)
                for (i in 0 until 8) {
                    val a = i * PI / 4
                    line(
                        c,
                        50f + (24 * cos(a)).toFloat(), 50f + (24 * sin(a)).toFloat(),
                        50f + (33 * cos(a)).toFloat(), 50f + (33 * sin(a)).toFloat()
                    )
                }
            }
            AppId.PHONE -> {
                stroked(c, 12f) {
                    moveTo(32f, 30f)
                    cubicTo(30f, 54f, 46f, 70f, 70f, 68f)
                }
                disc(c, 32f, 30f, 9f)
                disc(c, 70f, 68f, 9f)
            }
            AppId.COUNTDOWN -> {
                solid(c, Path().apply {
                    moveTo(34f, 30f)
                    lineTo(66f, 30f)
                    cubicTo(66f, 42f, 56f, 46f, 54f, 50f)
                    cubicTo(56f, 54f, 66f, 58f, 66f, 70f)
                    lineTo(34f, 70f)
                    cubicTo(34f, 58f, 44f, 54f, 46f, 50f)
                    cubicTo(44f, 46f, 34f, 42f, 34f, 30f)
                    close()
                })
                line(c, 30f, 22f, 70f, 22f)
                line(c, 30f, 78f, 70f, 78f)
            }
            AppId.WORLDBOOK -> {
                box(c, 22f, 28f, 78f, 72f, 11f)
                line(c, 50f, 30f, 50f, 70f)
            }
            AppId.MUSIC -> {
                line(c, 55f, 66f, 55f, 28f)
                disc(c, 44f, 68f, 11f)
                solid(c, Path().apply {
                    moveTo(55f, 26f)
                    cubicTo(60f, 36f, 72f, 38f, 73f, 54f)
                    cubicTo(66f, 46f, 60f, 44f, 55f, 44f)
                    close()
                })
            }
            AppId.MEET -> {
                stroked(c) {
                    moveTo(50f, 80f)
                    cubicTo(42f, 72f, 28f, 58f, 28f, 43f)
                    arcTo(Rect(28f, 21f, 72f, 65f), 180f, 180f, false)
                    cubicTo(72f, 58f, 58f, 72f, 50f, 80f)
                    close()
                }
                ring(c, 50f, 43f, 8f)
            }
            AppId.LINK -> {
                ring(c, 38f, 50f, 18f)
                ring(c, 62f, 50f, 18f)
            }
            AppId.FORUM -> {
                line(c, 44f, 24f, 38f, 76f)
                line(c, 64f, 24f, 58f, 76f)
                line(c, 24f, 39f, 76f, 39f)
                line(c, 24f, 61f, 76f, 61f)
            }
            AppId.NOVEL -> {
                box(c, 28f, 20f, 72f, 80f, 11f)
                line(c, 40f, 22f, 40f, 78f)
                line(c, 51f, 38f, 62f, 38f)
                line(c, 51f, 50f, 62f, 50f)
            }
            AppId.COUPLE -> {
                solid(c, Path().apply {
                    moveTo(50f, 82f)
                    cubicTo(24f, 64f, 16f, 48f, 16f, 36f)
                    cubicTo(16f, 25f, 24f, 18f, 33f, 18f)
                    cubicTo(41f, 18f, 47f, 22f, 50f, 29f)
                    cubicTo(53f, 22f, 59f, 18f, 67f, 18f)
                    cubicTo(76f, 18f, 84f, 25f, 84f, 36f)
                    cubicTo(84f, 48f, 76f, 64f, 50f, 82f)
                    close()
                }, 4f)
            }
            AppId.PRESET -> {
                line(c, 24f, 32f, 76f, 32f)
                line(c, 24f, 50f, 76f, 50f)
                line(c, 24f, 68f, 76f, 68f)
                disc(c, 40f, 32f, 9f)
                disc(c, 60f, 50f, 9f)
                disc(c, 46f, 68f, 9f)
            }
            AppId.SIDELINE -> {
                box(c, 20f, 28f, 80f, 72f, 11f)
                stroked(c) {
                    moveTo(27f, 37f)
                    lineTo(50f, 55f)
                    lineTo(73f, 37f)
                }
            }
            AppId.SHOP -> {
                box(c, 26f, 38f, 74f, 80f, 11f)
                stroked(c) {
                    moveTo(39f, 40f)
                    lineTo(39f, 32f)
                    arcTo(Rect(39f, 21f, 61f, 43f), 180f, 180f, false)
                    lineTo(61f, 40f)
                }
            }
            AppId.DREAM -> {
                val moon = Path.combine(
                    PathOperation.Difference,
                    circlePath(46f, 52f, 26f),
                    circlePath(60f, 42f, 21f)
                )
                solid(c, moon)
                disc(c, 73f, 27f, 4f)
            }
        }
    }
}

private const val LINE_W = 7f

private fun pen(w: Float = LINE_W) = Stroke(width = w, cap = StrokeCap.Round, join = StrokeJoin.Round)

private fun circlePath(x: Float, y: Float, r: Float) =
    Path().apply { addOval(Rect(Offset(x, y), r)) }

private fun roundPath(l: Float, t: Float, r: Float, b: Float, rad: Float) =
    Path().apply { addRoundRect(RoundRect(l, t, r, b, rad, rad)) }

private fun union(vararg parts: Path): Path {
    var out = parts[0]
    for (i in 1 until parts.size) {
        out = Path.combine(PathOperation.Union, out, parts[i])
    }
    return out
}

private fun DrawScope.line(c: Color, x1: Float, y1: Float, x2: Float, y2: Float, w: Float = LINE_W) =
    drawLine(c, Offset(x1, y1), Offset(x2, y2), strokeWidth = w, cap = StrokeCap.Round)

private fun DrawScope.ring(c: Color, x: Float, y: Float, r: Float, w: Float = LINE_W) =
    drawCircle(c, r, Offset(x, y), style = pen(w))

private fun DrawScope.disc(c: Color, x: Float, y: Float, r: Float) =
    drawCircle(c, r, Offset(x, y))

private fun DrawScope.box(
    c: Color, l: Float, t: Float, r: Float, b: Float, rad: Float,
) = drawRoundRect(c, Offset(l, t), Size(r - l, b - t), CornerRadius(rad), style = pen())

private fun DrawScope.stroked(c: Color, w: Float = LINE_W, build: Path.() -> Unit) =
    drawPath(Path().apply(build), c, style = pen(w))

private fun DrawScope.solid(c: Color, path: Path, edge: Float = 3f) {
    drawPath(path, c)
    drawPath(path, c, style = pen(edge))
}
