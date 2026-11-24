package com.island.phone.core.ui.theme

import androidx.compose.runtime.State
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight

internal class OverrideFontResolver(
    private val base: FontFamily.Resolver,
    private val family: FontFamily,
) : FontFamily.Resolver {
    override suspend fun preload(fontFamily: FontFamily) {
        base.preload(fontFamily)
    }

    override fun resolve(
        fontFamily: FontFamily?,
        fontWeight: FontWeight,
        fontStyle: FontStyle,
        fontSynthesis: FontSynthesis,
    ): State<Any> {
        val target = if (fontFamily == null || fontFamily == FontFamily.Default) family else fontFamily
        return base.resolve(target, fontWeight, fontStyle, fontSynthesis)
    }
}
