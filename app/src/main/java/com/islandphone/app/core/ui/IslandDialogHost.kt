package com.islandphone.app.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.islandphone.app.core.theme.IslandTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DIALOG_EXIT_MILLIS = 200L

@Composable
internal fun IslandDialogHost(
    onDismissRequest: () -> Unit,
    dismissible: Boolean = true,
    fromBottom: Boolean = false,
    content: @Composable (dismiss: () -> Unit) -> Unit
) {
    val latestDismissRequest by rememberUpdatedState(onDismissRequest)
    val scope = rememberCoroutineScope()
    var shown by remember { mutableStateOf(false) }
    val dismiss: () -> Unit = {
        if (shown) {
            shown = false
            scope.launch {
                delay(DIALOG_EXIT_MILLIS)
                latestDismissRequest()
            }
        }
    }

    LaunchedEffect(Unit) { shown = true }

    Dialog(
        onDismissRequest = { if (dismissible) dismiss() },
        properties = DialogProperties(
            dismissOnBackPress = dismissible,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val scrimAlpha by animateFloatAsState(
            targetValue = if (shown) 0.45f else 0f,
            animationSpec = tween(200),
            label = "dialogScrim"
        )
        val enter: EnterTransition = if (fromBottom) {
            slideInVertically(animationSpec = spring(dampingRatio = 0.85f, stiffness = 400f)) { it } +
                fadeIn(tween(150))
        } else {
            scaleIn(animationSpec = spring(dampingRatio = 0.7f, stiffness = 500f), initialScale = 0.88f) +
                fadeIn(tween(150))
        }
        val exit: ExitTransition = if (fromBottom) {
            slideOutVertically(animationSpec = tween(200)) { it } + fadeOut(tween(150))
        } else {
            scaleOut(animationSpec = tween(150), targetScale = 0.92f) + fadeOut(tween(150))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { if (dismissible) dismiss() },
            contentAlignment = if (fromBottom) Alignment.BottomCenter else Alignment.Center
        ) {
            AnimatedVisibility(visible = shown, enter = enter, exit = exit) {
                Box(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {}
                ) {
                    content(dismiss)
                }
            }
        }
    }
}

@Composable
internal fun IslandDialogSurface(
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = IslandTheme.colors
    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.dialogSurface)
            .background(Brush.verticalGradient(listOf(colors.glassHighlight, Color.Transparent)))
            .border(1.dp, colors.glassBorder, shape),
        content = content
    )
}
