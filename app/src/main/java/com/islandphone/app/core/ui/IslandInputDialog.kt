package com.islandphone.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.islandphone.app.R
import com.islandphone.app.core.theme.IslandTheme
import kotlinx.coroutines.delay

@Composable
fun IslandInputDialog(
    title: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    initialValue: String = "",
    hint: String? = null,
    singleLine: Boolean = true,
    allowEmpty: Boolean = false,
    confirmText: String = stringResource(R.string.dialog_confirm),
    cancelText: String = stringResource(R.string.dialog_cancel)
) {
    val colors = IslandTheme.colors
    var field by remember {
        mutableStateOf(TextFieldValue(initialValue, TextRange(initialValue.length)))
    }
    val focusRequester = remember { FocusRequester() }
    val canConfirm = allowEmpty || field.text.isNotBlank()
    val fieldShape = RoundedCornerShape(20.dp)

    IslandDialogHost(onDismissRequest = onDismiss) { dismiss ->
        LaunchedEffect(Unit) {
            delay(150)
            runCatching { focusRequester.requestFocus() }
        }
        IslandDialogSurface(
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .imePadding()
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
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(fieldShape)
                        .background(colors.glassFill)
                        .border(1.dp, colors.glassBorder, fieldShape)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    if (field.text.isEmpty() && hint != null) {
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.textSecondary
                        )
                    }
                    BasicTextField(
                        value = field,
                        onValueChange = { field = it },
                        singleLine = singleLine,
                        maxLines = if (singleLine) 1 else 6,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = colors.onBackground),
                        cursorBrush = SolidColor(colors.accent),
                        keyboardOptions = KeyboardOptions(
                            imeAction = if (singleLine) ImeAction.Done else ImeAction.Default
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (canConfirm) {
                                    onConfirm(field.text)
                                    dismiss()
                                }
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )
                }
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
                            onConfirm(field.text)
                            dismiss()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = canConfirm
                    )
                }
            }
        }
    }
}
