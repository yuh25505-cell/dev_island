package com.island.phone.feature.home

import android.content.Context
import com.island.phone.core.ui.apps.AppId
import org.json.JSONArray
import org.json.JSONObject

object LayoutStore {
    private const val FILE = "island_layout"
    private const val KEY = "home_layout"
    private const val MaxPage = 20

    fun encode(state: HomeState): String {
        val items = JSONArray()
        state.items.forEach { item ->
            val o = JSONObject()
            when (item) {
                is AppItem -> {
                    o.put("t", "app")
                    o.put("id", item.app.name)
                }
                is FolderItem -> {
                    o.put("t", "folder")
                    o.put("id", item.id)
                    o.put("name", item.name)
                    o.put("apps", JSONArray(item.apps.map { it.name }))
                }
            }
            items.put(o)
        }
        val widgets = JSONArray()
        state.widgets.forEach { w ->
            val o = JSONObject()
            o.put("k", w.kind.name)
            o.put("p", w.page)
            o.put("r", w.row)
            o.put("c", w.col)
            widgets.put(o)
        }
        val dock = JSONArray(state.dock.map { it.name })
        return JSONObject()
            .put("v", 2)
            .put("items", items)
            .put("widgets", widgets)
            .put("dock", dock)
            .toString()
    }

    fun save(context: Context, encoded: String) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY, encoded)
            .apply()
    }

    fun restore(context: Context, state: HomeState) {
        val raw = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .getString(KEY, null) ?: return
        try {
            val root = JSONObject(raw)
            val dockArr = root.optJSONArray("dock")
            val dock = if (dockArr == null) DockApps else readDock(dockArr)
            val items = readItems(root.getJSONArray("items"), dock)
            val widgets = readWidgets(root.getJSONArray("widgets"), state.widgets.toList())
            state.restore(items, widgets, dock)
        } catch (e: Exception) {
        }
    }

    private fun appOf(name: String): AppId? = AppId.entries.firstOrNull { it.name == name }

    private fun readDock(arr: JSONArray): List<AppId> {
        val out = mutableListOf<AppId>()
        for (i in 0 until arr.length()) {
            val app = appOf(arr.getString(i))
            if (app != null && app !in out && out.size < DockCapacity) out.add(app)
        }
        return out
    }

    private fun readItems(arr: JSONArray, dock: List<AppId>): List<HomeItem> {
        val allowed = AppId.entries.toSet()
        val seen = dock.toMutableSet()
        val usedIds = mutableSetOf<Int>()
        val out = mutableListOf<HomeItem>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            if (o.getString("t") == "app") {
                val app = appOf(o.getString("id"))
                if (app != null && app in allowed && seen.add(app)) out.add(AppItem(app))
            } else {
                val list = o.getJSONArray("apps")
                val apps = mutableListOf<AppId>()
                for (j in 0 until list.length()) {
                    val app = appOf(list.getString(j))
                    if (app != null && app in allowed && seen.add(app)) apps.add(app)
                }
                val id = o.getInt("id")
                if (apps.size >= 2 && usedIds.add(id)) {
                    out.add(FolderItem(id, o.getString("name"), apps))
                } else {
                    apps.forEach { out.add(AppItem(it)) }
                }
            }
        }
        AppId.entries.filter { it !in seen }.forEach { out.add(AppItem(it)) }
        return out
    }

    private fun readWidgets(arr: JSONArray, defaults: List<WidgetPlacement>): List<WidgetPlacement> {
        val out = mutableListOf<WidgetPlacement>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val kind = WidgetKind.entries.firstOrNull { it.name == o.getString("k") } ?: continue
            if (out.any { it.kind == kind }) return defaults
            val placement = WidgetPlacement(kind, o.getInt("p"), o.getInt("r"), o.getInt("c"))
            if (!fits(placement, out)) return defaults
            out.add(placement)
        }
        for (d in defaults) {
            if (out.none { it.kind == d.kind }) {
                if (!fits(d, out)) return defaults
                out.add(d)
            }
        }
        return out
    }

    private fun fits(w: WidgetPlacement, others: List<WidgetPlacement>): Boolean {
        if (w.page !in 0..MaxPage) return false
        if (w.row < 0 || w.col < 0) return false
        if (w.row + w.kind.rows > GridRows || w.col + w.kind.cols > GridColumns) return false
        return others.none {
            it.page == w.page &&
                w.row < it.row + it.kind.rows && w.row + w.kind.rows > it.row &&
                w.col < it.col + it.kind.cols && w.col + w.kind.cols > it.col
        }
    }
}
