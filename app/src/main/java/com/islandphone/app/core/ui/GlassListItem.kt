package com.islandphone.app.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islandphone.app.core.theme.IslandTheme

@Composable
fun GlassListItem(
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    val colors = IslandTheme.colors
    val shape = RoundedCornerShape(22.dp)

    val base = if (onClick != null) {
        modifier.glassClickable(onClick = onClick)
    } else {
        modifier
    }

    Row(
        modifier = base
            .fillMaxWidth()
            .glassSurface(shape, elevation = if (onClick != null) 2.dp else 1.dp)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leading != null) {
            leading()
            Spacer(modifier = Modifier.width(14.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onBackground,
            modifier = Modifier.weight(1f)
        )
        if (trailing != null) {
            Spacer(modifier = Modifier.width(14.dp))
            trailing()
        }
    }
}
