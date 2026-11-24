package com.island.phone.core.ui.icons

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale

private fun rearChatBubble() = Path().apply {
    moveTo(203f, 104f)
    cubicTo(270f, 104f, 322f, 145f, 338f, 199f)
    cubicTo(282f, 199f, 235f, 222f, 216f, 267f)
    cubicTo(204f, 289f, 203f, 313f, 211f, 329f)
    cubicTo(190f, 330f, 169f, 326f, 151f, 320f)
    lineTo(115f, 341f)
    cubicTo(112f, 343f, 110f, 340f, 112f, 334f)
    lineTo(120f, 305f)
    cubicTo(89f, 282f, 69f, 249f, 69f, 215f)
    cubicTo(69f, 153f, 126f, 104f, 203f, 104f)
    close()
}

private fun frontChatBubble() = Path().apply {
    moveTo(331f, 211f)
    cubicTo(394f, 211f, 444f, 253f, 444f, 306f)
    cubicTo(444f, 334f, 428f, 359f, 402f, 379f)
    lineTo(407f, 407f)
    cubicTo(408f, 412f, 405f, 412f, 400f, 409f)
    lineTo(373f, 394f)
    cubicTo(360f, 397f, 346f, 399f, 331f, 399f)
    cubicTo(268f, 399f, 219f, 357f, 219f, 305f)
    cubicTo(219f, 253f, 268f, 211f, 331f, 211f)
    close()
}

private fun DrawScope.drawBubble(path: Path, front: Boolean) {
    val top = if (front) Color(0xFFF7FFFC) else Color(0xFFF5FFFA)
    val bottom = if (front) Color(0xFFD9F9EB) else Color(0xFFE2FBF0)
    drawPath(
        path,
        Brush.linearGradient(
            colors = listOf(Color.White, top, bottom),
            start = Offset(140f, 100f),
            end = Offset(350f, 420f)
        )
    )
    drawPath(path, Color(0x1A00A85C), style = Stroke(width = 9f))
    drawPath(
        path,
        Brush.linearGradient(
            colors = listOf(Color.White.copy(alpha = 0.98f), Color.White.copy(alpha = 0.38f)),
            start = Offset(140f, 105f),
            end = Offset(300f, 390f)
        ),
        style = Stroke(width = 5f)
    )
    drawPath(path, Color(0x55FFFFFF), style = Stroke(width = 2f))
}

private fun DrawScope.drawChatEye(x: Float, y: Float) {
    drawCircle(Color(0x3000C96A), 25f, Offset(x, y + 2f))
    drawCircle(Color(0x66FFFFFF), 21f, Offset(x, y))
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF00D875), Color(0xFF00B95F)),
            center = Offset(x - 4f, y - 5f),
            radius = 25f
        ),
        radius = 18f,
        center = Offset(x, y)
    )
}

fun DrawScope.drawChatDefaultIcon() {
    scale(size.width / 512f, size.height / 512f, pivot = Offset.Zero) {
        val tile = Path().apply {
            addRoundRect(RoundRect(Rect(0f, 0f, 512f, 512f), CornerRadius(116f, 116f)))
        }
        drawPath(
            tile,
            Brush.linearGradient(
                colors = listOf(Color(0xFF00E978), Color(0xFF00D66A), Color(0xFF00B95B)),
                start = Offset(180f, 0f),
                end = Offset(300f, 512f)
            )
        )
        drawPath(
            tile,
            Brush.linearGradient(
                colors = listOf(Color.White.copy(alpha = 0.55f), Color.Transparent, Color(0x33007839)),
                start = Offset(40f, 0f),
                end = Offset(450f, 512f)
            ),
            style = Stroke(width = 5f)
        )
        val rear = rearChatBubble()
        val front = frontChatBubble()
        drawPath(rear, Color(0x22006E3C), style = Stroke(width = 15f))
        drawBubble(rear, false)
        drawChatEye(159f, 179f)
        drawChatEye(250f, 179f)
        drawPath(front, Color(0x30006D3D), style = Stroke(width = 14f))
        drawBubble(front, true)
        drawChatEye(294f, 275f)
        drawChatEye(370f, 275f)
    }
}
