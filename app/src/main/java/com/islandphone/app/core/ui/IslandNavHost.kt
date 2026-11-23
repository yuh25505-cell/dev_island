package com.islandphone.app.core.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun IslandNavHost(
    controller: IslandNavController,
    modifier: Modifier = Modifier,
    content: @Composable (route: String) -> Unit
) {
    val entries = controller.entries

    BackHandler(enabled = controller.canGoBack) { controller.back() }

    CompositionLocalProvider(LocalIslandNav provides controller) {
        Box(modifier = modifier.fillMaxSize()) {
            val lastIndex = entries.lastIndex
            entries.forEachIndexed { index, entry ->
                key(entry.id) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        content(entry.route)
                        if (index != lastIndex) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .pointerInput(Unit) { detectTapGestures { } }
                            )
                        }
                    }
                }
            }
        }
    }
}
