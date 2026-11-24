package com.island.phone.feature.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.theme.IslandTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PhotoCardWidget(modifier: Modifier = Modifier) {
    val store = PhotoCardStore.get(LocalContext.current)
    val label = IslandTheme.icon.label
    val soft = label.copy(alpha = 0.62f)
    val faint = label.copy(alpha = 0.3f)
    GlassSurface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        elevation = 10.dp
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val u = maxWidth.value
            val avatarSize = (u * 0.17f).dp
            Column(modifier = Modifier.fillMaxSize().padding((u * 0.07f).dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    CardAvatar(store.avatar, avatarSize, faint)
                    Spacer(Modifier.width((u * 0.04f).dp))
                    BasicText(
                        text = store.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            color = label,
                            fontSize = (u * 0.065f).sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    BasicText(
                        text = store.topic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(color = soft, fontSize = (u * 0.048f).sp)
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(top = (u * 0.025f).dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(avatarSize)
                            .fillMaxHeight()
                            .drawBehind {
                                drawLine(
                                    color = faint,
                                    start = Offset(size.width / 2f, 0f),
                                    end = Offset(size.width / 2f, size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                    )
                    Spacer(Modifier.width((u * 0.03f).dp))
                    Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        BasicText(
                            text = store.body,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = label,
                                fontSize = (u * 0.05f).sp,
                                lineHeight = (u * 0.064f).sp
                            )
                        )
                        Spacer(Modifier.height((u * 0.025f).dp))
                        BasicText(
                            text = "♬ ${store.music}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(color = soft, fontSize = (u * 0.046f).sp)
                        )
                        Spacer(Modifier.height((u * 0.025f).dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape((u * 0.04f).dp))
                                .background(label.copy(alpha = 0.12f))
                        ) {
                            val photo = store.photo
                            if (photo != null) {
                                Image(
                                    bitmap = photo,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Spacer(Modifier.height((u * 0.025f).dp))
                        CardBottomBar(u, soft)
                    }
                }
            }
        }
    }
}

@Composable
fun CardAvatar(image: ImageBitmap?, size: Dp, fill: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(fill.copy(alpha = 0.4f))
    ) {
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun CardBottomBar(u: Float, color: Color) {
    val iconH = (u * 0.062f).dp
    Row(
        modifier = Modifier.fillMaxWidth().height((u * 0.075f).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size((u * 0.044f).dp, iconH)) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                lineTo(w, h)
                lineTo(w / 2f, h * 0.72f)
                lineTo(0f, h)
                close()
            }
            drawPath(path, color, style = glyphStroke(this))
        }
        Spacer(Modifier.width((u * 0.04f).dp))
        Canvas(modifier = Modifier.size(iconH)) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(w, 0f)
                lineTo(0f, h * 0.46f)
                lineTo(w * 0.42f, h * 0.6f)
                lineTo(w * 0.56f, h)
                close()
            }
            drawPath(path, color, style = glyphStroke(this))
            drawLine(
                color = color,
                start = Offset(w, 0f),
                end = Offset(w * 0.42f, h * 0.6f),
                strokeWidth = 1.1.dp.toPx()
            )
        }
        Spacer(Modifier.width((u * 0.04f).dp))
        Canvas(modifier = Modifier.size(iconH)) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val outer = size.width / 2f
            val inner = outer * 0.45f
            val path = Path()
            for (i in 0 until 10) {
                val r = if (i % 2 == 0) outer else inner
                val a = -PI / 2 + i * PI / 5
                val x = cx + (r * cos(a)).toFloat()
                val y = cy + (r * sin(a)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color, style = glyphStroke(this))
        }
    }
}

private fun glyphStroke(scope: androidx.compose.ui.graphics.drawscope.DrawScope) = Stroke(
    width = with(scope) { 1.1.dp.toPx() },
    cap = StrokeCap.Round,
    join = StrokeJoin.Round
)
