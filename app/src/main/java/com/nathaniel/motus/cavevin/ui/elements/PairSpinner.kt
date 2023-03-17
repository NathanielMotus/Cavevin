package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PairSpinner(
    itemList: List<Pair<Int, String>>,
    selectedItem: Pair<Int, String>,
    onSelectionChanged: (Pair<Int, String>) -> Unit,
    labelText: String = "Label",
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {

        TextField(
            readOnly = true,
            value = selectedItem.second,
            onValueChange = {},
            label = {
                Text(
                    text = labelText
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier
                .exposedDropdownSize(matchTextFieldWidth = true)
                .requiredSize(300.dp)
        ) {
            itemList.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.second) },
                    onClick = {
                        onSelectionChanged(item)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}