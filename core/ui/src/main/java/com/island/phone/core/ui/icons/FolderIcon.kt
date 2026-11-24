package com.island.phone.core.ui.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.appearance.drawImageCover
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.text.BasicText
import com.island.phone.core.ui.theme.IslandTheme

@Composable
fun FolderIcon(apps: List<AppId>, modifier: Modifier = Modifier, size: Dp = 60.dp) {
    val icon = IslandTheme.icon
    val folder = IslandTheme.folder
    val store = AppearanceStore.get(LocalContext.current)
    val gap = size * 0.04f
    GlassSurface(
        modifier = modifier.size(size),
        shape = RoundedCornerShape(percent = 22),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind { drawRect(icon.tileTint) }
                .padding(size * 0.11f),
            verticalArrangement = Arrangement.spacedBy(gap)
        ) {
            for (r in 0 until 3) {
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(gap)
                ) {
                    for (c in 0 until 3) {
                        val app = apps.getOrNull(r * 3 + c)
                        val custom = if (app == null) null else store.iconOf(app)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .then(
                                    if (app == null) {
                                        Modifier
                                    } else {
                                        Modifier
                                            .clip(RoundedCornerShape(percent = 26))
                                            .drawBehind {
                                                drawRect(folder.miniTile)
                                                if (custom != null) {
                                                    drawImageCover(custom)
                                                } else {
                                                    drawAppGlyph(app, icon.glyph)
                                                }
                                            }
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FolderIconWithLabel(
    name: String,
    apps: List<AppId>,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
) {
    val tokens = IslandTheme.icon
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        FolderIcon(apps = apps, size = size)
        Spacer(Modifier.height(6.dp))
        BasicText(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                color = tokens.label,
                fontSize = 11.sp,
                shadow = Shadow(tokens.labelShadow, Offset(0f, 1f), 3f)
            )
        )
    }
}
