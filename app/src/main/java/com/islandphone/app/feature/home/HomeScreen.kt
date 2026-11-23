package com.islandphone.app.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islandphone.app.core.design.GlassSurface

private data class HomeApp(val title: String, val glyph: String, val color: Color)
private val appList = listOf(
    HomeApp("聊天","✉",Color(0xFF83B79B)), HomeApp("屿年","⌛",Color(0xFFD0AD8A)),
    HomeApp("相册","▧",Color(0xFFB6A0CF)), HomeApp("日历","▦",Color(0xFFE09A9A)),
    HomeApp("备忘录","✎",Color(0xFFE2C576)), HomeApp("天气","☁",Color(0xFF8DB7D8)),
    HomeApp("时钟","◷",Color(0xFF96A5B8)), HomeApp("设置","⚙",Color(0xFF9CA1A8)),
    HomeApp("相遇","⌘",Color(0xFFD5A2B5)), HomeApp("论坛","◎",Color(0xFF89B5B2)),
    HomeApp("文学城","文",Color(0xFFB7A58B)), HomeApp("栖迟","☾",Color(0xFF9E9BCB))
)

@Composable
fun HomeScreen() {
    Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFFB9CBD9),Color(0xFFDAD8D0),Color(0xFFB8C5BC))))) {
        Column(Modifier.fillMaxSize().padding(horizontal=23.dp, vertical=26.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically) {
                Column {
                    Text("星期一 · 11月24日", color=Color(0xFF263746).copy(alpha=.72f), fontSize=12.sp)
                    Text("00:09", color=Color(0xFF1E2B38), fontSize=38.sp, fontWeight=FontWeight.Light)
                }
                GlassSurface(Modifier.size(52.dp)) {
                    Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center) { Text("岛", color=Color(0xFF304454), fontSize=21.sp) }
                }
            }
            Spacer(Modifier.height(20.dp))
            GlassSurface(Modifier.fillMaxWidth().height(108.dp)) {
                Column(Modifier.fillMaxSize().padding(17.dp), verticalArrangement=Arrangement.Center) {
                    Text("今天的岛屿", color=Color(0xFF273846), fontSize=15.sp, fontWeight=FontWeight.Medium)
                    Spacer(Modifier.height(6.dp))
                    Text("生活正在慢慢发生。", color=Color(0xFF334A5B), fontSize=13.sp)
                }
            }
            Spacer(Modifier.height(22.dp))
            LazyVerticalGrid(columns=GridCells.Fixed(4), modifier=Modifier.weight(1f),
                contentPadding=PaddingValues(bottom=16.dp), verticalArrangement=Arrangement.spacedBy(21.dp), horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                items(appList) { app ->
                    Column(horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.spacedBy(6.dp)) {
                        Box(Modifier.size(57.dp).clip(RoundedCornerShape(19.dp))
                            .background(Brush.linearGradient(listOf(app.color, app.color.copy(alpha=.72f)))),
                            contentAlignment=Alignment.Center) {
                            Text(app.glyph, color=Color.White, fontSize=24.sp)
                        }
                        Text(app.title, color=Color(0xFF293A48), fontSize=11.sp)
                    }
                }
            }
            GlassSurface(Modifier.fillMaxWidth().height(78.dp)) {
                Row(Modifier.fillMaxSize().padding(horizontal=18.dp), horizontalArrangement=Arrangement.SpaceEvenly, verticalAlignment=Alignment.CenterVertically) {
                    listOf("☎","✉","♫","⚙").forEach { glyph ->
                        Box(Modifier.size(46.dp).clip(CircleShape).background(Color.White.copy(alpha=.25f)), contentAlignment=Alignment.Center) {
                            Text(glyph, color=Color(0xFF304353), fontSize=21.sp)
                        }
                    }
                }
            }
        }
    }
}
