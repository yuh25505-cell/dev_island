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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.icons.AppIconWithLabel
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

@Composable
fun HomePager(state: HomeState, modifier: Modifier = Modifier) {
    val pages = paginate(state.apps.toList())
    val pagerState = rememberPagerState(pageCount = { pages.size })
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures { pos ->
                        val index = slotIndex(pos, size.width, pagerState.currentPage)
                        if (state.editing && index >= state.apps.size) {
                            state.editing = false
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { pos ->
                            val index = slotIndex(pos, size.width, pagerState.currentPage)
                            if (index < state.apps.size) {
                                state.dragging = state.apps[index]
                                state.dragPos = pos
                                state.editing = true
                            }
                        },
                        onDrag = { change, amount ->
                            val app = state.dragging
                            if (app != null) {
                                change.consume()
                                state.dragPos = state.dragPos + amount
                                state.moveTo(
                                    app,
                                    slotIndex(state.dragPos, size.width, pagerState.currentPage)
                                )
                            }
                        },
                        onDragEnd = { state.dragging = null },
                        onDragCancel = { state.dragging = null }
                    )
                }
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = state.dragging == null,
                modifier = Modifier.fillMaxWidth()
            ) { index ->
                AppGrid(apps = pages[index], state = state)
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
                    AppIconWithLabel(dragged)
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
