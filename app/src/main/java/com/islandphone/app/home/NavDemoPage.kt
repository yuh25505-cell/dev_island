package com.islandphone.app.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.islandphone.app.R
import com.islandphone.app.core.ui.GlassButton
import com.islandphone.app.core.ui.GlassListItem
import com.islandphone.app.core.ui.GlassPanel
import com.islandphone.app.core.ui.IslandConfirmDialog
import com.islandphone.app.core.ui.IslandPage

@Composable
fun NavDemoPage(
    level: Int,
    onOpenNext: () -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }

    IslandPage(title = stringResource(R.string.demo_page_title, level)) {
        Spacer(modifier = Modifier.height(16.dp))
        GlassPanel(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(20.dp)
        ) {
            GlassButton(
                text = stringResource(R.string.demo_page_next),
                onClick = onOpenNext,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            GlassListItem(
                title = stringResource(R.string.demo_open_confirm),
                onClick = { showConfirm = true }
            )
        }
    }

    if (showConfirm) {
        IslandConfirmDialog(
            title = stringResource(R.string.demo_confirm_question),
            confirmText = stringResource(R.string.demo_delete),
            destructive = true,
            onConfirm = {},
            onDismiss = { showConfirm = false }
        )
    }
}
