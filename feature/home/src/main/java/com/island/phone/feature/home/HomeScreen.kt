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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.island.phone.core.ui.glass.LocalBackdrop
import com.island.phone.core.ui.glass.PopupBackdrop
import com.island.phone.core.ui.glass.backdropSource
import com.island.phone.core.ui.glass.rememberBackdropState
import com.island.phone.core.ui.theme.IslandDesktopTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val backdrop = rememberBackdropState()
    val state = remember { HomeState(GridApps).also { LayoutStore.restore(context, it) } }
    val launcher = remember { LauncherState() }
    LaunchedEffect(state) {
        snapshotFlow { LayoutStore.encode(state) }.collect { LayoutStore.save(context, it) }
    }
    CompositionLocalProvider(
        LocalBackdrop provides backdrop,
        LocalLauncher provides launcher
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            IslandDesktopTheme {
                Wallpaper(modifier = Modifier.fillMaxSize().backdropSource(backdrop))
                HomePager(
                    state = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(if (state.isDraggingAny) 1f else 0f)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(top = 48.dp, bottom = 118.dp)
                )
                Dock(
                    state = state,
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
                val openFolder = state.openFolder
                if (openFolder != null) {
                    PopupBackdrop(modifier = Modifier.fillMaxSize()) {
                        FolderPopup(folder = openFolder, state = state)
                    }
                }
                if (state.photoCardOpen) {
                    PopupBackdrop(modifier = Modifier.fillMaxSize()) {
                        PhotoCardEditor(state = state)
                    }
                }
                if (state.vinylOpen) {
                    PopupBackdrop(modifier = Modifier.fillMaxSize()) {
                        VinylEditor(state = state)
                    }
                }
            }
            AppLauncher(launcher = launcher)
        }
    }
}
