package com.island.phone.feature.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun Jiggle(
    active: Boolean,
    seed: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val angle: State<Float>? = if (active) {
        val transition = rememberInfiniteTransition(label = "jiggle")
        transition.animateFloat(
            initialValue = -1.6f,
            targetValue = 1.6f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 105 + (seed % 4) * 14, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "jiggleAngle"
        )
    } else {
        null
    }
    Box(modifier = modifier.graphicsLayer { rotationZ = angle?.value ?: 0f }) {
        content()
    }
}
