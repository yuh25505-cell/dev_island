package com.island.phone.feature.home

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt

private data class Cell(val row: Int, val col: Int)

private fun Density.cellAt(pos: Offset, width: Int): Cell {
    val padH = GridPadH.toPx()
    val padV = GridPadV.toPx()
    val cellW = (width - 2 * padH) / GridColumns
    val rowH = (CellHeight + RowGap).toPx()
    val col = ((pos.x - padH) / cellW).toInt().coerceIn(0, GridColumns - 1)
    val row = ((pos.y - padV) / rowH).toInt().coerceIn(0, GridRows - 1)
    return Cell(row, col)
}

private fun Density.iconCellHit(pos: Offset, width: Int): Cell? {
    val padH = GridPadH.toPx()
    val padV = GridPadV.toPx()
    val cellW = (width - 2 * padH) / GridColumns
    val stride = (CellHeight + RowGap).toPx()
    val x = pos.x - padH
    val y = pos.y - padV
    if (x < 0f || y < 0f) return null
    val col = (x / cellW).toInt()
    val row = (y / stride).toInt()
    if (col >= GridColumns || row >= GridRows) return null
    val insideX = abs(x - (col + 0.5f) * cellW) <= 36.dp.toPx()
    val insideY = y - row * stride <= CellHeight.toPx()
    return if (insideX && insideY) Cell(row, col) else null
}

private fun Density.widgetHit(state: HomeState, page: Int, pos: Offset, width: Int): WidgetPlacement? =
    state.widgets.firstOrNull {
        if (it.page != page) {
            false
        } else {
            val r = widgetRect(it.kind, it.row, it.col, width)
            pos.x >= r.x && pos.x <= r.x + r.w && pos.y >= r.y && pos.y <= r.y + r.h
        }
    }

private fun Density.inCellCenter(pos: Offset, width: Int): Boolean {
    val padH = GridPadH.toPx()
    val padV = GridPadV.toPx()
    val cellW = (width - 2 * padH) / GridColumns
    val rowH = (CellHeight + RowGap).toPx()
    val x = ((pos.x - padH).coerceIn(0f, cellW * GridColumns - 1f) % cellW) / cellW
    val y = (pos.y - padV).coerceAtLeast(0f) % rowH
    return x in 0.2f..0.8f && y in 6.dp.toPx()..56.dp.toPx()
}

private fun finishDrag(state: HomeState) {
    val dragged = state.dragging
    val hover = state.hoverKey
    if (dragged is AppItem && hover != null && state.mergeReady) {
        state.mergeInto(dragged, hover)
    }
    state.clearDrag()
}

private fun Density.finishWidgetDrag(state: HomeState, page: Int, width: Int) {
    val kind = state.draggingWidget
    if (kind != null) {
        val base = widgetRect(kind, 0, 0, width)
        val cellW = (width - 2 * GridPadH.toPx()) / GridColumns
        val stride = (CellHeight + RowGap).toPx()
        val topLeft = state.dragPos - state.widgetGrab
        state.dropWidget(
            kind,
            page,
            ((topLeft.y - base.y) / stride).roundToInt(),
            ((topLeft.x - base.x) / cellW).roundToInt()
        )
    }
    state.clearDrag()
}

@Composable
fun HomePager(state: HomeState, modifier: Modifier = Modifier) {
    val pageCount = state.pageCount()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { state.pageCount() })
    val density = LocalDensity.current
    var boxWidth by remember { mutableStateOf(0) }
    val edge by remember {
        derivedStateOf {
            val edgePx = with(density) { 32.dp.toPx() }
            when {
                !state.isDraggingAny || boxWidth == 0 -> 0
                state.dragPos.x < edgePx -> -1
                state.dragPos.x > boxWidth - edgePx -> 1
                else -> 0
            }
        }
    }
    LaunchedEffect(edge) {
        if (edge != 0) {
            while (true) {
                delay(600)
                val target = pagerState.currentPage + edge
                if (target < 0 || target >= pagerState.pageCount) break
                pagerState.animateScrollToPage(target)
                val dragged = state.dragging
                if (dragged != null) {
                    val cell = density.cellAt(state.dragPos, boxWidth)
                    val index = state.itemIndexAt(pagerState.currentPage, cell.row, cell.col)
                    if (index != null) state.moveTo(dragged.key, index)
                }
            }
        }
    }
    LaunchedEffect(state.pendingMove) {
        val target = state.pendingMove
        if (target != null) {
            delay(220)
            val dragged = state.dragging
            if (dragged != null && state.pendingMove == target) {
                state.moveTo(dragged.key, target)
                state.pendingMove = null
            }
        }
    }
    LaunchedEffect(state.hoverKey) {
        if (state.hoverKey != null) {
            delay(450)
            state.mergeReady = true
        }
    }
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .onSizeChanged { boxWidth = it.width }
                .pointerInput(Unit) {
                    detectTapGestures { pos ->
                        val page = pagerState.currentPage
                        val tapped = widgetHit(state, page, pos, size.width)
                        val tappedKind = if (!state.editing) tapped?.kind else null
                        if (tappedKind == WidgetKind.PHOTOCARD) {
                            state.photoCardOpen = true
                        } else if (tappedKind == WidgetKind.VINYL) {
                            state.vinylOpen = true
                        } else {
                            val cell = cellAt(pos, size.width)
                            val index = state.itemIndexAt(page, cell.row, cell.col)
                            val item = if (index != null) state.items.getOrNull(index) else null
                            if (item is FolderItem) {
                                state.openFolderId = item.id
                            } else if (state.editing && index != null && item == null) {
                                state.editing = false
                            }
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { pos ->
                            val page = pagerState.currentPage
                            val widget = widgetHit(state, page, pos, size.width)
                            if (widget != null) {
                                state.clearDrag()
                                val rect = widgetRect(widget.kind, widget.row, widget.col, size.width)
                                state.draggingWidget = widget.kind
                                state.widgetGrab = Offset(pos.x - rect.x, pos.y - rect.y)
                                state.dragPos = pos
                                state.editing = true
                            } else {
                                val cell = iconCellHit(pos, size.width)
                                val index = if (cell != null) {
                                    state.itemIndexAt(page, cell.row, cell.col)
                                } else {
                                    null
                                }
                                val item = if (index != null) state.items.getOrNull(index) else null
                                if (item != null) {
                                    state.clearDrag()
                                    state.dragging = item
                                    state.dragPos = pos
                                    state.editing = true
                                }
                            }
                        },
                        onDrag = { change, amount ->
                            if (state.draggingWidget != null) {
                                change.consume()
                                state.dragPos = state.dragPos + amount
                            } else {
                                val dragged = state.dragging
                                if (dragged != null) {
                                    change.consume()
                                    state.dragPos = state.dragPos + amount
                                    val cell = cellAt(state.dragPos, size.width)
                                    val index = state.itemIndexAt(pagerState.currentPage, cell.row, cell.col)
                                    val target = if (index != null) state.items.getOrNull(index) else null
                                    if (target != null &&
                                        state.canMerge(dragged, target) &&
                                        inCellCenter(state.dragPos, size.width)
                                    ) {
                                        state.pendingMove = null
                                        if (state.hoverKey != target.key) {
                                            state.hoverKey = target.key
                                            state.mergeReady = false
                                        }
                                    } else {
                                        state.hoverKey = null
                                        state.mergeReady = false
                                        val own = state.indexOfKey(dragged.key)
                                        state.pendingMove = if (index != null && index != own) index else null
                                    }
                                }
                            }
                        },
                        onDragEnd = {
                            if (state.draggingWidget != null) {
                                finishWidgetDrag(state, pagerState.currentPage, size.width)
                            } else {
                                finishDrag(state)
                            }
                        },
                        onDragCancel = { state.clearDrag() }
                    )
                }
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = !state.isDraggingAny,
                modifier = Modifier.fillMaxWidth()
            ) { index ->
                AppGrid(page = index, state = state)
            }
            val dragged = state.dragging
            if (dragged != null) {
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (state.dragPos.x - 36.dp.toPx()).roundToInt(),
                                (state.dragPos.y - 42.dp.toPx()).roundToInt()
                            )
                        }
                        .size(72.dp, 84.dp)
                        .graphicsLayer {
                            scaleX = 1.12f
                            scaleY = 1.12f
                        },
                    contentAlignment = Alignment.TopCenter
                ) {
                    HomeItemIcon(dragged)
                }
            }
            val draggedWidget = state.draggingWidget
            if (draggedWidget != null) {
                val rect = with(density) { widgetRect(draggedWidget, 0, 0, boxWidth) }
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (state.dragPos.x - state.widgetGrab.x).roundToInt(),
                                (state.dragPos.y - state.widgetGrab.y).roundToInt()
                            )
                        }
                        .size(
                            with(density) { rect.w.toDp() },
                            with(density) { rect.h.toDp() }
                        )
                        .graphicsLayer {
                            scaleX = 1.05f
                            scaleY = 1.05f
                        }
                ) {
                    WidgetContent(draggedWidget, Modifier.fillMaxSize())
                }
            }
        }
        PageDots(
            count = pageCount,
            current = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
        )
    }
}
