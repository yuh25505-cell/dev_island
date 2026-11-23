package com.islandphone.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme
import com.islandphone.app.core.theme.ThemeMode
import com.islandphone.app.core.ui.ThemeModeSelector

@Composable
fun HomeScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    val colors = IslandTheme.colors
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = colors.onBackground
            )
            Spacer(modifier = Modifier.height(32.dp))
            ThemeModeSelector(
                selected = themeMode,
                onSelect = onThemeModeChange
            )
        }
    }
}
