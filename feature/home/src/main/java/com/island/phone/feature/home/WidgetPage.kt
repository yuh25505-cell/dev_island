package com.island.phone.feature.home

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.theme.IslandTheme
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WidgetContent(kind: WidgetKind, modifier: Modifier = Modifier) {
    when (kind) {
        WidgetKind.CLOCK -> ClockWidget(modifier)
        WidgetKind.BATTERY -> BatteryWidget(modifier)
        WidgetKind.WEATHER -> WeatherWidget(modifier)
        WidgetKind.COUNTDOWN -> CountdownWidget(modifier)
        WidgetKind.PHOTOCARD -> PhotoCardWidget(modifier)
    }
}

@Composable
fun ClockWidget(modifier: Modifier = Modifier) {
    var time by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val format = DateTimeFormatter.ofPattern("M月d日 EEEE", Locale.CHINA)
        while (true) {
            val now = LocalDateTime.now()
            time = String.format(Locale.US, "%02d:%02d", now.hour, now.minute)
            date = now.format(format)
            delay(1000)
        }
    }
    val color = IslandTheme.widget.clockText
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val unit = maxWidth.value
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BasicText(
                text = date,
                style = TextStyle(
                    color = color,
                    fontSize = (unit * 0.072f).sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            BasicText(
                text = time,
                style = TextStyle(
                    color = color,
                    fontSize = (unit * 0.32f).sp,
                    fontWeight = FontWeight.Bold,
                    fontFeatureSettings = "tnum"
                )
            )
        }
    }
}

@Composable
fun BatteryWidget(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var level by remember { mutableStateOf(100) }
    var charging by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            if (intent != null) {
                val l = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val s = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
                if (l >= 0 && s > 0) level = l * 100 / s
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            }
            delay(5000)
        }
    }
    val tokens = IslandTheme.widget
    val label = IslandTheme.icon.label
    val ringColor = if (level <= 20 && !charging) tokens.ringRed else tokens.ringGreen
    GlassSurface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        elevation = 10.dp
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val unit = maxWidth.value
            val ringSize = (unit * 0.4f).dp
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding((unit * 0.09f).dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.size(ringSize), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = size.width * 0.11f
                        val arcSize = Size(size.width - stroke, size.height - stroke)
                        val topLeft = Offset(stroke / 2f, stroke / 2f)
                        drawArc(
                            color = tokens.ringTrack,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke)
                        )
                        drawArc(
                            color = ringColor,
                            startAngle = -90f,
                            sweepAngle = 360f * level / 100f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }
                    Canvas(modifier = Modifier.size((ringSize.value * 0.27f).dp, (ringSize.value * 0.46f).dp)) {
                        val w = size.width
                        val h = size.height
                        val sw = w * 0.1f
                        drawRoundRect(
                            color = label,
                            topLeft = Offset(sw / 2f, sw / 2f),
                            size = Size(w - sw, h - sw),
                            cornerRadius = CornerRadius(w * 0.3f),
                            style = Stroke(width = sw)
                        )
                        drawRoundRect(
                            color = label,
                            topLeft = Offset(w * 0.34f, sw * 1.7f),
                            size = Size(w * 0.32f, sw),
                            cornerRadius = CornerRadius(sw / 2f)
                        )
                        drawRoundRect(
                            color = label,
                            topLeft = Offset(w * 0.3f, h - sw * 2.7f),
                            size = Size(w * 0.4f, sw),
                            cornerRadius = CornerRadius(sw / 2f)
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (charging) {
                        Canvas(modifier = Modifier.size((unit * 0.07f).dp, (unit * 0.1f).dp)) {
                            val w = size.width
                            val h = size.height
                            val bolt = Path().apply {
                                moveTo(w * 0.6f, 0f)
                                lineTo(0f, h * 0.58f)
                                lineTo(w * 0.46f, h * 0.58f)
                                lineTo(w * 0.36f, h)
                                lineTo(w, h * 0.38f)
                                lineTo(w * 0.54f, h * 0.38f)
                                close()
                            }
                            drawPath(bolt, tokens.ringGreen)
                        }
                        Spacer(Modifier.width(3.dp))
                    }
                    BasicText(
                        text = "$level%",
                        maxLines = 1,
                        style = TextStyle(
                            color = label,
                            fontSize = (unit * 0.25f).sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherWidget(modifier: Modifier = Modifier) {
    WidgetCard(value = "--°", modifier = modifier)
}

@Composable
fun CountdownWidget(modifier: Modifier = Modifier) {
    WidgetCard(value = "--", modifier = modifier)
}

@Composable
private fun WidgetCard(
    value: String,
    modifier: Modifier = Modifier,
) {
    val label = IslandTheme.icon.label
    GlassSurface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        elevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            BasicText(text = value, style = TextStyle(color = label, fontSize = 34.sp))
        }
    }
}
