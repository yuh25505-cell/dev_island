package com.island.phone.core.ui.appearance

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

fun DrawScope.drawImageCover(image: ImageBitmap) {
    val w = size.width
    val h = size.height
    if (w <= 0f || h <= 0f) return
    val scale = maxOf(w / image.width, h / image.height)
    val srcW = (w / scale).roundToInt().coerceIn(1, image.width)
    val srcH = (h / scale).roundToInt().coerceIn(1, image.height)
    drawImage(
        image = image,
        srcOffset = IntOffset((image.width - srcW) / 2, (image.height - srcH) / 2),
        srcSize = IntSize(srcW, srcH),
        dstSize = IntSize(w.roundToInt(), h.roundToInt()),
        filterQuality = FilterQuality.Medium
    )
}
