package com.island.phone.feature.home

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.glass.glassPressable
import com.island.phone.core.ui.icons.AppIconWithLabel
import com.island.phone.core.ui.theme.IslandTheme
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.roundToInt

@Composable
fun FolderPopup(folder: FolderItem, state: HomeState) {
    val tokens = IslandTheme.folder
    val labelColor = IslandTheme.icon.label
    val focus = LocalFocusManager.current
    val launcher = LocalLauncher.current
    val current by rememberUpdatedState(folder)

    var shown by remember { mutableStateOf(false) }
    var closing by remember { mutableStateOf(false) }
    var backProgress by remember { mutableStateOf(0f) }
    var rootOrigin by remember { mutableStateOf(Offset.Zero) }
    var gridOrigin by remember { mutableStateOf(Offset.Zero) }
    var panelRect by remember { mutableStateOf(Rect.Zero) }
    var dragApp by remember { mutableStateOf<AppId?>(null) }
    var dragPos by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(Unit) { shown = true }
    val appear by animateFloatAsState(
        targetValue = if (shown && !closing) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = 420f),
        label = "folderAppear"
    )
    LaunchedEffect(closing) {
        if (closing) {
            delay(160)
            state.closeFolder()
        }
    }

    fun requestClose() {
        focus.clearFocus()
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { rootOrigin = it.positionInRoot() }
            .drawBehind { drawRect(tokens.scrim.copy(alpha = tokens.scrim.alpha * appear)) }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { requestClose() }
            .windowInsetsPadding(WindowInsets.ime),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 48.dp)
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
                modifier = Modifier
                    .width(FolderPanelWidth)
                    .onGloballyPositioned { panelRect = it.boundsInRoot() },
                shape = RoundedCornerShape(36.dp),
                elevation = 18.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind { drawRect(tokens.panelFill) }
                        .padding(horizontal = FolderPanelPad, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BasicTextField(
                        value = folder.name,
                        onValueChange = { state.renameFolder(folder.id, it) },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = tokens.title,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        ),
                        cursorBrush = SolidColor(tokens.cursor),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focus.clearFocus() }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    val rows = ((current.apps.size + FolderColumns - 1) / FolderColumns).coerceAtLeast(1)
                    Box(
                        modifier = Modifier
                            .size(FolderCellW * FolderColumns, FolderCellH * rows)
                            .onGloballyPositioned { gridOrigin = it.positionInRoot() }
                            .pointerInput(Unit) {
                                detectTapGestures { pos ->
                                    if (!state.editing) {
                                        val cw = FolderCellW.toPx()
                                        val ch = FolderCellH.toPx()
                                        val col = (pos.x / cw).toInt()
                                        val row = (pos.y / ch).toInt()
                                        val app = if (col in 0 until FolderColumns) {
                                            current.apps.getOrNull(row * FolderColumns + col)
                                        } else {
                                            null
                                        }
                                        if (app != null) {
                                            val icon = 60.dp.toPx()
                                            val left = gridOrigin.x + col * cw + (cw - icon) / 2f
                                            val top = gridOrigin.y + row * ch
                                            launcher.open(app, Rect(left, top, left + icon, top + icon))
                                        }
                                    }
                                }
                            }
                            .pointerInput(Unit) {
                                detectDragGesturesAfterLongPress(
                                    onDragStart = { pos ->
                                        val col = (pos.x / FolderCellW.toPx()).toInt()
                                        val row = (pos.y / FolderCellH.toPx()).toInt()
                                        val app = if (col in 0 until FolderColumns) {
                                            current.apps.getOrNull(row * FolderColumns + col)
                                        } else {
                                            null
                                        }
                                        if (app != null) {
                                            dragApp = app
                                            dragPos = pos
                                        }
                                    },
                                    onDrag = { change, amount ->
                                        if (dragApp != null) {
                                            change.consume()
                                            dragPos = dragPos + amount
                                        }
                                    },
                                    onDragEnd = {
                                        val app = dragApp
                                        dragApp = null
                                        if (app != null && !panelRect.contains(gridOrigin + dragPos)) {
                                            state.pullOut(current.id, app)
                                        }
                                    },
                                    onDragCancel = { dragApp = null }
                                )
                            }
                    ) {
                        Column {
                            current.apps.chunked(FolderColumns).forEachIndexed { r, rowApps ->
                                Row {
                                    rowApps.forEachIndexed { c, app ->
                                        Box(
                                            modifier = Modifier.size(FolderCellW, FolderCellH),
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            Jiggle(active = state.editing, seed = r * FolderColumns + c) {
                                                Box(modifier = Modifier.graphicsLayer {
                                                    alpha = if (dragApp == app) 0f else 1f
                                                }) {
                                                    AppIconWithLabel(app)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    GlassSurface(
                        modifier = Modifier.glassPressable { state.dissolveFolder(folder.id) },
                        shape = RoundedCornerShape(50),
                        elevation = 4.dp
                    ) {
                        BasicText(
                            text = "解散文件夹",
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                            style = TextStyle(color = labelColor, fontSize = 15.sp)
                        )
                    }
                }
            }
        }
        val dragged = dragApp
        if (dragged != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset {
                        val p = gridOrigin - rootOrigin + dragPos
                        IntOffset(
                            (p.x - 36.dp.toPx()).roundToInt(),
                            (p.y - 42.dp.toPx()).roundToInt()
                        )
                    }
                    .size(72.dp, 84.dp)
                    .graphicsLayer {
                        scaleX = 1.12f
                        scaleY = 1.12f
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                AppIconWithLabel(dragged)
            }
        }
    }
}
