package com.islandphone.app.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.islandphone.app.core.theme.IslandTheme

data class GlassBarItem(
    val label: String,
    val icon: (@Composable (tint: Color) -> Unit)? = null
)

@Composable
fun GlassBottomBar(
    items: List<GlassBarItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = IslandTheme.colors
    val outer = RoundedCornerShape(32.dp)
    val inner = RoundedCornerShape(26.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(outer)
            .background(colors.glassFill)
            .border(1.dp, colors.glassBorder, outer)
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            val pillColor by animateColorAsState(
                targetValue = if (selected) colors.accentSoft else Color.Transparent,
                label = "barPill"
            )
            val tint = if (selected) colors.accent else colors.textSecondary

            Column(
                modifier = Modifier
                    .weight(1f)
                    .glassClickable(role = Role.Tab) { onSelect(index) }
                    .clip(inner)
                    .background(pillColor)
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item.icon?.invoke(tint)
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = tint
                )
            }
        }
    }
}
