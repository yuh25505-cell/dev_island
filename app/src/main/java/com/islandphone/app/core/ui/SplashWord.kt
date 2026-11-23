package com.islandphone.app.core.ui

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islandphone.app.core.theme.IslandTheme

private const val WORD_START = 0.06f
private const val WORD_SLOT = 0.20f

@Composable
internal fun SplashWord(
    words: Array<String>,
    time: SplashTime,
    modifier: Modifier = Modifier
) {
    val colors = IslandTheme.colors
    val lastIndex = words.size - 1
    val index by remember {
        derivedStateOf {
            ((time.value - WORD_START) / WORD_SLOT).toInt().coerceIn(0, lastIndex)
        }
    }
    val word = words[index]

    Row(
        modifier = modifier.height(64.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        word.forEachIndexed { i, ch ->
            Text(
                text = ch.toString(),
                fontSize = 46.sp,
                fontWeight = FontWeight.Light,
                color = colors.onBackground,
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .graphicsLayer {
                        val local = (time.value - WORD_START - index * WORD_SLOT) / WORD_SLOT
                        val inA = splashSmooth((local - i * 0.03f) / 0.2f)
                        val outA = if (index == lastIndex) {
                            0f
                        } else {
                            splashSmooth((local - 0.78f - i * 0.02f) / 0.18f)
                        }
                        val a = inA * (1f - outA)
                        alpha = a
                        translationY = (1f - inA) * 14.dp.toPx() - outA * 10.dp.toPx()
                        val r = (1f - a) * 16f
                        renderEffect = if (r > 0.5f && Build.VERSION.SDK_INT >= 31) {
                            BlurEffect(r, r, TileMode.Decal)
                        } else {
                            null
                        }
                    }
            )
        }
    }
}
