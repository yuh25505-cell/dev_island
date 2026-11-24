package com.island.phone.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.island.phone.core.ui.apps.AppId
import kotlin.math.abs

data class PlacedItem(val item: HomeItem, val row: Int, val col: Int)

class HomeState(initial: List<AppId>) {
    val items = mutableStateListOf<HomeItem>().apply { addAll(initial.map { AppItem(it) }) }
    val widgets = mutableStateListOf(
        WidgetPlacement(WidgetKind.CLOCK, 0, 0, 0),
        WidgetPlacement(WidgetKind.BATTERY, 0, 2, 2),
        WidgetPlacement(WidgetKind.WEATHER, 1, 0, 0),
        WidgetPlacement(WidgetKind.COUNTDOWN, 1, 2, 2),
        WidgetPlacement(WidgetKind.PHOTOCARD, 2, 0, 2),
        WidgetPlacement(WidgetKind.VINYL, 2, 2, 0),
    )
    var editing by mutableStateOf(false)
    var photoCardOpen by mutableStateOf(false)
    var vinylOpen by mutableStateOf(false)
    var dragging by mutableStateOf<HomeItem?>(null)
    var draggingWidget by mutableStateOf<WidgetKind?>(null)
    var widgetGrab by mutableStateOf(Offset.Zero)
    var dragPos by mutableStateOf(Offset.Zero)
    var hoverKey by mutableStateOf<String?>(null)
    var mergeReady by mutableStateOf(false)
    var pendingMove by mutableStateOf<Int?>(null)
    var openFolderId by mutableStateOf<Int?>(null)
    private var nextFolderId = 1

    val openFolder: FolderItem?
        get() = items.firstOrNull { it is FolderItem && it.id == openFolderId } as? FolderItem

    val isDraggingAny: Boolean
        get() = dragging != null || draggingWidget != null

    fun isDragging(item: HomeItem): Boolean = dragging?.key == item.key

    fun indexOfKey(key: String): Int = items.indexOfFirst { it.key == key }

    private fun indexOfFolder(id: Int): Int =
        items.indexOfFirst { it is FolderItem && it.id == id }

    fun widgetAt(page: Int, row: Int, col: Int): WidgetPlacement? =
        widgets.firstOrNull {
            it.page == page &&
                row >= it.row && row < it.row + it.kind.rows &&
                col >= it.col && col < it.col + it.kind.cols
        }

    private fun freeCount(page: Int): Int =
        GridRows * GridColumns -
            widgets.filter { it.page == page }.sumOf { it.kind.cols * it.kind.rows }

    fun pageCount(): Int {
        val lastWidgetPage = widgets.maxOfOrNull { it.page } ?: 0
        var total = 0
        var p = 0
        while (true) {
            total += freeCount(p)
            if (total >= items.size && p >= lastWidgetPage) return p + 1
            p++
        }
    }

    private fun ordinalOf(page: Int, row: Int, col: Int): Int {
        var n = 0
        for (p in 0 until page) n += freeCount(p)
        for (r in 0 until GridRows) {
            for (c in 0 until GridColumns) {
                if (r == row && c == col) return n
                if (widgetAt(page, r, c) == null) n++
            }
        }
        return n
    }

    fun itemIndexAt(page: Int, row: Int, col: Int): Int? =
        if (widgetAt(page, row, col) != null) null else ordinalOf(page, row, col)

    fun placedOn(page: Int): List<PlacedItem> {
        var k = 0
        for (p in 0 until page) k += freeCount(p)
        val out = mutableListOf<PlacedItem>()
        for (r in 0 until GridRows) {
            for (c in 0 until GridColumns) {
                if (widgetAt(page, r, c) == null) {
                    val entry = items.getOrNull(k)
                    if (entry != null) out.add(PlacedItem(entry, r, c))
                    k++
                }
            }
        }
        return out
    }

    private fun widgetFits(kind: WidgetKind, page: Int, row: Int, col: Int): Boolean =
        widgets.none {
            it.kind != kind && it.page == page &&
                row < it.row + it.kind.rows && row + kind.rows > it.row &&
                col < it.col + it.kind.cols && col + kind.cols > it.col
        }

    fun dropWidget(kind: WidgetKind, page: Int, row: Int, col: Int) {
        val cur = widgets.indexOfFirst { it.kind == kind }
        if (cur < 0) return
        var best: WidgetPlacement? = null
        var bestDist = Int.MAX_VALUE
        for (r in 0..(GridRows - kind.rows)) {
            for (c in 0..(GridColumns - kind.cols)) {
                if (!widgetFits(kind, page, r, c)) continue
                val d = abs(r - row) + abs(c - col)
                if (d < bestDist) {
                    bestDist = d
                    best = WidgetPlacement(kind, page, r, c)
                }
            }
        }
        val target = best
        if (target != null) widgets[cur] = target
    }

    fun moveTo(key: String, target: Int) {
        val from = indexOfKey(key)
        if (from < 0) return
        val to = target.coerceIn(0, items.lastIndex)
        if (from == to) return
        val item = items.removeAt(from)
        items.add(to, item)
    }

    fun clearDrag() {
        dragging = null
        draggingWidget = null
        hoverKey = null
        mergeReady = false
        pendingMove = null
    }

    fun canMerge(dragged: HomeItem, target: HomeItem): Boolean {
        if (dragged !is AppItem) return false
        if (dragged.key == target.key) return false
        return when (target) {
            is AppItem -> true
            is FolderItem -> target.apps.size < FolderCapacity
        }
    }

    fun mergeInto(dragged: AppItem, targetKey: String) {
        val ti = indexOfKey(targetKey)
        val di = indexOfKey(dragged.key)
        if (ti < 0 || di < 0) return
        when (val target = items[ti]) {
            is FolderItem -> {
                if (target.apps.size >= FolderCapacity) return
                items[ti] = target.copy(apps = target.apps + dragged.app)
                items.removeAt(di)
            }
            is AppItem -> {
                items[ti] = FolderItem(nextFolderId++, DefaultFolderName, listOf(target.app, dragged.app))
                items.removeAt(di)
            }
        }
    }

    fun renameFolder(id: Int, name: String) {
        val i = indexOfFolder(id)
        if (i < 0) return
        val folder = items[i] as FolderItem
        items[i] = folder.copy(name = name.take(FolderNameMax))
    }

    fun closeFolder() {
        val id = openFolderId
        if (id != null) {
            val i = indexOfFolder(id)
            if (i >= 0) {
                val folder = items[i] as FolderItem
                if (folder.name.isBlank()) items[i] = folder.copy(name = DefaultFolderName)
            }
        }
        openFolderId = null
    }

    fun pullOut(id: Int, app: AppId) {
        val i = indexOfFolder(id)
        if (i < 0) return
        val folder = items[i] as FolderItem
        val rest = folder.apps - app
        if (rest.size >= 2) {
            items[i] = folder.copy(apps = rest)
            items.add(i + 1, AppItem(app))
        } else {
            items.removeAt(i)
            items.addAll(i, rest.map { AppItem(it) } + AppItem(app))
        }
        openFolderId = null
    }

    fun dissolveFolder(id: Int) {
        val i = indexOfFolder(id)
        if (i < 0) return
        val folder = items[i] as FolderItem
        items.removeAt(i)
        items.addAll(i, folder.apps.map { AppItem(it) })
        openFolderId = null
    }
}
