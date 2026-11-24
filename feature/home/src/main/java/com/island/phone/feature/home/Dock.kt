package com.island.phone.feature.home

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.icons.AppIcon
import kotlin.math.abs

@Composable
fun Dock(state: HomeState, modifier: Modifier = Modifier) {
    val launcher = LocalLauncher.current
    val density = LocalDensity.current
    GlassSurface(
        modifier = modifier
            .fillMaxWidth()
            .height(98.dp)
            .onGloballyPositioned { state.dockRect = it.boundsInRoot() },
        shape = RoundedCornerShape(40.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { pos ->
                        if (!state.editing) {
                            val n = state.dock.size
                            val slot = state.dockSlotPx
                            val left = size.width / 2f - n * slot / 2f
                            val idx = ((pos.x - left) / slot).toInt()
                            if (pos.x >= left && idx in 0 until n) {
                                val icon = 60.dp.toPx()
                                val cx = left + (idx + 0.5f) * slot
                                val cy = size.height / 2f
                                if (abs(pos.x - cx) <= icon / 2f && abs(pos.y - cy) <= icon / 2f) {
                                    val gx = state.dockRect.left + cx
                                    val gy = state.dockRect.top + cy
                                    launcher.open(
                                        state.dock[idx],
                                        Rect(gx - icon / 2f, gy - icon / 2f, gx + icon / 2f, gy + icon / 2f)
                                    )
                                }
                            }
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { pos ->
                            val n = state.dock.size
                            val slot = state.dockSlotPx
                            val left = size.width / 2f - n * slot / 2f
                            val idx = ((pos.x - left) / slot).toInt()
                            val app = if (pos.x >= left && idx in 0 until n) state.dock[idx] else null
                            if (app != null) {
                                state.clearDrag()
                                state.dragging = AppItem(app)
                                state.dragPos = Offset(
                                    state.dockRect.left + pos.x,
                                    state.dockRect.top + pos.y
                                ) - state.pagerRoot
                                state.editing = true
                            }
                        },
                        onDrag = { change, amount ->
                            if (state.dragging != null) {
                                change.consume()
                                state.dragPos = state.dragPos + amount
                                onItemDragMoved(state)
                            }
                        },
                        onDragEnd = { finishDrag(state) },
                        onDragCancel = { state.clearDrag() }
                    )
                }
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            val slot = minOf(maxWidth / DockCapacity, 78.dp)
            val slotPx = with(density) { slot.toPx() }
            SideEffect { state.dockSlotPx = slotPx }
            Row(verticalAlignment = Alignment.CenterVertically) {
                state.dock.forEachIndexed { i, app ->
                    Box(
                        modifier = Modifier.size(slot, 98.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Jiggle(active = state.editing, seed = i) {
                            Box(modifier = Modifier.graphicsLayer {
                                alpha = if (state.isDragging(AppItem(app))) 0f else 1f
                            }) {
                                AppIcon(app = app)
                            }
                        }
                    }
                }
            }
        }
    }
}
