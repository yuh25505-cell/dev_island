package com.island.phone.feature.home

import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.apps.AppId

val DockApps = listOf(AppId.CHAT, AppId.PHONE, AppId.ALBUM, AppId.SETTINGS)

val GridApps = AppId.entries.filter { it !in DockApps }

const val GridColumns = 4
const val GridRows = 4
const val AppsPerPage = GridColumns * GridRows

val GridPadH = 16.dp
val GridPadV = 8.dp
val CellHeight = 84.dp
val RowGap = 22.dp

const val FolderColumns = 3
const val FolderCapacity = 9
const val FolderNameMax = 10
const val DefaultFolderName = "文件夹"

val FolderCellW = 84.dp
val FolderCellH = 98.dp
val FolderPanelPad = 20.dp
val FolderPanelWidth = FolderCellW * FolderColumns + FolderPanelPad * 2

fun <T> paginate(items: List<T>, perPage: Int = AppsPerPage): List<List<T>> =
    if (items.isEmpty()) listOf(emptyList()) else items.chunked(perPage)
