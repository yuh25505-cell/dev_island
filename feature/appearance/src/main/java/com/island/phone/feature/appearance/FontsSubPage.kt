package com.island.phone.feature.appearance

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.appearance.FontEntry
import com.island.phone.core.ui.glass.glassPressable
import com.island.phone.core.ui.text.BasicText
import com.island.phone.core.ui.theme.IslandTheme
import kotlinx.coroutines.launch

@Composable
internal fun FontsSubPage(store: AppearanceStore, onClose: () -> Unit) {
    FontListSubPage(
        store = store,
        pageTitle = "全局字体",
        cardTitle = "字体",
        fonts = store.fonts,
        activeId = store.activeFontId,
        onSelect = { store.selectFont(it) },
        onDelete = { store.removeFont(it) },
        onImport = { store.importFont(it) },
        onClose = onClose
    )
}

@Composable
internal fun EmojiSubPage(store: AppearanceStore, onClose: () -> Unit) {
    FontListSubPage(
        store = store,
        pageTitle = "表情字体",
        cardTitle = "表情",
        fonts = store.emojis,
        activeId = store.activeEmojiId,
        onSelect = { store.selectEmoji(it) },
        onDelete = { store.removeEmoji(it) },
        onImport = { store.importEmoji(it) },
        onClose = onClose
    )
}

@Composable
private fun FontListSubPage(
    store: AppearanceStore,
    pageTitle: String,
    cardTitle: String,
    fonts: List<FontEntry>,
    activeId: String?,
    onSelect: (String?) -> Unit,
    onDelete: (String) -> Unit,
    onImport: suspend (Uri) -> Boolean,
    onClose: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val labelColor = IslandTheme.icon.label

    val pick = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            scope.launch {
                if (!onImport(uri)) {
                    Toast.makeText(context, "这个字体文件用不了", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SubPageScaffold(store = store, title = pageTitle, onClose = onClose) {
        SettingCard(title = cardTitle, titleColor = labelColor) {
            ChoiceRow(
                title = "系统默认",
                color = labelColor,
                selected = activeId == null,
                onDelete = null
            ) { onSelect(null) }
            fonts.forEach { entry ->
                ChoiceRow(
                    title = entry.name,
                    color = labelColor,
                    selected = activeId == entry.id,
                    onDelete = { onDelete(entry.id) }
                ) { onSelect(entry.id) }
            }
        }
        Spacer(Modifier.height(16.dp))
        PillButton(
            text = "添加字体",
            color = labelColor,
            modifier = Modifier.fillMaxWidth()
        ) { pick.launch(arrayOf("*/*")) }
    }
}

@Composable
internal fun ChoiceRow(
    title: String,
    color: Color,
    selected: Boolean,
    onDelete: (() -> Unit)?,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .glassPressable(onSelect)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
                style = TextStyle(color = color, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            )
            if (selected) {
                Spacer(Modifier.width(10.dp))
                CheckMark(color)
            }
        }
        if (onDelete != null) {
            Spacer(Modifier.width(10.dp))
            MinusButton(color = color, enabled = true, onClick = onDelete)
        }
    }
}

@Composable
private fun CheckMark(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.08f, size.height * 0.55f)
            lineTo(size.width * 0.38f, size.height * 0.85f)
            lineTo(size.width * 0.92f, size.height * 0.18f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}
