package com.islandphone.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import com.islandphone.app.core.design.IslandTheme
import com.islandphone.app.feature.home.HomeScreen
import com.islandphone.app.feature.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IslandTheme {
                var splash by remember { mutableStateOf(true) }
                AnimatedContent(targetState=splash,
                    transitionSpec={ fadeIn(tween(450)) togetherWith fadeOut(tween(300)) },
                    label="splash-home") { show ->
                    if (show) SplashScreen { splash=false } else HomeScreen()
                }
            }
        }
    }
}
