package com.island.phone.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.glass.LocalBackdrop
import com.island.phone.core.ui.glass.backdropSource
import com.island.phone.core.ui.glass.rememberBackdropState

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val backdrop = rememberBackdropState()
    val state = remember { HomeState(GridApps) }
    CompositionLocalProvider(LocalBackdrop provides backdrop) {
        Box(modifier = modifier.fillMaxSize()) {
            Wallpaper(modifier = Modifier.fillMaxSize().backdropSource(backdrop))
            HomePager(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(top = 48.dp, bottom = 118.dp)
            )
            Dock(
                apps = DockApps,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )
            if (state.editing) {
                EditDoneButton(
                    onClick = { state.editing = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(top = 6.dp, end = 16.dp)
                )
            }
        }
    }
}
