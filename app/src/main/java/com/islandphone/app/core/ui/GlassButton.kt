package com.islandphone.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.islandphone.app.core.theme.IslandTheme

enum class GlassButtonStyle { Primary, Glass }

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GlassButtonStyle = GlassButtonStyle.Primary,
    enabled: Boolean = true
) {
    val colors = IslandTheme.colors
    val shape = RoundedCornerShape(22.dp)
    val primary = style == GlassButtonStyle.Primary
    val fill = if (primary) colors.accent else colors.glassFill
    val textColor = if (primary) colors.onAccent else colors.accent
    val borderColor = if (primary) Color.Transparent else colors.glassBorder

    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.4f)
            .glassClickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .clip(shape)
            .background(fill)
            .border(1.dp, borderColor, shape)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = textColor
        )
    }
}
