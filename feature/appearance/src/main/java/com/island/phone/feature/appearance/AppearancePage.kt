package com.island.phone.feature.appearance

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.glass.GlassSettings
import com.island.phone.core.ui.shell.AppShell
import com.island.phone.core.ui.text.BasicText
import com.island.phone.core.ui.theme.IslandTheme
import com.island.phone.core.ui.theme.ThemeMode
import kotlinx.coroutines.launch

@Composable
fun AppearancePage(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = AppearanceStore.get(context)
    val glass = GlassSettings.get(context)
    val scope = rememberCoroutineScope()
    val labelColor = IslandTheme.icon.label
    var iconsOpen by remember { mutableStateOf(false) }
    var fontsOpen by remember { mutableStateOf(false) }
    var emojiOpen by remember { mutableStateOf(false) }
    var glassOpen by remember { mutableStateOf(false) }

    val pickWallpaper = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) scope.launch { store.importWallpaper(uri) }
    }
    val launchWallpaperPicker = {
        pickWallpaper.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                SettingCard(title = "显示模式", titleColor = labelColor) {
                    ChoiceRow(
                        title = "跟随系统",
                        color = labelColor,
                        selected = store.themeMode == ThemeMode.FOLLOW_SYSTEM,
                        onDelete = null
                    ) { store.setThemeMode(ThemeMode.FOLLOW_SYSTEM) }
                    ChoiceRow(
                        title = "日间",
                        color = labelColor,
                        selected = store.themeMode == ThemeMode.DAY,
                        onDelete = null
                    ) { store.setThemeMode(ThemeMode.DAY) }
                    ChoiceRow(
                        title = "夜间",
                        color = labelColor,
                        selected = store.themeMode == ThemeMode.NIGHT,
                        onDelete = null
                    ) { store.setThemeMode(ThemeMode.NIGHT) }
                }
                Spacer(Modifier.height(16.dp))
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
                SettingCard(
                    title = "玻璃",
                    titleColor = labelColor,
                    action = {
                        MinusButton(
                            color = labelColor,
                            enabled = !glass.isDefault
                        ) { glass.reset() }
                    }
                ) {
                    EntryRow(
                        title = "液态玻璃",
                        color = labelColor
                    ) { glassOpen = true }
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
                        color = labelColor,
                        trailing = {
                            BasicText(
                                text = "Aa字",
                                style = TextStyle(color = labelColor.copy(alpha = 0.6f), fontSize = 15.sp)
                            )
                        }
                    ) { fontsOpen = true }
                }
                Spacer(Modifier.height(16.dp))
                SettingCard(
                    title = "表情",
                    titleColor = labelColor,
                    action = {
                        MinusButton(
                            color = labelColor,
                            enabled = store.hasEmojiFont
                        ) { store.resetEmoji() }
                    }
                ) {
                    EntryRow(
                        title = "表情字体",
                        color = labelColor,
                        trailing = {
                            BasicText(
                                text = "😀",
                                style = TextStyle(fontSize = 17.sp)
                            )
                        }
                    ) { emojiOpen = true }
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
        if (fontsOpen) {
            FontsSubPage(store = store, onClose = { fontsOpen = false })
        }
        if (emojiOpen) {
            EmojiSubPage(store = store, onClose = { emojiOpen = false })
        }
        if (glassOpen) {
            GlassSubPage(store = store, onClose = { glassOpen = false })
        }
    }
}
