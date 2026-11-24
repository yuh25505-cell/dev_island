package com.island.phone.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.island.phone.core.ui.glass.GlassSurface
import com.island.phone.core.ui.theme.IslandTheme
import kotlinx.coroutines.delay
import java.time.LocalTime

private val WidgetGap = 16.dp

@Composable
fun WidgetPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = GridPadH, vertical = GridPadV),
        verticalArrangement = Arrangement.spacedBy(WidgetGap)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(WidgetGap)
        ) {
            ClockWidget(Modifier.weight(1f).aspectRatio(1f))
            WeatherWidget(Modifier.weight(1f).aspectRatio(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(WidgetGap)
        ) {
            CountdownWidget(Modifier.weight(1f).aspectRatio(1f))
            StickyWidget(Modifier.weight(1f).aspectRatio(1f))
        }
    }
}

@Composable
fun ClockWidget(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            val t = LocalTime.now()
            text = "%02d:%02d".format(t.hour, t.minute)
            delay(1000)
        }
    }
    WidgetCard(title = "时钟", value = text, modifier = modifier)
}

@Composable
fun WeatherWidget(modifier: Modifier = Modifier) {
    WidgetCard(title = "天气", value = "--°", modifier = modifier)
}

@Composable
fun CountdownWidget(modifier: Modifier = Modifier) {
    WidgetCard(title = "倒数日", value = "--", modifier = modifier)
}

@Composable
fun StickyWidget(modifier: Modifier = Modifier) {
    WidgetCard(
        title = "便利贴",
        value = "",
        modifier = modifier,
        fill = IslandTheme.widget.stickyFill
    )
}

@Composable
private fun WidgetCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    fill: Color = Color.Transparent,
) {
    val label = IslandTheme.icon.label
    GlassSurface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind { drawRect(fill) }
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            BasicText(text = title, style = TextStyle(color = label, fontSize = 13.sp))
            BasicText(text = value, style = TextStyle(color = label, fontSize = 34.sp))
        }
    }
}
