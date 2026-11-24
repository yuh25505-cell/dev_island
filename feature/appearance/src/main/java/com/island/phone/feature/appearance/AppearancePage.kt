package com.island.phone.feature.appearance

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.glass.glassPressable
import com.island.phone.core.ui.icons.AppIconWithLabel
import com.island.phone.core.ui.shell.AppShell
import com.island.phone.core.ui.shell.IslandWallpaper
import com.island.phone.core.ui.theme.IslandTheme
import kotlinx.coroutines.launch

@Composable
fun AppearancePage(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = AppearanceStore.get(context)
    val scope = rememberCoroutineScope()
    val labelColor = IslandTheme.icon.label
    var editing by remember { mutableStateOf<AppId?>(null) }
    val apps = remember { AppId.entries.filter { !it.adminOnly } }

    val pickWallpaper = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) scope.launch { store.importWallpaper(uri) }
    }
    val launchWallpaperPicker = {
        pickWallpaper.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    AppShell(
        title = "外观",
        onBack = onBack,
        overlay = {
            val target = editing
            if (target != null) {
                IconEditorPopup(app = target, store = store, onClose = { editing = null })
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            SectionTitle("壁纸", labelColor)
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                GlassSurface(
                    modifier = Modifier
                        .width(150.dp)
                        .height(260.dp)
                        .glassPressable { launchWallpaperPicker() },
                    shape = RoundedCornerShape(28.dp),
                    elevation = 10.dp
                ) {
                    IslandWallpaper(modifier = Modifier.fillMaxSize())
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PillButton(
                    text = "更换壁纸",
                    color = labelColor,
                    modifier = Modifier.weight(1f)
                ) { launchWallpaperPicker() }
                PillButton(
                    text = "恢复默认",
                    color = labelColor,
                    modifier = Modifier.weight(1f),
                    enabled = store.wallpaper != null
                ) { store.resetWallpaper() }
            }

            SectionTitle("图标", labelColor)
            apps.chunked(4).forEach { rowApps ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowApps.forEach { app ->
                        Box(
                            modifier = Modifier
                                .width(72.dp)
                                .glassPressable { editing = app },
                            contentAlignment = Alignment.Center
                        ) {
                            AppIconWithLabel(app = app)
                        }
                    }
                    repeat(4 - rowApps.size) { Spacer(Modifier.width(72.dp)) }
                }
            }
            Spacer(Modifier.height(16.dp))
            PillButton(
                text = "全部图标恢复默认",
                color = labelColor,
                modifier = Modifier.fillMaxWidth(),
                enabled = store.hasCustomIcons
            ) { store.resetAllIcons() }
        }
    }
}
