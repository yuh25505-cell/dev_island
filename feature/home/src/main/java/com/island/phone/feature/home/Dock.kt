package com.island.phone.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.icons.AppIcon

@Composable
fun Dock(apps: List<AppId>, modifier: Modifier = Modifier) {
    GlassSurface(
        modifier = modifier
            .fillMaxWidth()
            .height(98.dp),
        shape = RoundedCornerShape(40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            apps.forEach { app -> AppIcon(app = app) }
        }
    }
}
