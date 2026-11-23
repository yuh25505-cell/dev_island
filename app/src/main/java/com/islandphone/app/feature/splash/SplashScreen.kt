package com.islandphone.app.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val spread = remember { Animatable(0f) }
    val falling = remember { Animatable(0f) }
    val textFade = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        falling.animateTo(1f, tween(620))
        spread.animateTo(1f, tween(1050))
        textFade.animateTo(1f, tween(750))
        delay(1450)
        onFinished()
    }
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment=Alignment.Center) {
        Box(Modifier.size(230.dp).scale(0.35f + spread.value * 1.8f)
            .alpha((1f - spread.value).coerceIn(0f, 0.26f))
            .background(Color(0xFF8EAFC9).copy(alpha=0.42f), CircleShape))
        Box(Modifier.offset(y=(-52).dp + 52.dp * falling.value).size(7.dp)
            .alpha(1f - falling.value * 0.4f).background(Color(0xFF8EAFC9), CircleShape))
        Text("请让这座孤岛因你们而拥有属于它的故事",
            Modifier.padding(horizontal=28.dp).alpha(textFade.value),
            color=MaterialTheme.colorScheme.onBackground, fontSize=15.sp, letterSpacing=1.8.sp)
    }
}
