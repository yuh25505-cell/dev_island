package com.island.phone.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.island.phone.core.ui.apps.AppId

@Composable
fun HomePager(apps: List<AppId>, modifier: Modifier = Modifier) {
    val pages = remember(apps) { paginate(apps) }
    val pagerState = rememberPagerState(pageCount = { pages.size })
    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { index ->
            AppGrid(apps = pages[index])
        }
        PageDots(
            count = pages.size,
            current = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
        )
    }
}
