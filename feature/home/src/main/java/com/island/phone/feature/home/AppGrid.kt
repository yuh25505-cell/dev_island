package com.island.phone.feature.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.island.phone.core.ui.icons.AppIconWithLabel
import com.island.phone.core.ui.icons.FolderIconWithLabel

@Composable
fun HomeItemIcon(item: HomeItem) {
    when (item) {
        is AppItem -> AppIconWithLabel(item.app)
        is FolderItem -> FolderIconWithLabel(name = item.name, apps = item.apps)
    }
}

@Composable
fun AppGrid(items: List<HomeItem>, state: HomeState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = GridPadH, vertical = GridPadV),
        verticalArrangement = Arrangement.spacedBy(RowGap)
    ) {
        items.chunked(GridColumns).forEachIndexed { r, row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEachIndexed { c, item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(CellHeight),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Jiggle(active = state.editing, seed = r * GridColumns + c) {
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
                repeat(GridColumns - row.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}
