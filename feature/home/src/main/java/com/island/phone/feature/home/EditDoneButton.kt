package com.island.phone.feature.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.glass.glassPressable
import com.island.phone.core.ui.theme.IslandTheme

@Composable
fun EditDoneButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    GlassSurface(
        modifier = modifier.glassPressable(onClick),
        shape = RoundedCornerShape(50),
        elevation = 4.dp
    ) {
        BasicText(
            text = "完成",
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
            style = TextStyle(color = IslandTheme.icon.label, fontSize = 15.sp)
        )
    }
}
