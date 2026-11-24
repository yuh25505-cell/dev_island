package com.island.phone.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.icons.AppIconWithLabel

@Composable
fun AppGrid(apps: List<AppId>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        apps.chunked(GridColumns).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { app ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {
                        AppIconWithLabel(app)
                    }
                }
                repeat(GridColumns - row.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}
