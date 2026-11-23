package com.islandphone.app.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
class IslandNavEntry(
    val id: Int,
    val route: String
)

@Stable
class IslandNavController internal constructor(startRoutes: List<String>) {
    private var nextId = 0

    val entries = mutableStateListOf<IslandNavEntry>()

    init {
        startRoutes.forEach { entries.add(IslandNavEntry(nextId++, it)) }
    }

    val canGoBack: Boolean get() = entries.size > 1

    fun push(route: String) {
        entries.add(IslandNavEntry(nextId++, route))
    }

    fun back() {
        if (entries.size > 1) entries.removeAt(entries.lastIndex)
    }
}

val LocalIslandNav = staticCompositionLocalOf<IslandNavController?> { null }

@Composable
fun rememberIslandNavController(startRoute: String): IslandNavController =
    rememberSaveable(
        saver = listSaver<IslandNavController, String>(
            save = { controller -> controller.entries.map { it.route } },
            restore = { routes -> IslandNavController(routes) }
        )
    ) { IslandNavController(listOf(startRoute)) }
