package com.islandphone.app.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.islandphone.app.core.theme.IslandTheme

@Composable
fun GlassSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colors = IslandTheme.colors
    val trackWidth = 52.dp
    val trackHeight = 32.dp
    val thumbSize = 26.dp
    val inset = 3.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - inset * 2 else 0.dp,
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 520f),
        label = "switchThumb"
    )
    val trackColor by animateColorAsState(
        targetValue = if (checked) colors.accent else colors.glassFill,
        label = "switchTrack"
    )

    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.4f)
            .size(trackWidth, trackHeight)
            .clip(CircleShape)
            .background(trackColor)
            .background(Brush.verticalGradient(listOf(colors.glassHighlight, androidx.compose.ui.graphics.Color.Transparent)))
            .border(1.dp, colors.glassBorder, CircleShape)
            .toggleable(
                value = checked,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            )
            .padding(inset)
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(colors.backgroundElevated)
                .background(Brush.verticalGradient(listOf(colors.glassEdgeHighlight.copy(alpha = 0.75f), androidx.compose.ui.graphics.Color.Transparent)))
                .border(1.dp, colors.glassBorder, CircleShape)
        )
    }
}
