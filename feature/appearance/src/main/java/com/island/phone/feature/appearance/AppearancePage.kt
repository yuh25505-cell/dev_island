package com.island.phone.feature.appearance

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.shell.AppShell
import com.island.phone.core.ui.theme.IslandTheme
import kotlinx.coroutines.launch

@Composable
fun AppearancePage(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = AppearanceStore.get(context)
    val scope = rememberCoroutineScope()
    val labelColor = IslandTheme.icon.label
    var iconsOpen by remember { mutableStateOf(false) }

    val pickWallpaper = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) scope.launch { store.importWallpaper(uri) }
    }
    val launchWallpaperPicker = {
        pickWallpaper.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    val pickFont = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            scope.launch {
                if (!store.importFont(uri)) {
                    Toast.makeText(context, "这个字体文件用不了", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppShell(title = "外观", onBack = onBack) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 24.dp)
            ) {
                SettingCard(
                    title = "壁纸",
                    titleColor = labelColor,
                    action = {
                        MinusButton(
                            color = labelColor,
                            enabled = store.wallpaper != null
                        ) { store.resetWallpaper() }
                    }
                ) {
                    EntryRow(
                        title = "主页背景",
                        color = labelColor,
                        trailing = { WallpaperThumb(borderColor = labelColor.copy(alpha = 0.15f)) }
                    ) { launchWallpaperPicker() }
                }
                Spacer(Modifier.height(16.dp))
                SettingCard(title = "图标", titleColor = labelColor) {
                    EntryRow(
                        title = "应用图标",
                        color = labelColor
                    ) { iconsOpen = true }
                }
                Spacer(Modifier.height(16.dp))
                SettingCard(
                    title = "字体",
                    titleColor = labelColor,
                    action = {
                        MinusButton(
                            color = labelColor,
                            enabled = store.hasCustomFont
                        ) { store.resetFont() }
                    }
                ) {
                    EntryRow(
                        title = "全局字体",
                        color = labelColor
                    ) { pickFont.launch(arrayOf("*/*")) }
                }
                Spacer(Modifier.height(16.dp))
                SettingCard(title = "动画", titleColor = labelColor) {
                    SwitchRow(
                        title = "进出与返回动画",
                        color = labelColor,
                        checked = store.pageAnimation
                    ) { store.setPageAnimation(it) }
                }
            }
        }
        if (iconsOpen) {
            IconsSubPage(store = store, onClose = { iconsOpen = false })
        }
    }
}
