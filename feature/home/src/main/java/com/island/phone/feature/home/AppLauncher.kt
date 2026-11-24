package com.island.phone.feature.home

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.apps.LocalAppPages
import com.island.phone.core.ui.icons.AppIcon
import com.island.phone.core.ui.shell.AppShell
import com.island.phone.core.ui.shell.LocalShellOverlay
import com.island.phone.core.ui.shell.ShellOverlayState
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.roundToInt

class LauncherState {
    var app by mutableStateOf<AppId?>(null)
    var origin by mutableStateOf(Rect.Zero)

    fun open(app: AppId, origin: Rect) {
        if (this.app != null) return
        this.origin = origin
        this.app = app
    }

    fun close() {
        app = null
    }
}

val LocalLauncher = staticCompositionLocalOf { LauncherState() }

@Composable
fun AppLauncher(launcher: LauncherState) {
    val app = launcher.app
    if (app != null) {
        AppLauncherLayer(app = app, launcher = launcher)
    }
}

private fun smooth(t: Float): Float {
    val x = t.coerceIn(0f, 1f)
    return x * x * (3f - 2f * x)
}

@Composable
private fun AppLauncherLayer(app: AppId, launcher: LauncherState) {
    val density = LocalDensity.current
    val animate = AppearanceStore.get(LocalContext.current).pageAnimation
    val origin = launcher.origin
    val open = remember(app) { Animatable(if (animate) 0f else 1f) }
    var closing by remember(app) { mutableStateOf(false) }
    var backTarget by remember(app) { mutableStateOf(0f) }
    val overlayState = remember(app) { ShellOverlayState() }
    val backAnimated by animateFloatAsState(
        targetValue = backTarget,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "appBack"
    )
    val back = if (animate) backAnimated else 0f
    var rootOrigin by remember { mutableStateOf(Offset.Zero) }
    var area by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(app) {
        if (animate) {
            open.animateTo(1f, spring(dampingRatio = 0.9f, stiffness = 300f))
        } else {
            open.snapTo(1f)
        }
    }
    LaunchedEffect(closing) {
        if (closing) {
            backTarget = 0f
            if (animate) {
                open.animateTo(0f, spring(dampingRatio = 1f, stiffness = 420f))
            } else {
                open.snapTo(0f)
            }
            launcher.close()
        }
    }

    PredictiveBackHandler(enabled = !closing) { progress ->
        try {
            progress.collect { backTarget = it.progress }
            closing = true
        } catch (e: CancellationException) {
            backTarget = 0f
        }
    }

    fun frameAt(p: Float): Rect {
        val fromLeft = origin.left - rootOrigin.x
        val fromTop = origin.top - rootOrigin.y
        val fromRight = origin.right - rootOrigin.x
        val fromBottom = origin.bottom - rootOrigin.y
        return Rect(
            lerp(fromLeft, 0f, p),
            lerp(fromTop, 0f, p),
            lerp(fromRight, area.width.toFloat(), p),
            lerp(fromBottom, area.height.toFloat(), p)
        )
    }

    val fromRadius = origin.width * 0.22f
    val backRadius = with(density) { 28.dp.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                rootOrigin = it.positionInRoot()
                area = it.size
            }
    ) {
        if (area.width > 0 && area.height > 0) {
            val areaW = with(density) { area.width.toDp() }
            val areaH = with(density) { area.height.toDp() }
            Box(
                modifier = Modifier
                    .offset {
                        val f = frameAt(open.value.coerceIn(0f, 1f))
                        IntOffset(f.left.roundToInt(), f.top.roundToInt())
                    }
                    .layout { measurable, _ ->
                        val f = frameAt(open.value.coerceIn(0f, 1f))
                        val w = f.width.roundToInt().coerceAtLeast(1)
                        val h = f.height.roundToInt().coerceAtLeast(1)
                        val placeable = measurable.measure(Constraints.fixed(w, h))
                        layout(w, h) { placeable.place(0, 0) }
                    }
                    .graphicsLayer {
                        val p = open.value.coerceIn(0f, 1f)
                        val s = 1f - 0.08f * back
                        scaleX = s
                        scaleY = s
                        shape = RoundedCornerShape(lerp(fromRadius, 0f, p) + backRadius * back)
                        clip = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .requiredSize(areaW, areaH)
                        .graphicsLayer { alpha = smooth((open.value - 0.15f) / 0.5f) }
                ) {
                    CompositionLocalProvider(LocalShellOverlay provides overlayState) {
                        AppShellPage(app = app, onBack = { closing = true })
                    }
                }
                Box(
                    modifier = Modifier
                        .requiredSize(60.dp)
                        .graphicsLayer {
                            val p = open.value.coerceIn(0f, 1f)
                            val s = frameAt(p).width / size.width
                            scaleX = s
                            scaleY = s
                            alpha = 1f - (p / 0.3f).coerceIn(0f, 1f)
                        }
                ) {
                    AppIcon(app)
                }
            }
        }
        if (!closing && overlayState.count == 0) {
            EdgeStrip(
                modifier = Modifier.align(Alignment.CenterStart),
                sign = 1,
                onProgress = { backTarget = it },
                onCommit = { closing = true }
            )
            EdgeStrip(
                modifier = Modifier.align(Alignment.CenterEnd),
                sign = -1,
                onProgress = { backTarget = it },
                onCommit = { closing = true }
            )
        }
    }
}

@Composable
private fun EdgeStrip(
    modifier: Modifier,
    sign: Int,
    onProgress: (Float) -> Unit,
    onCommit: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(18.dp)
            .pointerInput(Unit) {
                var total = 0f
                var last = 0f
                val threshold = 140.dp.toPx()
                detectHorizontalDragGestures(
                    onDragStart = {
                        total = 0f
                        last = 0f
                    },
                    onHorizontalDrag = { change, amount ->
                        change.consume()
                        total += amount
                        last = (total * sign / threshold).coerceIn(0f, 1f)
                        onProgress(last)
                    },
                    onDragEnd = {
                        if (last >= 0.4f) onCommit() else onProgress(0f)
                    },
                    onDragCancel = { onProgress(0f) }
                )
            }
    )
}

@Composable
private fun AppShellPage(app: AppId, onBack: () -> Unit) {
    val page = LocalAppPages.current[app]
    if (page != null) {
        page(onBack)
    } else {
        AppShell(title = app.label, onBack = onBack) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AppIcon(app = app, size = 96.dp)
            }
        }
    }
}
