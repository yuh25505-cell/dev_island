package com.island.phone.feature.home

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

data class CellRect(val x: Float, val y: Float, val w: Float, val h: Float)

fun Density.widgetRect(kind: WidgetKind, row: Int, col: Int, width: Int): CellRect {
    val padH = GridPadH.toPx()
    val padV = GridPadV.toPx()
    val cellW = (width - 2 * padH) / GridColumns
    val stride = (CellHeight + RowGap).toPx()
    val inset = (cellW - 60.dp.toPx()) / 2f
    val ax = padH + col * cellW + inset
    val aw = kind.cols * cellW - 2 * inset
    val ay = padV + row * stride
    val ah = (kind.rows - 1) * stride + 60.dp.toPx()
    return CellRect(ax, ay, aw, ah)
}
