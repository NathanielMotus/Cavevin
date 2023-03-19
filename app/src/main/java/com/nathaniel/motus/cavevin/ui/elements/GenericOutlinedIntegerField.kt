package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericOutlinedIntegerField(
    value: Int?,
    onValueChange: (value: Int?) -> Unit,
    labelText: String = "",
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { onValueChange(it.toIntOrNull()) },
        label = {
            Text(
                text = labelText
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier=modifier
    )
}