package com.island.phone.feature.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.icons.AppIconWithLabel
import com.island.phone.core.ui.icons.FolderIconWithLabel
import com.island.phone.core.ui.text.BasicText
import com.island.phone.core.ui.theme.IslandTheme
import kotlin.math.roundToInt

@Composable
fun HomeItemIcon(item: HomeItem) {
    when (item) {
        is AppItem -> AppIconWithLabel(item.app)
        is FolderItem -> FolderIconWithLabel(name = item.name, apps = item.apps)
    }
}

@Composable
fun AppGrid(page: Int, state: HomeState, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val iconTokens = IslandTheme.icon
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val widthPx = with(density) { maxWidth.roundToPx() }
        val cellW = (maxWidth - GridPadH * 2) / GridColumns
        state.placedOn(page).forEach { placed ->
            val item = placed.item
            key(item.key) {
                Box(
                    modifier = Modifier
                        .offset(
                            x = GridPadH + cellW * placed.col,
                            y = GridPadV + (CellHeight + RowGap) * placed.row
                        )
                        .size(cellW, CellHeight),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Jiggle(active = state.editing, seed = placed.row * GridColumns + placed.col) {
                        val mergeScale by animateFloatAsState(
                            targetValue = when {
                                state.hoverKey != item.key -> 1f
                                state.mergeReady -> 1.2f
                                else -> 1.08f
                            },
                            animationSpec = spring(dampingRatio = 0.6f, stiffness = 380f),
                            label = "mergeScale"
                        )
                        Box(modifier = Modifier.graphicsLayer {
                            alpha = if (state.isDragging(item)) 0f else 1f
                            scaleX = mergeScale
                            scaleY = mergeScale
                        }) {
                            HomeItemIcon(item)
                        }
                    }
                }
            }
        }
        state.widgets.filter { it.page == page }.forEach { w ->
            key(w.kind) {
                val rect = with(density) { widgetRect(w.kind, w.row, w.col, widthPx) }
                val rectW = with(density) { rect.w.toDp() }
                val rectH = with(density) { rect.h.toDp() }
                Column(
                    modifier = Modifier
                        .offset { IntOffset(rect.x.roundToInt(), rect.y.roundToInt()) }
                        .width(rectW)
                        .graphicsLayer {
                            alpha = if (state.draggingWidget == w.kind) 0.3f else 1f
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(rectW, rectH)) {
                        Jiggle(
                            active = state.editing,
                            seed = w.kind.ordinal,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            WidgetContent(w.kind, Modifier.fillMaxSize())
                        }
                    }
                    if (w.kind != WidgetKind.CLOCK) {
                        Spacer(Modifier.height(6.dp))
                        BasicText(
                            text = w.kind.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = iconTokens.label,
                                fontSize = 11.sp,
                                shadow = Shadow(iconTokens.labelShadow, Offset(0f, 1f), 3f)
                            )
                        )
                    }
                }
            }
        }
    }
}
