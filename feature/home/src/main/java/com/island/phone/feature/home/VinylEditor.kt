package com.island.phone.feature.home

import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.glass.glassPressable
import com.island.phone.core.ui.theme.IslandTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun VinylEditor(state: HomeState) {
    val tokens = IslandTheme.folder
    val labelColor = IslandTheme.icon.label
    val store = VinylStore.get(LocalContext.current)
    val scope = rememberCoroutineScope()

    var shown by remember { mutableStateOf(false) }
    var closing by remember { mutableStateOf(false) }
    var backProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) { shown = true }
    val appear by animateFloatAsState(
        targetValue = if (shown && !closing) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = 420f),
        label = "vinylEditorAppear"
    )
    LaunchedEffect(closing) {
        if (closing) {
            delay(160)
            state.vinylOpen = false
        }
    }

    fun requestClose() {
        closing = true
    }

    PredictiveBackHandler(enabled = !closing) { progress ->
        try {
            progress.collect { backProgress = it.progress }
            requestClose()
        } catch (e: CancellationException) {
            backProgress = 0f
        }
    }

    val pickCover = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) scope.launch { store.importCover(uri) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawRect(tokens.scrim.copy(alpha = tokens.scrim.alpha * appear)) }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { requestClose() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    val s = (0.86f + 0.14f * appear) * (1f - 0.12f * backProgress)
                    scaleX = s
                    scaleY = s
                    alpha = appear
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { }
        ) {
            GlassSurface(
                modifier = Modifier.width(FolderPanelWidth),
                shape = RoundedCornerShape(36.dp),
                elevation = 18.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind { drawRect(tokens.panelFill) }
                        .padding(horizontal = FolderPanelPad, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VinylThumb(store.cover, labelColor) {
                        pickCover.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        VinylButton("恢复默认", labelColor, Modifier.weight(1f)) { store.reset() }
                        VinylButton("完成", labelColor, Modifier.weight(1f)) { requestClose() }
                    }
                }
            }
        }
    }
}

@Composable
private fun VinylThumb(
    image: androidx.compose.ui.graphics.ImageBitmap?,
    tint: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .glassPressable(onClick)
            .size(140.dp)
            .clip(CircleShape)
            .background(tint.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Canvas(modifier = Modifier.size(28.dp)) {
                val sw = 2.5.dp.toPx()
                drawLine(tint.copy(alpha = 0.6f), Offset(0f, size.height / 2f), Offset(size.width, size.height / 2f), sw)
                drawLine(tint.copy(alpha = 0.6f), Offset(size.width / 2f, 0f), Offset(size.width / 2f, size.height), sw)
            }
        }
    }
}

@Composable
private fun VinylButton(text: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    GlassSurface(
        modifier = modifier.glassPressable(onClick),
        shape = RoundedCornerShape(50),
        elevation = 4.dp
    ) {
        BasicText(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            style = TextStyle(color = color, fontSize = 15.sp, textAlign = TextAlign.Center)
        )
    }
}
