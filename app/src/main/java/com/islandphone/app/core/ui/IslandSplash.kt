package com.islandphone.app.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme
import kotlinx.coroutines.delay

private const val TOTAL_MS = 3400

@Composable
fun IslandSplash(onFinished: () -> Unit) {
    val colors = IslandTheme.colors
    val time = remember { Animatable(0f) }
    val cover = remember { Animatable(1f) }
    val words = stringArrayResource(R.array.splash_words)
    val statuses = stringArrayResource(R.array.splash_status)

    LaunchedEffect(Unit) {
        time.animateTo(1f, tween(TOTAL_MS, easing = LinearEasing))
        delay(250)
        cover.animateTo(0f, tween(420, easing = FastOutSlowInEasing))
        onFinished()
    }

    val percent by remember {
        derivedStateOf { (FastOutSlowInEasing.transform(time.value) * 100f).toInt() }
    }
    val statusIndex by remember {
        derivedStateOf { (time.value * statuses.size).toInt().coerceIn(0, statuses.size - 1) }
    }
    val counter by remember { derivedStateOf { (time.value * 120f).toInt() } }

    val muted = colors.textSecondary
    val strong = colors.onBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = cover.value
                val s = 1f + (1f - cover.value) * 0.04f
                scaleX = s
                scaleY = s
            }
            .background(colors.background)
            .pointerInput(Unit) { detectTapGestures { } }
    ) {
        SplashLines(time = time, modifier = Modifier.fillMaxSize())
        SplashMarquee(
            text = (stringResource(R.string.splash_marquee_a) + "   ").repeat(3),
            yFraction = 0.36f,
            direction = -1f,
            time = time
        )
        SplashMarquee(
            text = (stringResource(R.string.splash_marquee_b) + "   ").repeat(3),
            yFraction = 0.64f,
            direction = 1f,
            time = time
        )
        SplashLabels(time = time)
        SplashEdgeLabels(time = time)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 14.dp)
                .graphicsLayer { alpha = splashSmooth(time.value / 0.1f) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SplashMark(color = strong)
                Spacer(modifier = Modifier.width(8.dp))
                SplashCaption(text = stringResource(R.string.splash_brand), color = muted)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                SplashDot(color = muted)
                Spacer(modifier = Modifier.width(6.dp))
                SplashCaption(text = stringResource(R.string.splash_motion), color = muted)
                Spacer(modifier = Modifier.width(10.dp))
                SplashCaption(
                    text = "%03d".format(counter),
                    color = muted.copy(alpha = 0.6f)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer { alpha = splashSmooth(time.value / 0.12f) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SplashCaption(
                text = "—  " + stringResource(R.string.splash_tagline) + "  —",
                color = muted
            )
            Spacer(modifier = Modifier.height(22.dp))
            SplashWord(words = words, time = time)
            Spacer(modifier = Modifier.height(22.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SplashCaption(
                    text = stringResource(R.string.splash_sub_left),
                    color = muted.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(muted.copy(alpha = 0.7f))
                )
                Spacer(modifier = Modifier.width(10.dp))
                SplashCaption(
                    text = stringResource(R.string.splash_sub_right),
                    color = muted.copy(alpha = 0.7f)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 18.dp)
                .graphicsLayer { alpha = splashSmooth(time.value / 0.1f) }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SplashDot(color = muted)
                    Spacer(modifier = Modifier.width(8.dp))
                    SplashCaption(text = statuses[statusIndex], color = muted)
                }
                Text(
                    text = "$percent%",
                    fontSize = 13.sp,
                    color = strong,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .drawBehind {
                        drawRect(color = muted.copy(alpha = 0.18f))
                        drawRect(
                            color = strong,
                            size = Size(
                                width = size.width * FastOutSlowInEasing.transform(time.value),
                                height = size.height
                            )
                        )
                    }
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SplashCaption(
                    text = stringResource(R.string.splash_footer_left),
                    color = muted.copy(alpha = 0.7f)
                )
                SplashCaption(
                    text = stringResource(R.string.splash_footer_right),
                    color = muted.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun SplashCaption(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = color,
        fontSize = 10.sp,
        letterSpacing = 1.6f.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun SplashDot(color: Color) {
    Box(
        modifier = Modifier
            .size(5.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
private fun SplashMark(color: Color) {
    Canvas(modifier = Modifier.size(width = 16.dp, height = 12.dp)) {
        for (i in 0..2) {
            val x = size.width * (0.15f + i * 0.32f)
            drawLine(
                color = color,
                start = Offset(x, size.height),
                end = Offset(x + size.width * 0.25f, 0f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}
