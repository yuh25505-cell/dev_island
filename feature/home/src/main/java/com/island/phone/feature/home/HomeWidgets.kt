package com.island.phone.feature.home

enum class WidgetKind(val cols: Int, val rows: Int) {
    CLOCK(4, 2),
    BATTERY(2, 2),
    WEATHER(2, 2),
    COUNTDOWN(2, 2),
}

data class WidgetPlacement(
    val kind: WidgetKind,
    val page: Int,
    val row: Int,
    val col: Int,
)
