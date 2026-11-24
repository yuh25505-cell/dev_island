package com.island.phone.core.ui.apps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

typealias AppPage = @Composable (onBack: () -> Unit) -> Unit

val LocalAppPages = staticCompositionLocalOf<Map<AppId, AppPage>> { emptyMap() }
