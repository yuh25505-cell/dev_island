package com.islandphone.app.core.ui

import androidx.activity.BackEventCompat
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

private const val UNDER_PAGE_SHIFT = 0.25f
private const val UNDER_PAGE_SCRIM = 0.12f

private val PageSpring = spring<Float>(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = 380f
)

private class IslandPageMotion(initial: Float) {
    val fraction = Animatable(initial)
    var sign: Float = 1f
}

@Composable
fun IslandNavHost(
    controller: IslandNavController,
    modifier: Modifier = Modifier,
    content: @Composable (route: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val motions = remember { mutableMapOf<Int, IslandPageMotion>() }
    val entries = controller.entries

    fun motionOf(entry: IslandNavEntry): IslandPageMotion =
        motions.getOrPut(entry.id) { IslandPageMotion(if (entry.animateIn) 1f else 0f) }

    LaunchedEffect(entries.size) {
        val alive = entries.map { it.id }.toSet()
        motions.keys.retainAll(alive)
    }

    val backRequestId = controller.backRequestId
    LaunchedEffect(backRequestId) {
        if (backRequestId != null) {
            val entry = entries.lastOrNull { it.id == backRequestId }
            if (entry != null) {
                val motion = motionOf(entry)
                motion.sign = 1f
                motion.fraction.animateTo(1f, PageSpring)
            }
            controller.removeTop(backRequestId)
        }
    }

    PredictiveBackHandler(enabled = entries.size > 1 && backRequestId == null) { events ->
        val topEntry = entries.last()
        val motion = motionOf(topEntry)
        try {
            events.collect { event ->
                motion.sign = if (event.swipeEdge == BackEventCompat.EDGE_RIGHT) -1f else 1f
                motion.fraction.snapTo(event.progress)
            }
            motion.fraction.animateTo(1f, PageSpring)
            controller.removeTop(topEntry.id)
        } catch (e: CancellationException) {
            scope.launch { motion.fraction.animateTo(0f, PageSpring) }
        }
    }

    CompositionLocalProvider(LocalIslandNav provides controller) {
        Box(modifier = modifier.fillMaxSize()) {
            val lastIndex = entries.lastIndex
            entries.forEachIndexed { index, entry ->
                key(entry.id) {
                    val motion = motionOf(entry)
                    val topMotion = motionOf(entries[lastIndex])
                    val isTop = index == lastIndex
                    val isUnderTop = index == lastIndex - 1

                    LaunchedEffect(Unit) {
                        if (entry.animateIn) motion.fraction.animateTo(0f, PageSpring)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = when {
                                    isTop -> size.width * motion.fraction.value * motion.sign
                                    isUnderTop -> -size.width * UNDER_PAGE_SHIFT *
                                        (1f - topMotion.fraction.value) * topMotion.sign
                                    else -> 0f
                                }
                            }
                    ) {
                        content(entry.route)
                        if (!isTop) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        alpha = if (isUnderTop) {
                                            UNDER_PAGE_SCRIM * (1f - topMotion.fraction.value)
                                        } else {
                                            0f
                                        }
                                    }
                                    .background(Color.Black)
                                    .pointerInput(Unit) { detectTapGestures { } }
                            )
                        }
                    }
                }
            }
        }
    }
}
