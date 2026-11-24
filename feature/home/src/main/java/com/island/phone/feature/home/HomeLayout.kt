package com.island.phone.feature.home

import com.island.phone.core.ui.apps.AppId

val DockApps = listOf(AppId.CHAT, AppId.PHONE, AppId.ALBUM, AppId.SETTINGS)

val GridApps = AppId.entries.filter { it !in DockApps }
