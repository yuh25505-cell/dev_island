package com.island.phone.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.island.phone.core.ui.apps.AppId

class HomeState(initial: List<AppId>) {
    val items = mutableStateListOf<HomeItem>().apply { addAll(initial.map { AppItem(it) }) }
    var editing by mutableStateOf(false)
    var dragging by mutableStateOf<HomeItem?>(null)
    var dragPos by mutableStateOf(Offset.Zero)
    var hoverKey by mutableStateOf<String?>(null)
    var mergeReady by mutableStateOf(false)
    var pendingMove by mutableStateOf<Int?>(null)
    var openFolderId by mutableStateOf<Int?>(null)
    private var nextFolderId = 1

    val openFolder: FolderItem?
        get() = items.firstOrNull { it is FolderItem && it.id == openFolderId } as? FolderItem

    fun isDragging(item: HomeItem): Boolean = dragging?.key == item.key

    fun indexOfKey(key: String): Int = items.indexOfFirst { it.key == key }

    private fun indexOfFolder(id: Int): Int =
        items.indexOfFirst { it is FolderItem && it.id == id }

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
