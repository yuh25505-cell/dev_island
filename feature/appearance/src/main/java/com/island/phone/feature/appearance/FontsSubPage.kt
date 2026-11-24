package com.island.phone.feature.appearance

import android.widget.Toast
import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.glass.glassPressable
import com.island.phone.core.ui.shell.AppShell
import com.island.phone.core.ui.shell.HoldShellEdgeBack
import com.island.phone.core.ui.text.BasicText
import com.island.phone.core.ui.theme.IslandTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@Composable
internal fun FontsSubPage(store: AppearanceStore, onClose: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val labelColor = IslandTheme.icon.label
    val animate = store.pageAnimation
    var shown by remember { mutableStateOf(!animate) }
    var closing by remember { mutableStateOf(false) }
    var backProgress by remember { mutableStateOf(0f) }
    HoldShellEdgeBack()

    val pick = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            scope.launch {
                if (!store.importFont(uri)) {
                    Toast.makeText(context, "这个字体文件用不了", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(Unit) { shown = true }
    val appearAnimated by animateFloatAsState(
        targetValue = if (shown && !closing) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.9f, stiffness = 380f),
        label = "fontsPageAppear"
    )
    val appear = if (animate) appearAnimated else 1f
    LaunchedEffect(closing) {
        if (closing) {
            if (animate) delay(220)
            onClose()
        }
    }

    PredictiveBackHandler(enabled = !closing) { progress ->
        try {
            progress.collect { if (animate) backProgress = it.progress }
            closing = true
        } catch (e: CancellationException) {
            backProgress = 0f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationX = if (animate) {
                    (1f - appear) * size.width + backProgress * size.width * 0.08f
                } else {
                    0f
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
    ) {
        AppShell(title = "全局字体", onBack = { closing = true }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 24.dp)
            ) {
                SettingCard(title = "字体", titleColor = labelColor) {
                    FontRow(
                        title = "系统默认",
                        color = labelColor,
                        selected = store.activeFontId == null,
                        onDelete = null
                    ) { store.selectFont(null) }
                    store.fonts.forEach { entry ->
                        FontRow(
                            title = entry.name,
                            color = labelColor,
                            selected = store.activeFontId == entry.id,
                            onDelete = { store.removeFont(entry.id) }
                        ) { store.selectFont(entry.id) }
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
    }
}

@Composable
private fun FontRow(
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
