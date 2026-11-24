package com.island.phone.core.ui.icons

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import com.island.phone.core.ui.apps.AppId

private data class GlyphPalette(val top: Color, val bottom: Color, val shine: Color)

private fun palette(app: AppId, dark: Boolean): GlyphPalette {
    // 全部图标共用 iOS 风格的银灰玻璃色系，不按 App 分配彩色主题。
    return if (dark) {
        GlyphPalette(
            top = Color(0xFFE3E5E8),
            bottom = Color(0xFF777D85),
            shine = Color(0xFFF7F8FA)
        )
    } else {
        GlyphPalette(
            top = Color(0xFFFFFFFF),
            bottom = Color(0xFF9DA3AA),
            shine = Color(0xFFFFFFFF)
        )
    }
}

private fun rect(l: Float, t: Float, r: Float, b: Float, radius: Float) =
    Path().apply { addRoundRect(RoundRect(l, t, r, b, CornerRadius(radius))) }

private fun oval(x: Float, y: Float, radius: Float) =
    Path().apply { addOval(Rect(Offset(x - radius, y - radius), Offset(x + radius, y + radius))) }

private fun merge(vararg paths: Path): Path {
    var result = paths.first()
    paths.drop(1).forEach { result = Path.combine(PathOperation.Union, result, it) }
    return result
}

private fun cut(a: Path, b: Path) = Path.combine(PathOperation.Difference, a, b)

private fun path(build: Path.() -> Unit) = Path().apply(build)

private fun DrawScope.paint(path: Path, p: GlyphPalette) {
    // 先压出柔和的暗边，再叠银灰玻璃本体、内侧亮边和顶部反光。
    drawPath(
        path,
        Color(0x55000000),
        style = Stroke(width = 5.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
    drawPath(
        path,
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFFDFDFE),
                p.top.copy(alpha = 0.94f),
                Color(0xFFB9BEC5),
                p.bottom.copy(alpha = 0.96f)
            ),
            start = Offset(size.width * 0.16f, size.height * 0.06f),
            end = Offset(size.width * 0.82f, size.height * 0.98f)
        )
    )
    drawPath(
        path,
        Brush.linearGradient(
            colors = listOf(Color.White.copy(alpha = 0.82f), Color.Transparent),
            start = Offset(size.width * 0.2f, size.height * 0.04f),
            end = Offset(size.width * 0.72f, size.height * 0.7f)
        ),
        style = Stroke(width = 2.8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
    drawPath(
        path,
        Brush.linearGradient(
            colors = listOf(Color(0xFF555B63).copy(alpha = 0.5f), Color.Transparent),
            start = Offset(size.width * 0.8f, size.height * 0.9f),
            end = Offset(size.width * 0.28f, size.height * 0.2f)
        ),
        style = Stroke(width = 1.35f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

private fun DrawScope.detailLine(
    p: GlyphPalette,
    x1: Float, y1: Float, x2: Float, y2: Float,
    width: Float = 1.8f,
    alpha: Float = 0.72f
) {
    drawLine(
        p.shine.copy(alpha = alpha),
        Offset(x1, y1),
        Offset(x2, y2),
        strokeWidth = width,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawMaterialDetails(app: AppId, p: GlyphPalette, dark: Boolean) {
    val hi = p.shine.copy(alpha = if (dark) 0.72f else 0.9f)
    val shade = Color(0xFF555B63).copy(alpha = if (dark) 0.42f else 0.28f)
    when (app) {
        AppId.SETTINGS -> {
            drawCircle(shade, 17f, Offset(50f, 50f), style = Stroke(2.2f))
            drawCircle(hi, 8.5f, Offset(50f, 50f), style = Stroke(2.4f))
            for (i in 0 until 12) {
                val a = Math.PI * i / 6.0
                val x = 50f + (31f * kotlin.math.cos(a)).toFloat()
                val y = 50f + (31f * kotlin.math.sin(a)).toFloat()
                drawCircle(hi, 1.8f, Offset(x, y))
            }
        }
        AppId.ALBUM -> {
            drawCircle(hi, 7f, Offset(50f, 50f))
            drawCircle(shade, 29f, Offset(50f, 50f), style = Stroke(1.3f))
        }
        AppId.CLOCK -> {
            drawCircle(shade, 33f, Offset(50f, 50f), style = Stroke(2f))
            for (i in 0 until 12) {
                val a = Math.PI * i / 6.0
                val r1 = if (i % 3 == 0) 26f else 28f
                val r2 = 31f
                detailLine(p,
                    50f + (r1 * kotlin.math.sin(a)).toFloat(),
                    50f - (r1 * kotlin.math.cos(a)).toFloat(),
                    50f + (r2 * kotlin.math.sin(a)).toFloat(),
                    50f - (r2 * kotlin.math.cos(a)).toFloat(),
                    if (i % 3 == 0) 2.2f else 1.2f
                )
            }
            detailLine(p, 50f, 50f, 38f, 34f, 3f)
            detailLine(p, 50f, 50f, 65f, 57f, 2.4f)
            drawCircle(hi, 3f, Offset(50f, 50f))
        }
        AppId.CALENDAR -> {
            detailLine(p, 31f, 43f, 69f, 43f, 2.2f)
            detailLine(p, 35f, 55f, 65f, 55f, 1.5f, 0.55f)
            detailLine(p, 35f, 72f, 65f, 72f, 1.5f, 0.55f)
        }
        AppId.WEATHER -> {
            drawCircle(hi, 10f, Offset(35f, 33f), style = Stroke(2f))
            detailLine(p, 27f, 15f, 24f, 10f, 1.8f)
            detailLine(p, 43f, 15f, 46f, 10f, 1.8f)
            detailLine(p, 17f, 34f, 11f, 34f, 1.8f)
            detailLine(p, 29f, 83f, 25f, 90f, 2f)
            detailLine(p, 49f, 83f, 45f, 90f, 2f)
            detailLine(p, 69f, 83f, 65f, 90f, 2f)
        }
        AppId.CHAT -> {
            drawCircle(hi, 2.2f, Offset(36f, 45f))
            drawCircle(hi, 2.2f, Offset(50f, 45f))
            drawCircle(hi, 2.2f, Offset(64f, 45f))
        }
        AppId.PHONE -> {
            detailLine(p, 29f, 25f, 38f, 37f, 2.4f)
            detailLine(p, 62f, 64f, 76f, 74f, 2.4f)
        }
        AppId.MUSIC -> {
            detailLine(p, 62f, 23f, 62f, 59f, 2f)
            detailLine(p, 67f, 24f, 78f, 31f, 1.6f, 0.6f)
        }
        AppId.MEMO, AppId.NOVEL -> {
            detailLine(p, 34f, 32f, 34f, 70f, 1.6f, 0.6f)
            detailLine(p, 46f, 39f, 65f, 39f, 1.5f, 0.58f)
            detailLine(p, 46f, 53f, 65f, 53f, 1.5f, 0.58f)
        }
        AppId.MEET -> {
            drawCircle(hi, 8f, Offset(50f, 39f), style = Stroke(2f))
            drawCircle(shade, 13f, Offset(50f, 39f), style = Stroke(1.2f))
        }
        AppId.LINK -> {
            drawCircle(hi, 12f, Offset(38f, 50f), style = Stroke(2f))
            drawCircle(hi, 12f, Offset(62f, 50f), style = Stroke(2f))
        }
        AppId.AUTH, AppId.SHOP -> {
            detailLine(p, 39f, 45f, 39f, 36f, 2f)
            detailLine(p, 61f, 45f, 61f, 36f, 2f)
            drawCircle(hi, 2.2f, Offset(50f, 60f))
        }
        AppId.GROWTH -> {
            detailLine(p, 50f, 58f, 50f, 82f, 1.8f)
            detailLine(p, 50f, 62f, 69f, 37f, 1.4f, 0.6f)
        }
        AppId.WORLDBOOK -> {
            detailLine(p, 50f, 29f, 50f, 77f, 2f)
            detailLine(p, 25f, 38f, 42f, 38f, 1.4f, 0.55f)
            detailLine(p, 58f, 38f, 75f, 38f, 1.4f, 0.55f)
        }
        AppId.FORUM -> {
            drawCircle(hi, 2f, Offset(27f, 31f))
            drawCircle(hi, 2f, Offset(73f, 31f))
        }
        AppId.COUNTDOWN -> {
            detailLine(p, 35f, 25f, 65f, 25f, 2f)
            detailLine(p, 35f, 75f, 65f, 75f, 2f)
        }
        AppId.APPEARANCE -> {
            drawCircle(hi, 3f, Offset(50f, 50f))
        }
        AppId.COUPLE -> {
            detailLine(p, 50f, 35f, 50f, 70f, 1.2f, 0.25f)
        }
        AppId.PRESET -> {
            drawCircle(hi, 3f, Offset(42f, 32f))
            drawCircle(hi, 3f, Offset(63f, 51f))
            drawCircle(hi, 3f, Offset(35f, 70f))
        }
        AppId.SIDELINE -> {
            detailLine(p, 24f, 34f, 50f, 54f, 2f)
            detailLine(p, 50f, 54f, 76f, 34f, 2f)
        }
        AppId.DREAM -> {
            drawCircle(hi, 3f, Offset(74f, 25f))
            drawCircle(hi, 1.8f, Offset(27f, 35f))
        }
    }
}

fun DrawScope.drawPremiumAppGlyph(app: AppId, dark: Boolean) {
    scale(size.minDimension / 100f, pivot = Offset.Zero) {
        val p = palette(app, dark)
        val glyph = when (app) {
            AppId.AUTH -> cut(
                path {
                    moveTo(50f, 12f); cubicTo(64f, 20f, 74f, 22f, 82f, 24f)
                    lineTo(79f, 57f); cubicTo(76f, 75f, 63f, 85f, 50f, 91f)
                    cubicTo(37f, 85f, 24f, 75f, 21f, 57f); lineTo(18f, 24f)
                    cubicTo(30f, 22f, 40f, 20f, 50f, 12f); close()
                },
                merge(oval(50f, 49f, 6f), rect(46f, 53f, 54f, 68f, 4f))
            )
            AppId.CHAT -> merge(
                rect(15f, 20f, 85f, 69f, 23f),
                path { moveTo(27f, 60f); cubicTo(27f, 76f, 21f, 82f, 17f, 85f); cubicTo(35f, 82f, 44f, 74f, 49f, 66f); close() }
            ).let { cut(it, merge(oval(36f, 45f, 4f), oval(50f, 45f, 4f), oval(64f, 45f, 4f))) }
            AppId.APPEARANCE -> cut(merge(oval(39f, 42f, 25f), oval(61f, 59f, 25f)), oval(50f, 50f, 8f))
            AppId.GROWTH -> merge(
                rect(46f, 48f, 54f, 88f, 4f),
                path { moveTo(50f, 58f); cubicTo(47f, 31f, 65f, 18f, 85f, 22f); cubicTo(86f, 43f, 70f, 62f, 50f, 58f); close() },
                path { moveTo(50f, 72f); cubicTo(45f, 51f, 27f, 43f, 14f, 49f); cubicTo(18f, 69f, 34f, 79f, 50f, 72f); close() }
            )
            AppId.ALBUM -> {
                val petals = (0 until 8).map { i ->
                    val a = Math.PI * i / 4.0
                    oval(50f + (23 * kotlin.math.cos(a)).toFloat(), 50f + (23 * kotlin.math.sin(a)).toFloat(), 13f)
                }
                cut(merge(*petals.toTypedArray()), oval(50f, 50f, 8f))
            }
            AppId.CALENDAR -> cut(
                merge(rect(17f, 22f, 83f, 86f, 13f), rect(29f, 12f, 39f, 35f, 5f), rect(61f, 12f, 71f, 35f, 5f)),
                merge(rect(26f, 45f, 74f, 51f, 3f), oval(37f, 66f, 4f), oval(50f, 66f, 4f), oval(63f, 66f, 4f))
            )
            AppId.MEMO -> cut(rect(23f, 13f, 77f, 88f, 12f), merge(rect(35f, 34f, 65f, 40f, 3f), rect(35f, 49f, 65f, 55f, 3f), rect(35f, 64f, 58f, 70f, 3f)))
            AppId.WEATHER -> merge(oval(34f, 31f, 15f), rect(20f, 51f, 83f, 78f, 14f), oval(39f, 53f, 17f), oval(61f, 48f, 20f), oval(76f, 59f, 12f))
            AppId.CLOCK -> cut(oval(50f, 50f, 36f), merge(rect(47f, 22f, 53f, 52f, 3f), rect(48f, 47f, 72f, 53f, 3f))).let { merge(it, oval(50f, 50f, 4f)) }
            AppId.SETTINGS -> {
                val teeth = (0 until 12).map { i ->
                    val a = Math.PI * i / 6.0
                    val cx = 50f + (31 * kotlin.math.cos(a)).toFloat()
                    val cy = 50f + (31 * kotlin.math.sin(a)).toFloat()
                    rect(cx - 5f, cy - 5f, cx + 5f, cy + 5f, 2.5f)
                }
                cut(merge(oval(50f, 50f, 28f), *teeth.toTypedArray()), oval(50f, 50f, 11f))
            }
            AppId.PHONE -> path {
                moveTo(30f, 17f); cubicTo(23f, 17f, 18f, 24f, 20f, 34f)
                cubicTo(27f, 61f, 43f, 77f, 67f, 82f); cubicTo(77f, 84f, 84f, 78f, 83f, 69f)
                lineTo(81f, 59f); cubicTo(80f, 55f, 75f, 53f, 71f, 55f)
                lineTo(61f, 61f); cubicTo(51f, 57f, 44f, 50f, 40f, 40f)
                lineTo(46f, 30f); cubicTo(48f, 26f, 46f, 21f, 42f, 20f); close()
            }
            AppId.COUNTDOWN -> cut(
                path { moveTo(27f, 18f); lineTo(73f, 18f); cubicTo(73f, 36f, 60f, 42f, 55f, 50f); cubicTo(60f, 58f, 73f, 64f, 73f, 82f); lineTo(27f, 82f); cubicTo(27f, 64f, 40f, 58f, 45f, 50f); cubicTo(40f, 42f, 27f, 36f, 27f, 18f); close() },
                merge(rect(34f, 29f, 66f, 35f, 3f), rect(34f, 65f, 66f, 71f, 3f))
            )
            AppId.WORLDBOOK -> merge(
                path { moveTo(48f, 27f); cubicTo(38f, 19f, 25f, 20f, 16f, 26f); lineTo(16f, 78f); cubicTo(28f, 71f, 40f, 72f, 48f, 81f); close() },
                path { moveTo(52f, 27f); cubicTo(62f, 19f, 75f, 20f, 84f, 26f); lineTo(84f, 78f); cubicTo(72f, 71f, 60f, 72f, 52f, 81f); close() }
            )
            AppId.MUSIC -> merge(
                rect(58f, 17f, 67f, 68f, 4f),
                path { moveTo(64f, 18f); cubicTo(75f, 21f, 83f, 29f, 84f, 43f); lineTo(65f, 39f); close() },
                oval(43f, 70f, 14f)
            )
            AppId.MEET -> cut(
                path { moveTo(50f, 90f); cubicTo(39f, 75f, 19f, 55f, 19f, 39f); cubicTo(19f, 19f, 33f, 9f, 50f, 9f); cubicTo(67f, 9f, 81f, 19f, 81f, 39f); cubicTo(81f, 55f, 61f, 75f, 50f, 90f); close() },
                oval(50f, 39f, 12f)
            )
            AppId.LINK -> cut(merge(oval(38f, 50f, 24f), oval(62f, 50f, 24f)), merge(oval(38f, 50f, 11f), oval(62f, 50f, 11f)))
            AppId.FORUM -> merge(rect(22f, 24f, 32f, 76f, 5f), rect(59f, 24f, 69f, 76f, 5f), rect(15f, 39f, 85f, 48f, 4f), rect(15f, 58f, 85f, 67f, 4f))
            AppId.NOVEL -> cut(rect(21f, 13f, 79f, 87f, 10f), merge(rect(34f, 28f, 40f, 72f, 3f), rect(49f, 34f, 68f, 40f, 3f), rect(49f, 48f, 68f, 54f, 3f), rect(49f, 62f, 63f, 68f, 3f)))
            AppId.COUPLE -> path {
                moveTo(50f, 84f); cubicTo(42f, 77f, 15f, 57f, 15f, 37f)
                cubicTo(15f, 12f, 43f, 8f, 50f, 30f); cubicTo(57f, 8f, 85f, 12f, 85f, 37f)
                cubicTo(85f, 57f, 58f, 77f, 50f, 84f); close()
            }
            AppId.PRESET -> merge(
                rect(20f, 28f, 80f, 36f, 4f), rect(20f, 47f, 80f, 55f, 4f), rect(20f, 66f, 80f, 74f, 4f),
                oval(42f, 32f, 9f), oval(63f, 51f, 9f), oval(35f, 70f, 9f)
            )
            AppId.SIDELINE -> cut(rect(15f, 25f, 85f, 76f, 12f), path { moveTo(18f, 31f); lineTo(50f, 55f); lineTo(82f, 31f); lineTo(82f, 42f); lineTo(50f, 66f); lineTo(18f, 42f); close() })
            AppId.SHOP -> cut(merge(rect(20f, 38f, 80f, 86f, 12f), path { moveTo(35f, 43f); lineTo(35f, 30f); cubicTo(35f, 10f, 65f, 10f, 65f, 30f); lineTo(65f, 43f); close() }), merge(oval(40f, 58f, 3f), oval(60f, 58f, 3f)))
            AppId.DREAM -> cut(oval(48f, 51f, 32f), oval(63f, 39f, 27f)).let { merge(it, oval(76f, 22f, 4f), oval(23f, 29f, 3f)) }
        }
        paint(glyph, p)
        drawMaterialDetails(app, p, dark)
        drawCircle(Color.White.copy(alpha = if (dark) 0.48f else 0.62f), radius = 2.1f, center = Offset(32f, 25f))
    }
}
