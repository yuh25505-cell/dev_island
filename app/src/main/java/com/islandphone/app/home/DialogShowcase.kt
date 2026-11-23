package com.islandphone.app.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme
import com.islandphone.app.core.ui.GlassListItem
import com.islandphone.app.core.ui.GlassPanel
import com.islandphone.app.core.ui.IslandBottomSheet
import com.islandphone.app.core.ui.IslandChoiceDialog
import com.islandphone.app.core.ui.IslandConfirmDialog
import com.islandphone.app.core.ui.IslandInputDialog

@Composable
fun DialogShowcase(modifier: Modifier = Modifier) {
    val colors = IslandTheme.colors
    var showConfirm by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    var showInput by remember { mutableStateOf(false) }
    var showChoice by remember { mutableStateOf(false) }
    var choiceIndex by remember { mutableIntStateOf(0) }

    val chevron: @Composable () -> Unit = {
        Text(
            text = "›",
            style = MaterialTheme.typography.titleLarge,
            color = colors.textSecondary
        )
    }

    GlassPanel(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(20.dp)
    ) {
        Text(
            text = stringResource(R.string.demo_dialog_title),
            style = MaterialTheme.typography.titleMedium,
            color = colors.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        GlassListItem(
            title = stringResource(R.string.demo_open_confirm),
            onClick = { showConfirm = true },
            trailing = chevron
        )
        Spacer(modifier = Modifier.height(12.dp))
        GlassListItem(
            title = stringResource(R.string.demo_open_sheet),
            onClick = { showSheet = true },
            trailing = chevron
        )
        Spacer(modifier = Modifier.height(12.dp))
        GlassListItem(
            title = stringResource(R.string.demo_open_input),
            onClick = { showInput = true },
            trailing = chevron
        )
        Spacer(modifier = Modifier.height(12.dp))
        GlassListItem(
            title = stringResource(R.string.demo_open_choice),
            onClick = { showChoice = true },
            trailing = chevron
        )
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

    if (showSheet) {
        IslandBottomSheet(onDismiss = { showSheet = false }) { dismiss ->
            Text(
                text = stringResource(R.string.demo_sheet_title),
                style = MaterialTheme.typography.titleMedium,
                color = colors.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            GlassListItem(
                title = stringResource(R.string.demo_choice_a),
                onClick = dismiss
            )
            Spacer(modifier = Modifier.height(12.dp))
            GlassListItem(
                title = stringResource(R.string.demo_choice_b),
                onClick = dismiss
            )
            Spacer(modifier = Modifier.height(12.dp))
            GlassListItem(
                title = stringResource(R.string.demo_choice_c),
                onClick = dismiss
            )
        }
    }

    if (showInput) {
        IslandInputDialog(
            title = stringResource(R.string.demo_input_title),
            onConfirm = {},
            onDismiss = { showInput = false }
        )
    }

    if (showChoice) {
        IslandChoiceDialog(
            title = stringResource(R.string.demo_choice_title),
            options = listOf(
                stringResource(R.string.demo_choice_a),
                stringResource(R.string.demo_choice_b),
                stringResource(R.string.demo_choice_c)
            ),
            selectedIndex = choiceIndex,
            onSelect = { choiceIndex = it },
            onDismiss = { showChoice = false }
        )
    }
}
