package com.island.phone.feature.appearance

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.shell.AppShell
import com.island.phone.core.ui.shell.HoldShellEdgeBack
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

@Composable
internal fun SubPageScaffold(
    store: AppearanceStore,
    title: String,
    onClose: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val animate = store.pageAnimation
    var shown by remember { mutableStateOf(!animate) }
    var closing by remember { mutableStateOf(false) }
    var backProgress by remember { mutableStateOf(0f) }
    HoldShellEdgeBack()

    LaunchedEffect(Unit) { shown = true }
    val appearAnimated by animateFloatAsState(
        targetValue = if (shown && !closing) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.9f, stiffness = 380f),
        label = "subPageAppear"
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
        AppShell(title = title, onBack = { closing = true }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 24.dp),
                content = content
            )
        }
    }
}
