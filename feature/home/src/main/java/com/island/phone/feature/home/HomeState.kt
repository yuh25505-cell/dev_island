package com.island.phone.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.island.phone.core.ui.apps.AppId

class HomeState(initial: List<AppId>) {
    val apps = mutableStateListOf<AppId>().apply { addAll(initial) }
    var editing by mutableStateOf(false)
    var dragging by mutableStateOf<AppId?>(null)
    var dragPos by mutableStateOf(Offset.Zero)

    fun moveTo(app: AppId, target: Int) {
        val from = apps.indexOf(app)
        if (from < 0) return
        val to = target.coerceIn(0, apps.lastIndex)
        if (from == to) return
        apps.removeAt(from)
        apps.add(to, app)
    }
}
