package com.islandphone.app.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.islandphone.app.core.theme.IslandTheme
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@Composable
fun IslandBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit
) {
    val colors = IslandTheme.colors

    IslandDialogHost(onDismissRequest = onDismiss, fromBottom = true) { dismiss ->
        val scope = rememberCoroutineScope()
        val offsetY = remember { Animatable(0f) }
        val dragState = rememberDraggableState { delta ->
            scope.launch { offsetY.snapTo((offsetY.value + delta).coerceAtLeast(0f)) }
        }

        IslandDialogSurface(
            shape = RoundedCornerShape(32.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, bottom = 10.dp)
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .draggable(
                        state = dragState,
                        orientation = Orientation.Vertical,
                        onDragStopped = { velocity ->
                            if (offsetY.value > 240f || velocity > 1800f) {
                                dismiss()
                            } else {
                                offsetY.animateTo(0f, spring())
                            }
                        }
                    )
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 5.dp)
                        .clip(CircleShape)
                        .background(colors.textSecondary.copy(alpha = 0.4f))
                )
            }
            Column(
                modifier = Modifier
                    .heightIn(max = 640.dp)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                content(dismiss)
            }
        }
    }
}
