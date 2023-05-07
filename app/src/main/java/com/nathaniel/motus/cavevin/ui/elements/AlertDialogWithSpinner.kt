package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogWithSpinner(
    title: String?,
    items: List<Pair<Int, String>>,
    selectedItem: Pair<Int, String>,
    onDismissRequest: () -> Unit,
    onSelectionChanged: (Pair<Int, String>) -> Unit,
    labelText: String = "Label",
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = modifier.padding(16.dp)) {
                if (title != null) {
                    Text(text = title, style = MaterialTheme.typography.displaySmall)
                    Spacer(modifier = Modifier.size(16.dp))
                }

                PairSpinner(
                    itemList = items,
                    selectedItem = selectedItem,
                    onSelectionChanged = onSelectionChanged,
                    labelText = labelText
                )
            }
        }
    }
}