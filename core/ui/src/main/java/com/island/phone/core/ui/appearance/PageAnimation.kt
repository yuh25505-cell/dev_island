package com.island.phone.core.ui.appearance

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberPageAnimation(): Boolean = AppearanceStore.get(LocalContext.current).pageAnimation
