package com.islandphone.app.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme

@Composable
fun IslandConfirmDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = stringResource(R.string.dialog_confirm),
    cancelText: String = stringResource(R.string.dialog_cancel),
    destructive: Boolean = false
) {
    val colors = IslandTheme.colors
    IslandDialogHost(onDismissRequest = onDismiss) { dismiss ->
        IslandDialogSurface(
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .widthIn(max = 360.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassButton(
                        text = cancelText,
                        onClick = dismiss,
                        modifier = Modifier.weight(1f),
                        style = GlassButtonStyle.Glass
                    )
                    GlassButton(
                        text = confirmText,
                        onClick = {
                            onConfirm()
                            dismiss()
                        },
                        modifier = Modifier.weight(1f),
                        style = if (destructive) GlassButtonStyle.Danger else GlassButtonStyle.Primary
                    )
                }
            }
        }
    }
}
