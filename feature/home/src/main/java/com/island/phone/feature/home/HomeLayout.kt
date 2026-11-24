package com.island.phone.feature.home

import com.island.phone.core.ui.apps.AppId

val DockApps = listOf(AppId.CHAT, AppId.PHONE, AppId.ALBUM, AppId.SETTINGS)

val GridApps = AppId.entries.filter { it !in DockApps }

const val GridColumns = 4
const val GridRows = 5
const val AppsPerPage = GridColumns * GridRows

fun paginate(apps: List<AppId>, perPage: Int = AppsPerPage): List<List<AppId>> =
    if (apps.isEmpty()) listOf(emptyList()) else apps.chunked(perPage)
