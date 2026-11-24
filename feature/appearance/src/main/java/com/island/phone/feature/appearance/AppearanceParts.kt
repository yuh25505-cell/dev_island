package com.island.phone.feature.appearance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.glass.glassPressable
import com.island.phone.core.ui.shell.IslandWallpaper

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

@Composable
internal fun SettingCard(
    title: String,
    titleColor: Color,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    GlassSurface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 14.dp, top = 12.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 34.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BasicText(
                    text = title,
                    style = TextStyle(
                        color = titleColor.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                if (action != null) action()
            }
            content()
        }
    }
}

@Composable
internal fun MinusButton(color: Color, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .graphicsLayer { alpha = if (enabled) 1f else 0.4f }
            .size(width = 40.dp, height = 32.dp)
            .glassPressable { if (enabled) onClick() }
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.08f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(14.dp)) {
            drawLine(
                color = color,
                start = Offset(0f, size.height / 2f),
                end = Offset(size.width, size.height / 2f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun Chevron(color: Color) {
    Canvas(modifier = Modifier.size(width = 8.dp, height = 14.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.15f, size.height * 0.1f)
            lineTo(size.width * 0.85f, size.height * 0.5f)
            lineTo(size.width * 0.15f, size.height * 0.9f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
internal fun EntryRow(
    title: String,
    color: Color,
    tile: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .glassPressable(onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tile()
        Spacer(Modifier.width(14.dp))
        BasicText(
            text = title,
            modifier = Modifier.weight(1f),
            style = TextStyle(color = color, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        )
        if (trailing != null) {
            trailing()
            Spacer(Modifier.width(10.dp))
        }
        Chevron(color.copy(alpha = 0.35f))
    }
}

@Composable
private fun GlyphTile(top: Color, bottom: Color, glyph: DrawScope.() -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.verticalGradient(listOf(top, bottom)))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) { glyph() }
    }
}

@Composable
internal fun WallpaperTile() {
    GlyphTile(Color(0xFF8DB6FF), Color(0xFFB9E2FF)) { drawPictureGlyph() }
}

@Composable
internal fun IconsTile() {
    GlyphTile(Color(0xFFFF9FC0), Color(0xFFFFC8A6)) { drawGridGlyph() }
}

@Composable
internal fun WallpaperThumb(borderColor: Color) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = Modifier
            .size(width = 38.dp, height = 60.dp)
            .clip(shape)
            .border(1.dp, borderColor, shape)
    ) {
        IslandWallpaper(modifier = Modifier.fillMaxSize(), useCustom = true)
    }
}

private fun DrawScope.drawPictureGlyph() {
    val w = size.width
    val h = size.height
    val stroke = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(w * 0.2f, h * 0.22f),
        size = Size(w * 0.6f, h * 0.56f),
        cornerRadius = CornerRadius(5.dp.toPx()),
        style = stroke
    )
    drawCircle(color = Color.White, radius = w * 0.055f, center = Offset(w * 0.38f, h * 0.4f))
    val mountain = Path().apply {
        moveTo(w * 0.24f, h * 0.7f)
        lineTo(w * 0.42f, h * 0.55f)
        lineTo(w * 0.53f, h * 0.64f)
        lineTo(w * 0.62f, h * 0.54f)
        lineTo(w * 0.76f, h * 0.68f)
    }
    drawPath(path = mountain, color = Color.White, style = stroke)
}

private fun DrawScope.drawGridGlyph() {
    val w = size.width
    val h = size.height
    val cell = Size(w * 0.22f, w * 0.22f)
    val radius = CornerRadius(w * 0.06f)
    listOf(0.27f, 0.51f).forEach { x ->
        listOf(0.27f, 0.51f).forEach { y ->
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * x, h * y),
                size = cell,
                cornerRadius = radius
            )
        }
    }
}
