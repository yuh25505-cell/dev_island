package com.islandphone.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme
import com.islandphone.app.core.theme.ThemeMode
import com.islandphone.app.core.ui.GlassBarItem
import com.islandphone.app.core.ui.GlassBottomBar
import com.islandphone.app.core.ui.GlassButton
import com.islandphone.app.core.ui.GlassButtonStyle
import com.islandphone.app.core.ui.GlassListItem
import com.islandphone.app.core.ui.GlassPanel
import com.islandphone.app.core.ui.GlassSwitch
import com.islandphone.app.core.ui.ThemeModeSelector

@Composable
fun HomeScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onOpenDemoPage: () -> Unit = {}
) {
    val colors = IslandTheme.colors
    var switchOn by remember { mutableStateOf(true) }
    var barIndex by remember { mutableIntStateOf(0) }

    val barItems = listOf(
        GlassBarItem(stringResource(R.string.demo_bar_home)),
        GlassBarItem(stringResource(R.string.demo_bar_chat)),
        GlassBarItem(stringResource(R.string.demo_bar_me))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colors.background, colors.backgroundTint)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = colors.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))
            ThemeModeSelector(
                selected = themeMode,
                onSelect = onThemeModeChange
            )
            Spacer(modifier = Modifier.height(24.dp))
            GlassPanel(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.demo_panel_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassButton(
                        text = stringResource(R.string.demo_button_primary),
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = stringResource(R.string.demo_button_glass),
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        style = GlassButtonStyle.Glass
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                GlassListItem(
                    title = stringResource(R.string.demo_item_switch),
                    onClick = { switchOn = !switchOn },
                    trailing = {
                        GlassSwitch(
                            checked = switchOn,
                            onCheckedChange = { switchOn = it }
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                GlassListItem(
                    title = stringResource(R.string.demo_item_plain),
                    onClick = {},
                    trailing = {
                        Text(
                            text = "›",
                            style = MaterialTheme.typography.titleLarge,
                            color = colors.textSecondary
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            DialogShowcase()
            Spacer(modifier = Modifier.height(20.dp))
            GlassPanel(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.demo_nav_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                GlassListItem(
                    title = stringResource(R.string.demo_open_page),
                    onClick = onOpenDemoPage,
                    trailing = {
                        Text(
                            text = "›",
                            style = MaterialTheme.typography.titleLarge,
                            color = colors.textSecondary
                        )
                    }
                )
            }
        }

        GlassBottomBar(
            items = barItems,
            selectedIndex = barIndex,
            onSelect = { barIndex = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
        )
    }
}
