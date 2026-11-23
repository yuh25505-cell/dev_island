package com.islandphone.app.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme
import com.islandphone.app.core.theme.ThemeMode

@StringRes
private fun labelOf(mode: ThemeMode): Int = when (mode) {
    ThemeMode.FOLLOW_SYSTEM -> R.string.theme_follow_system
    ThemeMode.LIGHT -> R.string.theme_light
    ThemeMode.DARK -> R.string.theme_dark
}

@Composable
fun ThemeModeSelector(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = IslandTheme.colors
    val outer = RoundedCornerShape(26.dp)
    val inner = RoundedCornerShape(22.dp)

    Row(
        modifier = modifier
            .clip(outer)
            .background(colors.glassFill)
            .border(1.dp, colors.glassBorder, outer)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ThemeMode.entries.forEach { mode ->
            val isSelected = mode == selected
            Box(
                modifier = Modifier
                    .clip(inner)
                    .background(if (isSelected) colors.accentSoft else Color.Transparent)
                    .clickable { onSelect(mode) }
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(labelOf(mode)),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) colors.onBackground else colors.textSecondary
                )
            }
        }
    }
}
