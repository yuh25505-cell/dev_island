package com.island.phone.feature.home

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import kotlin.math.roundToInt

private fun Density.slotIndex(pos: Offset, width: Int, page: Int): Int {
    val padH = GridPadH.toPx()
    val padV = GridPadV.toPx()
    val cellW = (width - 2 * padH) / GridColumns
    val rowH = (CellHeight + RowGap).toPx()
    val col = ((pos.x - padH) / cellW).toInt().coerceIn(0, GridColumns - 1)
    val row = ((pos.y - padV) / rowH).toInt().coerceIn(0, GridRows - 1)
    return page * AppsPerPage + row * GridColumns + col
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

@Composable
fun HomePager(state: HomeState, modifier: Modifier = Modifier) {
    val pages = paginate(state.items.toList())
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val density = LocalDensity.current
    var boxWidth by remember { mutableStateOf(0) }
    val edge by remember {
        derivedStateOf {
            val edgePx = with(density) { 32.dp.toPx() }
            when {
                state.dragging == null || boxWidth == 0 -> 0
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
                    state.moveTo(dragged.key, density.slotIndex(state.dragPos, boxWidth, pagerState.currentPage))
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
                        val index = slotIndex(pos, size.width, pagerState.currentPage)
                        val item = state.items.getOrNull(index)
                        if (item is FolderItem) {
                            state.openFolderId = item.id
                        } else if (state.editing && item == null) {
                            state.editing = false
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { pos ->
                            val index = slotIndex(pos, size.width, pagerState.currentPage)
                            val item = state.items.getOrNull(index)
                            if (item != null) {
                                state.clearDrag()
                                state.dragging = item
                                state.dragPos = pos
                                state.editing = true
                            }
                        },
                        onDrag = { change, amount ->
                            val dragged = state.dragging
                            if (dragged != null) {
                                change.consume()
                                state.dragPos = state.dragPos + amount
                                val index = slotIndex(state.dragPos, size.width, pagerState.currentPage)
                                val target = state.items.getOrNull(index)
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
                                    state.pendingMove = if (index != own) index else null
                                }
                            }
                        },
                        onDragEnd = { finishDrag(state) },
                        onDragCancel = { state.clearDrag() }
                    )
                }
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = state.dragging == null,
                modifier = Modifier.fillMaxWidth()
            ) { index ->
                AppGrid(items = pages[index], state = state)
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
        }
        PageDots(
            count = pages.size,
            current = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
        )
    }
}
