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

fun paginate(apps: List<AppId>, perPage: Int = AppsPerPage): List<List<AppId>> =
    if (apps.isEmpty()) listOf(emptyList()) else apps.chunked(perPage)
