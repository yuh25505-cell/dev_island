package com.island.phone.feature.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.icons.AppIcon

@Composable
fun Dock(apps: List<AppId>, modifier: Modifier = Modifier, editing: Boolean = false) {
    val launcher = LocalLauncher.current
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
            apps.forEach { app ->
                var bounds by remember(app) { mutableStateOf(Rect.Zero) }
                AppIcon(
                    app = app,
                    modifier = Modifier
                        .onGloballyPositioned { bounds = it.boundsInRoot() }
                        .pointerInput(app, editing) {
                            detectTapGestures { if (!editing) launcher.open(app, bounds) }
                        }
                )
            }
        }
    }
}
