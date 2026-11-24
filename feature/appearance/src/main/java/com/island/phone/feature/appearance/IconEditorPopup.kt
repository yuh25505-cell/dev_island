package com.island.phone.feature.appearance

import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.appearance.rememberPageAnimation
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.glass.OwnBackdrop
import com.island.phone.core.ui.glass.glassPressable
import com.island.phone.core.ui.icons.AppIcon
import com.island.phone.core.ui.shell.HoldShellEdgeBack
import com.island.phone.core.ui.text.BasicText
import com.island.phone.core.ui.theme.IslandTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun IconEditorPopup(app: AppId, store: AppearanceStore, onClose: () -> Unit) {
    val tokens = IslandTheme.folder
    val labelColor = IslandTheme.icon.label
    val scope = rememberCoroutineScope()
    val animate = rememberPageAnimation()
    HoldShellEdgeBack()

    var shown by remember { mutableStateOf(!animate) }
    var closing by remember { mutableStateOf(false) }
    var backProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) { shown = true }
    val appearAnimated by animateFloatAsState(
        targetValue = if (shown && !closing) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = 420f),
        label = "iconEditorAppear"
    )
    val appear = if (animate) appearAnimated else if (closing) 0f else 1f
    LaunchedEffect(closing) {
        if (closing) {
            if (animate) delay(160)
            onClose()
        }
    }

    fun requestClose() {
        closing = true
    }

    PredictiveBackHandler(enabled = !closing) { progress ->
        try {
            progress.collect { if (animate) backProgress = it.progress }
            requestClose()
        } catch (e: CancellationException) {
            backProgress = 0f
        }
    }

    val pick = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) scope.launch { store.importIcon(app, uri) }
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
                modifier = Modifier.width(280.dp),
                shape = RoundedCornerShape(36.dp),
                elevation = 18.dp
            ) {
                OwnBackdrop(
                    modifier = Modifier.fillMaxWidth(),
                    source = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .drawBehind { drawRect(tokens.panelFill) }
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.glassPressable {
                                pick.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        ) {
                            AppIcon(app = app, size = 96.dp)
                        }
                        Spacer(Modifier.height(14.dp))
                        BasicText(
                            text = app.label,
                            style = TextStyle(
                                color = labelColor,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(Modifier.height(20.dp))
                        PillButton(
                            text = "更换图片",
                            color = labelColor,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            pick.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            PillButton(
                                text = "恢复默认",
                                color = labelColor,
                                modifier = Modifier.weight(1f),
                                enabled = store.iconOf(app) != null
                            ) { store.resetIcon(app) }
                            PillButton(
                                text = "完成",
                                color = labelColor,
                                modifier = Modifier.weight(1f)
                            ) { requestClose() }
                        }
                    }
                }
            }
        }
    }
}
