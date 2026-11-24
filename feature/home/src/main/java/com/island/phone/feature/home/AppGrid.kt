package com.island.phone.feature.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.icons.AppIconWithLabel

@Composable
fun AppGrid(apps: List<AppId>, state: HomeState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = GridPadH, vertical = GridPadV),
        verticalArrangement = Arrangement.spacedBy(RowGap)
    ) {
        apps.chunked(GridColumns).forEachIndexed { r, row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEachIndexed { c, app ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(CellHeight),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Jiggle(active = state.editing, seed = r * GridColumns + c) {
                            Box(modifier = Modifier.graphicsLayer {
                                alpha = if (state.dragging == app) 0f else 1f
                            }) {
                                AppIconWithLabel(app)
                            }
                        }
                    }
                }
                repeat(GridColumns - row.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}
