package com.islandphone.app.home

import androidx.compose.runtime.Composable
import com.islandphone.app.core.theme.ThemeMode
import com.islandphone.app.core.ui.IslandNavHost
import com.islandphone.app.core.ui.rememberIslandNavController

private const val ROUTE_HOME = "home"
private const val ROUTE_DEMO_PREFIX = "demo/"

@Composable
fun IslandRoot(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    val nav = rememberIslandNavController(ROUTE_HOME)

    IslandNavHost(controller = nav) { route ->
        when {
            route == ROUTE_HOME -> HomeScreen(
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
                onOpenDemoPage = { nav.push("${ROUTE_DEMO_PREFIX}1") }
            )
            route.startsWith(ROUTE_DEMO_PREFIX) -> {
                val level = route.removePrefix(ROUTE_DEMO_PREFIX).toIntOrNull() ?: 1
                NavDemoPage(
                    level = level,
                    onOpenNext = { nav.push("$ROUTE_DEMO_PREFIX${level + 1}") }
                )
            }
            else -> Unit
        }
    }
}
