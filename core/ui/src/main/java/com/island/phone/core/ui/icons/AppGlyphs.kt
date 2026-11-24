package com.island.phone.core.ui.icons

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import com.island.phone.core.ui.apps.AppId
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun DrawScope.drawAppGlyph(app: AppId, color: Color) {
    scale(scale = size.minDimension / 100f, pivot = Offset.Zero) {
        val c = color
        when (app) {
            AppId.AUTH -> {
                stroked(c, 8f) {
                    moveTo(38f, 48f)
                    lineTo(38f, 38f)
                    arcTo(Rect(38f, 26f, 62f, 50f), 180f, 180f, false)
                    lineTo(62f, 48f)
                }
                val body = diff(
                    roundPath(28f, 44f, 72f, 82f, 10f),
                    union(circlePath(50f, 59f, 5.5f), roundPath(47f, 60f, 53f, 72f, 3f))
                )
                fillPath(c, body)
            }
            AppId.CHAT -> {
                val tail = Path().apply {
                    moveTo(30f, 58f)
                    cubicTo(31f, 70f, 29f, 76f, 22f, 80f)
                    cubicTo(36f, 80f, 46f, 74f, 54f, 64f)
                    close()
                }
                val dots = union(
                    circlePath(36f, 45f, 4.5f),
                    circlePath(50f, 45f, 4.5f),
                    circlePath(64f, 45f, 4.5f)
                )
                fillPath(c, diff(union(roundPath(20f, 24f, 80f, 66f, 18f), tail), dots))
            }
            AppId.APPEARANCE -> {
                val both = Path.combine(
                    PathOperation.Xor,
                    circlePath(40f, 42f, 24f),
                    circlePath(60f, 58f, 24f)
                )
                fillPath(c, both)
            }
            AppId.GROWTH -> {
                fillPath(c, roundPath(46f, 54f, 54f, 84f, 4f))
                fillPath(c, Path().apply {
                    moveTo(50f, 58f)
                    cubicTo(50f, 44f, 60f, 33f, 80f, 30f)
                    cubicTo(82f, 48f, 70f, 61f, 50f, 58f)
                    close()
                })
                fillPath(c, Path().apply {
                    moveTo(50f, 68f)
                    cubicTo(50f, 56f, 40f, 47f, 22f, 44f)
                    cubicTo(20f, 61f, 32f, 71f, 50f, 68f)
                    close()
                })
            }
            AppId.ALBUM -> {
                var petals = circlePath(69f, 50f, 12f)
                for (i in 1 until 8) {
                    val a = i * PI / 4
                    petals = Path.combine(
                        PathOperation.Union,
                        petals,
                        circlePath(50f + (19 * cos(a)).toFloat(), 50f + (19 * sin(a)).toFloat(), 12f)
                    )
                }
                fillPath(c, diff(petals, circlePath(50f, 50f, 6.5f)))
            }
            AppId.CALENDAR -> {
                fillPath(c, diff(roundPath(20f, 26f, 80f, 82f, 13f), roundPath(29f, 48f, 71f, 74f, 5f)))
                fillPath(c, roundPath(31f, 17f, 39f, 33f, 4f))
                fillPath(c, roundPath(61f, 17f, 69f, 33f, 4f))
                disc(c, 39f, 61f, 4f)
                disc(c, 50f, 61f, 4f)
                disc(c, 61f, 61f, 4f)
            }
            AppId.MEMO -> {
                val lines = union(
                    roundPath(37f, 35f, 63f, 41f, 3f),
                    roundPath(37f, 48f, 63f, 54f, 3f),
                    roundPath(37f, 61f, 53f, 67f, 3f)
                )
                fillPath(c, diff(roundPath(26f, 18f, 74f, 82f, 11f), lines))
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
                val hands = union(
                    roundPath(46.5f, 28f, 53.5f, 53f, 3.5f),
                    roundPath(46.5f, 46.5f, 68f, 53.5f, 3.5f)
                )
                fillPath(c, diff(circlePath(50f, 50f, 32f), hands))
            }
            AppId.SETTINGS -> {
                val gear = Path().apply {
                    for (i in 0 until 8) {
                        val a = i * 45.0 - 90.0
                        val pts = listOf(
                            polar(27f, a - 12.0),
                            polar(35f, a - 8.0),
                            polar(35f, a + 8.0),
                            polar(27f, a + 12.0),
                            polar(27f, a + 22.5)
                        )
                        for ((k, p) in pts.withIndex()) {
                            if (i == 0 && k == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
                        }
                    }
                    close()
                }
                fillPath(c, diff(gear, circlePath(50f, 50f, 11f)), 2.5f)
            }
            AppId.PHONE -> {
                val handset = Path().apply {
                    moveTo(6.62f, 10.79f)
                    relativeCubicTo(1.44f, 2.83f, 3.76f, 5.14f, 6.59f, 6.59f)
                    relativeLineTo(2.2f, -2.2f)
                    relativeCubicTo(0.27f, -0.27f, 0.67f, -0.36f, 1.02f, -0.24f)
                    relativeCubicTo(1.12f, 0.37f, 2.33f, 0.57f, 3.57f, 0.57f)
                    relativeCubicTo(0.55f, 0f, 1f, 0.45f, 1f, 1f)
                    lineTo(21f, 20f)
                    relativeCubicTo(0f, 0.55f, -0.45f, 1f, -1f, 1f)
                    relativeCubicTo(-9.39f, 0f, -17f, -7.61f, -17f, -17f)
                    relativeCubicTo(0f, -0.55f, 0.45f, -1f, 1f, -1f)
                    relativeLineTo(3.5f, 0f)
                    relativeCubicTo(0.55f, 0f, 1f, 0.45f, 1f, 1f)
                    relativeCubicTo(0f, 1.25f, 0.2f, 2.45f, 0.57f, 3.57f)
                    relativeCubicTo(0.11f, 0.35f, 0.03f, 0.74f, -0.25f, 1.02f)
                    relativeLineTo(-2.2f, 2.2f)
                    close()
                }
                translate(left = 6.8f, top = 6.8f) {
                    scale(scale = 3.6f, pivot = Offset.Zero) {
                        fillPath(c, handset, 0.5f)
                    }
                }
            }
            AppId.COUNTDOWN -> {
                fillPath(c, Path().apply {
                    moveTo(34f, 30f)
                    lineTo(66f, 30f)
                    cubicTo(66f, 42f, 56f, 46f, 54f, 50f)
                    cubicTo(56f, 54f, 66f, 58f, 66f, 70f)
                    lineTo(34f, 70f)
                    cubicTo(34f, 58f, 44f, 54f, 46f, 50f)
                    cubicTo(44f, 46f, 34f, 42f, 34f, 30f)
                    close()
                })
                line(c, 30f, 22f, 70f, 22f, 8f)
                line(c, 30f, 78f, 70f, 78f, 8f)
            }
            AppId.WORLDBOOK -> {
                fillPath(c, Path().apply {
                    moveTo(48f, 32f)
                    cubicTo(40f, 26f, 28f, 26f, 20f, 30f)
                    lineTo(20f, 72f)
                    cubicTo(28f, 68f, 40f, 68f, 48f, 74f)
                    close()
                })
                fillPath(c, Path().apply {
                    moveTo(52f, 32f)
                    cubicTo(60f, 26f, 72f, 26f, 80f, 30f)
                    lineTo(80f, 72f)
                    cubicTo(72f, 68f, 60f, 68f, 52f, 74f)
                    close()
                })
            }
            AppId.MUSIC -> {
                val flagAndStem = Path().apply {
                    moveTo(49f, 68f)
                    lineTo(49f, 22f)
                    cubicTo(58f, 26f, 70f, 32f, 71f, 52f)
                    cubicTo(66f, 45f, 61f, 43f, 57f, 42f)
                    lineTo(57f, 68f)
                    close()
                }
                val note = union(flagAndStem, circlePath(43f, 68f, 13f))
                translate(left = 2f, top = 0f) {
                    fillPath(c, note, 1.5f)
                }
            }
            AppId.MEET -> {
                val pin = Path().apply {
                    moveTo(50f, 82f)
                    cubicTo(41f, 73f, 27f, 59f, 27f, 43f)
                    arcTo(Rect(27f, 20f, 73f, 66f), 180f, 180f, false)
                    cubicTo(73f, 59f, 59f, 73f, 50f, 82f)
                    close()
                }
                fillPath(c, diff(pin, circlePath(50f, 43f, 9f)))
            }
            AppId.LINK -> {
                val both = Path.combine(
                    PathOperation.Xor,
                    circlePath(36f, 50f, 24f),
                    circlePath(64f, 50f, 24f)
                )
                fillPath(c, both)
            }
            AppId.FORUM -> {
                line(c, 44f, 22f, 38f, 78f, 8f)
                line(c, 64f, 22f, 58f, 78f, 8f)
                line(c, 22f, 39f, 78f, 39f, 8f)
                line(c, 22f, 61f, 78f, 61f, 8f)
            }
            AppId.NOVEL -> {
                val cuts = union(
                    roundPath(35f, 26f, 40f, 74f, 2.5f),
                    roundPath(47f, 38f, 64f, 43f, 2.5f),
                    roundPath(47f, 50f, 64f, 55f, 2.5f)
                )
                fillPath(c, diff(roundPath(26f, 18f, 74f, 82f, 10f), cuts))
            }
            AppId.COUPLE -> {
                fillPath(c, Path().apply {
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
                line(c, 24f, 32f, 76f, 32f, 8f)
                line(c, 24f, 50f, 76f, 50f, 8f)
                line(c, 24f, 68f, 76f, 68f, 8f)
                disc(c, 40f, 32f, 10f)
                disc(c, 60f, 50f, 10f)
                disc(c, 46f, 68f, 10f)
            }
            AppId.SIDELINE -> {
                val flap = Path().apply {
                    moveTo(24f, 33f)
                    lineTo(50f, 53f)
                    lineTo(76f, 33f)
                    lineTo(76f, 41f)
                    lineTo(50f, 61f)
                    lineTo(24f, 41f)
                    close()
                }
                fillPath(c, diff(roundPath(18f, 26f, 82f, 74f, 10f), flap))
            }
            AppId.SHOP -> {
                stroked(c, 7f) {
                    moveTo(38f, 42f)
                    lineTo(38f, 32f)
                    arcTo(Rect(38f, 20f, 62f, 44f), 180f, 180f, false)
                    lineTo(62f, 42f)
                }
                val body = diff(
                    roundPath(24f, 38f, 76f, 82f, 11f),
                    union(circlePath(40f, 52f, 4f), circlePath(60f, 52f, 4f))
                )
                fillPath(c, body)
            }
            AppId.DREAM -> {
                val moon = Path.combine(
                    PathOperation.Difference,
                    circlePath(46f, 52f, 26f),
                    circlePath(60f, 42f, 21f)
                )
                fillPath(c, moon)
                disc(c, 73f, 27f, 4f)
            }
        }
    }
}

private fun polar(r: Float, deg: Double): Offset {
    val rad = deg * PI / 180.0
    return Offset(50f + (r * cos(rad)).toFloat(), 50f + (r * sin(rad)).toFloat())
}

private fun pen(w: Float) = Stroke(width = w, cap = StrokeCap.Round, join = StrokeJoin.Round)

private fun circlePath(x: Float, y: Float, r: Float) =
    Path().apply { addOval(Rect(Offset(x, y), r)) }

private fun roundPath(l: Float, t: Float, r: Float, b: Float, rad: Float) =
    Path().apply { addRoundRect(RoundRect(l, t, r, b, CornerRadius(rad))) }

private fun union(vararg parts: Path): Path {
    var out = parts[0]
    for (i in 1 until parts.size) {
        out = Path.combine(PathOperation.Union, out, parts[i])
    }
    return out
}

private fun diff(a: Path, b: Path): Path = Path.combine(PathOperation.Difference, a, b)

private fun DrawScope.line(c: Color, x1: Float, y1: Float, x2: Float, y2: Float, w: Float) =
    drawLine(c, Offset(x1, y1), Offset(x2, y2), strokeWidth = w, cap = StrokeCap.Round)

private fun DrawScope.disc(c: Color, x: Float, y: Float, r: Float) =
    drawCircle(c, r, Offset(x, y))

private fun DrawScope.stroked(c: Color, w: Float, build: Path.() -> Unit) =
    drawPath(Path().apply(build), c, style = pen(w))

private fun DrawScope.fillPath(c: Color, path: Path, soften: Float = 2f) {
    drawPath(path, c)
    drawPath(path, c, style = pen(soften))
}
