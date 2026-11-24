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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private fun oval(cx: Float, cy: Float, r: Float) = Path().apply {
    addOval(Rect(Offset(cx - r, cy - r), Offset(cx + r, cy + r)))
}

private fun round(l: Float, t: Float, r: Float, b: Float, radius: Float) = Path().apply {
    addRoundRect(RoundRect(l, t, r, b, CornerRadius(radius)))
}

private fun combine(vararg paths: Path): Path {
    var result = paths.first()
    paths.drop(1).forEach { result = Path.combine(PathOperation.Union, result, it) }
    return result
}

private fun subtract(a: Path, b: Path) = Path.combine(PathOperation.Difference, a, b)

private fun gearPath(): Path {
    val points = mutableListOf<Offset>()
    for (i in 0 until 12) {
        val a = i * PI / 6.0
        val radii = listOf(27.0, 27.0, 36.0, 36.0)
        val angles = listOf(-0.18, -0.11, -0.11, 0.11)
        for (j in 0..3) {
            val angle = a + angles[j]
            points += Offset(50f + (radii[j] * cos(angle)).toFloat(), 50f + (radii[j] * sin(angle)).toFloat())
        }
    }
    return Path().apply {
        points.forEachIndexed { index, p -> if (index == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
        close()
    }
}

private fun glyph(app: AppId): Path = when (app) {
    AppId.AUTH -> subtract(
        Path().apply {
            moveTo(50f, 10f); cubicTo(63f, 18f, 75f, 21f, 84f, 23f)
            lineTo(79f, 59f); cubicTo(76f, 76f, 62f, 87f, 50f, 93f)
            cubicTo(38f, 87f, 24f, 76f, 21f, 59f); lineTo(16f, 23f)
            cubicTo(27f, 21f, 39f, 18f, 50f, 10f); close()
        },
        combine(oval(50f, 47f, 7f), round(46f, 51f, 54f, 70f, 4f))
    )
    AppId.CHAT -> combine(
        Path().apply {
            moveTo(14f, 23f); cubicTo(14f, 16f, 21f, 12f, 30f, 12f)
            lineTo(70f, 12f); cubicTo(80f, 12f, 86f, 18f, 86f, 27f)
            lineTo(86f, 55f); cubicTo(86f, 64f, 80f, 69f, 70f, 69f)
            lineTo(43f, 69f); lineTo(20f, 88f); lineTo(25f, 67f)
            cubicTo(17f, 64f, 14f, 59f, 14f, 51f); close()
        },
        oval(36f, 41f, 3.5f), oval(50f, 41f, 3.5f), oval(64f, 41f, 3.5f)
    )
    AppId.APPEARANCE -> subtract(
        combine(oval(40f, 40f, 28f), oval(62f, 61f, 28f)),
        oval(50f, 50f, 8f)
    )
    AppId.GROWTH -> combine(
        round(46f, 48f, 54f, 91f, 4f),
        Path().apply {
            moveTo(50f, 61f); cubicTo(20f, 58f, 14f, 35f, 20f, 19f)
            cubicTo(40f, 18f, 55f, 34f, 50f, 61f); close()
        },
        Path().apply {
            moveTo(50f, 70f); cubicTo(58f, 42f, 77f, 35f, 88f, 43f)
            cubicTo(86f, 65f, 68f, 78f, 50f, 70f); close()
        }
    )
    AppId.ALBUM -> {
        val blades = (0 until 8).map { i ->
            val a = i * PI / 4.0
            val cx = 50f + (21f * cos(a)).toFloat()
            val cy = 50f + (21f * sin(a)).toFloat()
            val p = oval(cx, cy, 13f)
            Path.combine(PathOperation.Intersect, p, oval(50f, 50f, 36f))
        }
        subtract(combine(*blades.toTypedArray()), oval(50f, 50f, 7f))
    }
    AppId.CALENDAR -> subtract(
        combine(round(17f, 23f, 83f, 88f, 13f), round(29f, 12f, 39f, 38f, 5f), round(61f, 12f, 71f, 38f, 5f)),
        round(25f, 49f, 75f, 79f, 5f)
    ).let { combine(it, oval(35f, 64f, 3f), oval(50f, 64f, 3f), oval(65f, 64f, 3f)) }
    AppId.MEMO -> subtract(
        Path().apply {
            moveTo(25f, 12f); lineTo(63f, 12f); lineTo(78f, 27f); lineTo(78f, 86f)
            cubicTo(78f, 90f, 75f, 92f, 70f, 92f); lineTo(25f, 92f)
            cubicTo(20f, 92f, 18f, 89f, 18f, 84f); lineTo(18f, 20f)
            cubicTo(18f, 15f, 21f, 12f, 25f, 12f); close()
        },
        combine(round(33f, 39f, 67f, 45f, 3f), round(33f, 54f, 67f, 60f, 3f), round(33f, 69f, 57f, 75f, 3f))
    )
    AppId.WEATHER -> combine(
        oval(34f, 35f, 17f),
        round(20f, 48f, 85f, 82f, 17f),
        oval(42f, 56f, 18f), oval(65f, 52f, 20f), oval(78f, 64f, 12f)
    )
    AppId.CLOCK -> subtract(
        combine(oval(50f, 50f, 39f), oval(50f, 50f, 33f)),
        round(46f, 20f, 54f, 53f, 4f)
    ).let { combine(it, round(49f, 48f, 74f, 55f, 3f), oval(50f, 50f, 5f)) }
    AppId.SETTINGS -> subtract(gearPath(), oval(50f, 50f, 13f))
    AppId.PHONE -> Path().apply {
        moveTo(28f, 12f); cubicTo(21f, 12f, 15f, 19f, 17f, 28f)
        cubicTo(23f, 57f, 43f, 78f, 72f, 84f); cubicTo(82f, 86f, 88f, 79f, 86f, 70f)
        lineTo(83f, 59f); cubicTo(82f, 54f, 77f, 52f, 72f, 55f)
        lineTo(62f, 62f); cubicTo(52f, 57f, 43f, 48f, 39f, 38f)
        lineTo(46f, 28f); cubicTo(49f, 24f, 46f, 19f, 42f, 17f)
        lineTo(28f, 12f); close()
    }
    AppId.COUNTDOWN -> subtract(
        Path().apply {
            moveTo(23f, 12f); lineTo(77f, 12f); lineTo(77f, 20f)
            cubicTo(77f, 35f, 62f, 43f, 55f, 50f)
            cubicTo(62f, 57f, 77f, 65f, 77f, 80f)
            lineTo(77f, 88f); lineTo(23f, 88f); lineTo(23f, 80f)
            cubicTo(23f, 65f, 38f, 57f, 45f, 50f)
            cubicTo(38f, 43f, 23f, 35f, 23f, 20f); close()
        },
        combine(round(34f, 24f, 66f, 30f, 3f), round(34f, 70f, 66f, 76f, 3f))
    )
    AppId.WORLDBOOK -> combine(
        Path().apply {
            moveTo(48f, 22f); cubicTo(36f, 14f, 23f, 17f, 13f, 24f)
            lineTo(13f, 83f); cubicTo(27f, 76f, 39f, 78f, 48f, 88f); close()
        },
        Path().apply {
            moveTo(52f, 22f); cubicTo(64f, 14f, 77f, 17f, 87f, 24f)
            lineTo(87f, 83f); cubicTo(73f, 76f, 61f, 78f, 52f, 88f); close()
        }
    )
    AppId.MUSIC -> combine(
        Path().apply {
            moveTo(58f, 12f); lineTo(76f, 12f); lineTo(76f, 62f)
            cubicTo(76f, 74f, 66f, 81f, 55f, 81f)
            cubicTo(44f, 81f, 39f, 75f, 42f, 67f)
            cubicTo(45f, 58f, 56f, 55f, 66f, 59f); lineTo(66f, 31f)
            lineTo(58f, 31f); close()
        }
    )
    AppId.MEET -> subtract(
        Path().apply {
            moveTo(50f, 91f); cubicTo(43f, 80f, 17f, 55f, 17f, 38f)
            cubicTo(17f, 18f, 31f, 9f, 50f, 9f); cubicTo(69f, 9f, 83f, 18f, 83f, 38f)
            cubicTo(83f, 55f, 57f, 80f, 50f, 91f); close()
        },
        oval(50f, 38f, 13f)
    )
    AppId.LINK -> subtract(
        combine(round(10f, 31f, 61f, 68f, 18f), round(39f, 31f, 90f, 68f, 18f)),
        combine(round(24f, 42f, 52f, 57f, 7f), round(48f, 42f, 76f, 57f, 7f))
    )
    AppId.FORUM -> combine(
        round(14f, 15f, 86f, 85f, 18f),
        Path().apply { moveTo(31f, 78f); lineTo(22f, 94f); lineTo(50f, 80f); close() }
    ).let { subtract(it, combine(round(30f, 31f, 70f, 38f, 3f), round(30f, 47f, 70f, 54f, 3f), round(30f, 63f, 58f, 70f, 3f))) }
    AppId.NOVEL -> subtract(
        Path().apply {
            moveTo(20f, 12f); lineTo(80f, 12f); lineTo(80f, 88f); lineTo(20f, 88f); close()
        },
        combine(round(32f, 28f, 68f, 34f, 3f), round(32f, 43f, 68f, 49f, 3f), round(32f, 58f, 60f, 64f, 3f), round(32f, 73f, 54f, 79f, 3f))
    )
    AppId.COUPLE -> combine(
        Path().apply {
            moveTo(50f, 87f); cubicTo(40f, 78f, 14f, 58f, 14f, 38f)
            cubicTo(14f, 15f, 43f, 8f, 50f, 31f)
            cubicTo(57f, 8f, 86f, 15f, 86f, 38f)
            cubicTo(86f, 58f, 60f, 78f, 50f, 87f); close()
        }
    )
    AppId.PRESET -> combine(
        round(14f, 24f, 86f, 33f, 5f), round(14f, 46f, 86f, 55f, 5f), round(14f, 68f, 86f, 77f, 5f),
        oval(39f, 28f, 10f), oval(64f, 50f, 10f), oval(31f, 72f, 10f)
    )
    AppId.SIDELINE -> subtract(
        Path().apply {
            moveTo(14f, 22f); lineTo(86f, 22f); lineTo(86f, 72f)
            cubicTo(86f, 79f, 80f, 83f, 73f, 83f); lineTo(27f, 83f)
            cubicTo(20f, 83f, 14f, 79f, 14f, 72f); close()
        },
        Path().apply {
            moveTo(16f, 27f); lineTo(50f, 55f); lineTo(84f, 27f)
            lineTo(84f, 39f); lineTo(50f, 67f); lineTo(16f, 39f); close()
        }
    )
    AppId.SHOP -> subtract(
        combine(round(17f, 34f, 83f, 88f, 12f), Path().apply {
            moveTo(33f, 40f); lineTo(33f, 27f); cubicTo(33f, 5f, 67f, 5f, 67f, 27f)
            lineTo(67f, 40f); close()
        }),
        combine(oval(39f, 58f, 3f), oval(61f, 58f, 3f))
    )
    AppId.DREAM -> subtract(oval(48f, 52f, 34f), oval(64f, 39f, 29f)).let {
        combine(it, oval(76f, 21f, 4f), oval(24f, 29f, 2.5f), oval(78f, 67f, 2.5f))
    }
}

private fun DrawScope.drawGlyphMaterial(shape: Path, app: AppId, dark: Boolean) {
    val silverTop = if (dark) Color(0xFFE7E9EC) else Color(0xFFFFFFFF)
    val silverMid = if (dark) Color(0xFFB9BEC5) else Color(0xFFDDE1E5)
    val silverBottom = if (dark) Color(0xFF666D76) else Color(0xFF9299A2)
    drawPath(shape, Color(0x66000000), style = Stroke(7f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(
        shape,
        Brush.linearGradient(
            listOf(silverTop, Color(0xFFF5F6F7), silverMid, silverBottom),
            start = Offset(12f, 5f),
            end = Offset(86f, 96f)
        )
    )
    drawPath(
        shape,
        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.96f), Color.White.copy(alpha = 0.08f))),
        style = Stroke(3.1f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
    drawPath(
        shape,
        Brush.linearGradient(listOf(Color(0xFF525963).copy(alpha = 0.56f), Color.Transparent)),
        style = Stroke(1.4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
    val highlight = Color.White.copy(alpha = if (dark) 0.66f else 0.82f)
    val shadow = Color(0xFF5B626B).copy(alpha = if (dark) 0.45f else 0.27f)
    when (app) {
        AppId.SETTINGS -> {
            drawCircle(shadow, 17f, Offset(50f, 50f), style = Stroke(2.2f))
            drawCircle(highlight, 8f, Offset(50f, 50f), style = Stroke(2.8f))
        }
        AppId.CLOCK -> {
            drawLine(highlight, Offset(50f, 50f), Offset(39f, 32f), 3.2f, cap = StrokeCap.Round)
            drawLine(highlight, Offset(50f, 50f), Offset(68f, 59f), 2.7f, cap = StrokeCap.Round)
            drawCircle(highlight, 3f, Offset(50f, 50f))
        }
        AppId.CALENDAR -> {
            drawLine(shadow, Offset(25f, 43f), Offset(75f, 43f), 2f, cap = StrokeCap.Round)
            for (x in listOf(35f, 50f, 65f)) for (y in listOf(57f, 70f)) {
                drawCircle(highlight, 2f, Offset(x, y))
            }
        }
        AppId.WEATHER -> {
            drawCircle(highlight, 9f, Offset(34f, 33f), style = Stroke(2.2f))
            for (i in 0 until 8) {
                val a = i * PI / 4.0
                drawLine(highlight, Offset(34f + 22f * cos(a).toFloat(), 33f + 22f * sin(a).toFloat()),
                    Offset(34f + 27f * cos(a).toFloat(), 33f + 27f * sin(a).toFloat()), 1.8f, cap = StrokeCap.Round)
            }
        }
        AppId.CHAT, AppId.FORUM -> {
            drawCircle(highlight, 2.2f, Offset(37f, 41f))
            drawCircle(highlight, 2.2f, Offset(50f, 41f))
            drawCircle(highlight, 2.2f, Offset(63f, 41f))
        }
        AppId.ALBUM -> {
            drawCircle(highlight, 5f, Offset(50f, 50f))
            drawCircle(shadow, 28f, Offset(50f, 50f), style = Stroke(1.4f))
        }
        AppId.MUSIC -> {
            drawLine(highlight, Offset(64f, 17f), Offset(64f, 58f), 1.8f, cap = StrokeCap.Round)
        }
        AppId.MEMO, AppId.NOVEL -> {
            drawLine(highlight, Offset(31f, 23f), Offset(31f, 77f), 1.5f, cap = StrokeCap.Round)
        }
        AppId.MEET -> drawCircle(highlight, 3f, Offset(50f, 38f))
        AppId.LINK -> drawLine(highlight, Offset(41f, 50f), Offset(59f, 50f), 2.2f, cap = StrokeCap.Round)
        AppId.PHONE -> {
            drawLine(highlight, Offset(26f, 24f), Offset(36f, 38f), 2f, cap = StrokeCap.Round)
            drawLine(highlight, Offset(64f, 64f), Offset(77f, 75f), 2f, cap = StrokeCap.Round)
        }
        AppId.COUNTDOWN -> drawLine(highlight, Offset(50f, 43f), Offset(50f, 57f), 2f, cap = StrokeCap.Round)
        AppId.WORLDBOOK -> drawLine(highlight, Offset(50f, 25f), Offset(50f, 81f), 2f, cap = StrokeCap.Round)
        AppId.GROWTH -> drawLine(highlight, Offset(50f, 62f), Offset(50f, 85f), 2f, cap = StrokeCap.Round)
        AppId.AUTH, AppId.SHOP -> drawCircle(highlight, 2f, Offset(50f, 60f))
        AppId.APPEARANCE, AppId.COUPLE, AppId.PRESET, AppId.SIDELINE, AppId.DREAM -> Unit
    }
}

fun DrawScope.drawPremiumAppGlyph(app: AppId, dark: Boolean) {
    scale(size.minDimension / 100f, pivot = Offset.Zero) {
        drawGlyphMaterial(glyph(app), app, dark)
    }
}
