package com.islandphone.app.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun IslandSplash(onFinished: () -> Unit) {
    val colors = IslandTheme.colors
    val dot = remember { Animatable(0f) }
    val ring = remember { Animatable(0f) }
    val label = remember { Animatable(0f) }
    val cover = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        coroutineScope {
            launch { dot.animateTo(1f, spring(dampingRatio = 0.6f, stiffness = 350f)) }
            launch {
                delay(200)
                ring.animateTo(1f, tween(800, easing = FastOutSlowInEasing))
            }
            launch {
                delay(350)
                label.animateTo(1f, tween(450))
            }
        }
        delay(300)
        cover.animateTo(0f, tween(350))
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = cover.value }
            .background(Brush.verticalGradient(listOf(colors.background, colors.backgroundTint)))
            .pointerInput(Unit) { detectTapGestures { } }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val dotRadius = 36.dp.toPx()
            val radius = dotRadius * dot.value
            if (ring.value > 0f) {
                drawCircle(
                    color = colors.accent.copy(alpha = (1f - ring.value) * 0.5f),
                    radius = dotRadius + dotRadius * 2.2f * ring.value,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
            drawCircle(color = colors.accent, radius = radius)
            drawCircle(
                color = colors.glassHighlight,
                radius = radius * 0.35f,
                center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f)
            )
        }
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = colors.onBackground,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 140.dp)
                .graphicsLayer {
                    alpha = label.value
                    translationY = (1f - label.value) * 12.dp.toPx()
                }
        )
    }
}
