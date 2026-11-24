package com.island.phone.feature.home

import com.island.phone.core.ui.apps.AppId

sealed interface HomeItem {
    val key: String
}

data class AppItem(val app: AppId) : HomeItem {
    override val key: String get() = "app:${app.name}"
}

data class FolderItem(
    val id: Int,
    val name: String,
    val apps: List<AppId>,
) : HomeItem {
    override val key: String get() = "folder:$id"
}
