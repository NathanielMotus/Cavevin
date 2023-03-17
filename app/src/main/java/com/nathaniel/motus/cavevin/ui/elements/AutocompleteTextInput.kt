package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutocompleteTextInput(
    value: String,
    onValueChange: (value: String) -> Unit,
    suggestions: List<String>,
    labelText: String = "",
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = value,
            onValueChange = { value: String -> onValueChange(value) },
            label = { Text(text = labelText) },
            modifier = modifier
                .menuAnchor(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                expanded = false
            })
        )

        val filterSuggestions =
            suggestions.filter { it.contains(value, ignoreCase = true) }.sorted()
        if (filterSuggestions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = modifier.exposedDropdownSize(matchTextFieldWidth = true)
            ) {
                filterSuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        onClick = {
                            onValueChange(suggestion)
                            expanded = false
                            focusManager.clearFocus()
                        },
                        text = { Text(text = suggestion) })
                }

            }
        }
    }
}