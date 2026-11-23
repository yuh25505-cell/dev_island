package com.islandphone.app.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
class IslandNavEntry(
    val id: Int,
    val route: String,
    val animateIn: Boolean
)

@Stable
class IslandNavController internal constructor(startRoutes: List<String>) {
    private var nextId = 0

    val entries = mutableStateListOf<IslandNavEntry>()

    internal var backRequestId: Int? by mutableStateOf(null)
        private set

    init {
        startRoutes.forEach { entries.add(IslandNavEntry(nextId++, it, false)) }
    }

    val canGoBack: Boolean get() = entries.size > 1

    fun push(route: String) {
        if (backRequestId != null) return
        entries.add(IslandNavEntry(nextId++, route, true))
    }

    fun back() {
        if (entries.size > 1 && backRequestId == null) {
            backRequestId = entries.last().id
        }
    }

    internal fun removeTop(id: Int) {
        if (entries.size > 1 && entries.last().id == id) {
            entries.removeAt(entries.lastIndex)
        }
        backRequestId = null
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
