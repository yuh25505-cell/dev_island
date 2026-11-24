package com.island.phone.feature.home

enum class WidgetKind(val cols: Int, val rows: Int, val title: String) {
    CLOCK(4, 2, "时钟"),
    BATTERY(2, 2, "电池"),
    WEATHER(2, 2, "天气"),
    COUNTDOWN(2, 2, "倒数日"),
    PHOTOCARD(2, 2, "拾光"),
    VINYL(2, 2, "唱片"),
}

data class WidgetPlacement(
    val kind: WidgetKind,
    val page: Int,
    val row: Int,
    val col: Int,
)
