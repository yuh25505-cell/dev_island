package com.island.phone.core.ui.shell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class ShellOverlayState {
    var count by mutableIntStateOf(0)
}

val LocalShellOverlay = staticCompositionLocalOf { ShellOverlayState() }

@Composable
fun HoldShellEdgeBack() {
    val state = LocalShellOverlay.current
    DisposableEffect(state) {
        state.count++
        onDispose { state.count-- }
    }
}
