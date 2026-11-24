package com.island.phone.core.ui.shell

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.appearance.AppearanceStore
import com.island.phone.core.ui.appearance.drawImageCover
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.glass.glassPressable
import com.island.phone.core.ui.theme.IslandTheme

@Composable
fun IslandWallpaper(modifier: Modifier = Modifier, useCustom: Boolean = false) {
    val tokens = IslandTheme.wallpaper
    val dark = IslandTheme.dark
    val store = AppearanceStore.get(LocalContext.current)
    val custom = if (useCustom) store.wallpaper else null
    Box(
        modifier = modifier.drawBehind {
            if (custom != null) {
                drawImageCover(custom)
                if (dark) drawRect(Color.Black.copy(alpha = 0.3f))
            } else {
                drawRect(Brush.verticalGradient(listOf(tokens.top, tokens.bottom)))
                val base = size.minDimension
                blob(tokens.blobA, Offset(size.width * 0.2f, size.height * 0.22f), base * 0.75f)
                blob(tokens.blobB, Offset(size.width * 0.85f, size.height * 0.5f), base * 0.8f)
                blob(tokens.blobC, Offset(size.width * 0.3f, size.height * 0.85f), base * 0.7f)
            }
        }
    )
}

private fun DrawScope.blob(color: Color, center: Offset, radius: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(color.copy(alpha = 0.85f), color.copy(alpha = 0f)),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
}

@Composable
fun AppShell(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {},
) {
    val label = IslandTheme.icon.label
    Box(modifier = modifier.fillMaxSize()) {
        IslandWallpaper(modifier = Modifier.fillMaxSize())
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(top = 56.dp),
            content = content
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(56.dp)
                .padding(horizontal = 16.dp)
        ) {
            GlassSurface(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .glassPressable(onBack),
                shape = CircleShape,
                elevation = 4.dp
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val chevron = Path().apply {
                        moveTo(w * 0.58f, h * 0.3f)
                        lineTo(w * 0.4f, h * 0.5f)
                        lineTo(w * 0.58f, h * 0.7f)
                    }
                    drawPath(
                        path = chevron,
                        color = label,
                        style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
            BasicText(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 56.dp),
                style = TextStyle(
                    color = label,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        Box(modifier = Modifier.fillMaxSize(), content = overlay)
    }
}
