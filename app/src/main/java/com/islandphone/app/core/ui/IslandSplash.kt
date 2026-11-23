package com.islandphone.app.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.islandphone.app.core.theme.IslandTheme
import kotlinx.coroutines.delay

@Composable
fun IslandSplash(onFinished: () -> Unit) {
    val colors = IslandTheme.colors
    val time = remember { Animatable(0f) }
    val cover = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        time.animateTo(1f, tween(SPLASH_TOTAL_MS, easing = LinearEasing))
        delay(900)
        cover.animateTo(0f, tween(480, easing = FastOutSlowInEasing))
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = cover.value
                val s = 1f + (1f - cover.value) * 0.04f
                scaleX = s
                scaleY = s
            }
            .background(colors.background)
            .pointerInput(Unit) { detectTapGestures { } }
    ) {
        SplashScene(time = time, modifier = Modifier.fillMaxSize())
        SplashCurtain(time = time, modifier = Modifier.fillMaxSize())
        SplashMessage(time = time, modifier = Modifier.fillMaxSize())
    }
}
