package com.islandphone.app.core.ui

import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

@Composable
internal fun SplashCurtain(time: SplashTime, modifier: Modifier = Modifier) {
    val colors = IslandTheme.colors
    Canvas(modifier = modifier) {
        val q = FastOutSlowInEasing.transform(splashWindow(time.value, CURTAIN_START, CURTAIN_END))
        if (q <= 0f) return@Canvas
        val w = size.width
        val h = size.height
        val hem = 16.dp.toPx()
        val shadowOffset = 6.dp.toPx()
        val edge = (h + hem * 2f) * q
        val sway = sin(PI.toFloat() * q)
        fun hemY(x: Float): Float =
            edge + hem * sway * sin(x / w * 2f * PI.toFloat() * 2.5f + q * 8f)

        val hemPath = Path()
        hemPath.moveTo(0f, hemY(0f))
        var x = 0f
        while (x < w) {
            x = min(w, x + 14f)
            hemPath.lineTo(x, hemY(x))
        }

        val fill = Path()
        fill.moveTo(0f, 0f)
        fill.lineTo(w, 0f)
        fill.lineTo(w, hemY(w))
        x = w
        while (x > 0f) {
            x = max(0f, x - 14f)
            fill.lineTo(x, hemY(x))
        }
        fill.close()

        drawPath(
            path = fill,
            brush = Brush.verticalGradient(
                colors = listOf(colors.backgroundTint, colors.background),
                startY = 0f,
                endY = h
            )
        )

        val pleat = colors.onBackground.copy(alpha = 0.04f)
        val stops = Array(15) { i ->
            (i / 14f) to (if (i % 2 == 0) Color.Transparent else pleat)
        }
        clipPath(fill) {
            drawRect(brush = Brush.horizontalGradient(*stops, startX = 0f, endX = w))
        }

        val shadow = if (colors.isDark) Color.Black.copy(alpha = 0.35f)
        else colors.onBackground.copy(alpha = 0.10f)
        withTransform({ translate(0f, shadowOffset) }) {
            drawPath(
                path = hemPath,
                color = shadow,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
        drawPath(
            path = hemPath,
            color = colors.accent.copy(alpha = 0.55f),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

private fun Modifier.splashReveal(time: SplashTime, start: Float, span: Float = 0.08f): Modifier =
    graphicsLayer {
        val a = splashSmooth((time.value - start) / span)
        alpha = a
        translationY = (1f - a) * 14.dp.toPx()
        val r = (1f - a) * 18f
        renderEffect = if (r > 0.5f && Build.VERSION.SDK_INT >= 31) {
            BlurEffect(r, r, TileMode.Decal)
        } else {
            null
        }
    }

@Composable
internal fun SplashMessage(time: SplashTime, modifier: Modifier = Modifier) {
    val colors = IslandTheme.colors
    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SplashBoatMark(time = time, modifier = Modifier.splashReveal(time, 0.80f))
        Spacer(modifier = Modifier.height(26.dp))
        Text(
            text = stringResource(R.string.splash_title),
            color = colors.onBackground,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.splashReveal(time, 0.82f)
        )
        Spacer(modifier = Modifier.height(18.dp))
        Spacer(
            modifier = Modifier
                .splashReveal(time, 0.86f)
                .width(28.dp)
                .height(3.dp)
                .clip(CircleShape)
                .background(colors.accent)
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = stringResource(R.string.splash_line_1),
            color = colors.onBackground.copy(alpha = 0.72f),
            fontSize = 16.sp,
            letterSpacing = 1.5.sp,
            lineHeight = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.splashReveal(time, 0.88f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.splash_line_2),
            color = colors.onBackground.copy(alpha = 0.72f),
            fontSize = 16.sp,
            letterSpacing = 1.5.sp,
            lineHeight = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.splashReveal(time, 0.91f)
        )
    }
}

@Composable
private fun SplashBoatMark(time: SplashTime, modifier: Modifier = Modifier) {
    val colors = IslandTheme.colors
    Canvas(modifier = modifier.size(width = 64.dp, height = 54.dp)) {
        val sec = time.value * SPLASH_TOTAL_MS / 1000f
        val s = size.width * 0.8f
        val origin = Offset(size.width * 0.5f, size.height * 0.80f)
        drawPaperBoat(
            origin = origin,
            s = s,
            degrees = 3f * sin(sec * 1.8f),
            light = colors.accent,
            mid = lerp(colors.accent, colors.background, 0.45f),
            dark = lerp(colors.accent, Color.Black, 0.25f)
        )
        val line = Path()
        val y = size.height * 0.88f
        line.moveTo(size.width * 0.08f, y)
        var x = size.width * 0.08f
        while (x < size.width * 0.92f) {
            x = min(size.width * 0.92f, x + 3f)
            line.lineTo(x, y + 1.6.dp.toPx() * sin(x / size.width * 6f * PI.toFloat() + sec * 2f))
        }
        drawPath(
            path = line,
            color = colors.accent.copy(alpha = 0.5f),
            style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
