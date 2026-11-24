package com.island.phone.feature.appearance

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.glass.glassPressable

@Composable
internal fun PillButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    GlassSurface(
        modifier = modifier
            .graphicsLayer { alpha = if (enabled) 1f else 0.45f }
            .glassPressable { if (enabled) onClick() },
        shape = RoundedCornerShape(50),
        elevation = 4.dp
    ) {
        BasicText(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            style = TextStyle(color = color, fontSize = 15.sp, textAlign = TextAlign.Center)
        )
    }
}

@Composable
internal fun SectionTitle(text: String, color: Color) {
    BasicText(
        text = text,
        modifier = Modifier.padding(start = 4.dp, top = 16.dp, bottom = 10.dp),
        style = TextStyle(color = color, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    )
}
