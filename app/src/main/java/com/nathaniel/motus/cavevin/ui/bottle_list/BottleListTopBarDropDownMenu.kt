package com.nathaniel.motus.cavevin.ui.bottle_list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.nathaniel.motus.cavevin.ui.elements.AlertDialogWithTextField

@Composable
fun BottleListTopBarDropDownMenu(
    onValidateNewCellar: (String) -> Unit,
    onValidateRenameCellar: (String) -> Unit,
    onValidateDeleteCellar: () -> Unit,
    onValidateBackUp: () -> Unit,
    onValidateRestoreBackUp: () -> Unit,
    onValidateExport: () -> Unit,
    menuExpanded: Boolean,
    onDismissRequest: () -> Unit,
    cellarName: String,
    modifier: Modifier = Modifier
) {
    val showNewCellarDialog = remember { mutableStateOf(false) }
    val showRenameCellarDialog = remember { mutableStateOf(false) }
    val showDeleteCellarDialog = remember { mutableStateOf(false) }

    DropdownMenu(expanded = menuExpanded, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(
            text = { Text(text = "New cellar") },
            onClick = { showNewCellarDialog.value = true })
        DropdownMenuItem(
            text = { Text(text = "Rename cellar") },
            onClick = { showRenameCellarDialog.value = true })
        DropdownMenuItem(
            text = { Text(text = "Delete cellar") },
            onClick = { showDeleteCellarDialog.value = true })
        Divider()
        DropdownMenuItem(text = { Text(text = "Backup") }, onClick = onValidateBackUp)
        DropdownMenuItem(
            text = { Text(text = "Restore backup") },
            onClick = onValidateRestoreBackUp
        )
        Divider()
        DropdownMenuItem(text = { Text(text = "Export") }, onClick = onValidateExport)
    }
    if (showNewCellarDialog.value)
        AlertDialogWithTextField(
            title = "New cellar",
            label = "Cellar name",
            onDismissRequest = { showNewCellarDialog.value = false },
            onValidate = {
                onDismissRequest()
                onValidateNewCellar(it)
                showNewCellarDialog.value = false
            }
        )

    if (showRenameCellarDialog.value)
        AlertDialogWithTextField(
            title = "Rename cellar",
            input = cellarName,
            label = "Cellar name",
            onDismissRequest = { showRenameCellarDialog.value = false },
            onValidate = {
                onDismissRequest()
                onValidateRenameCellar(it)
                showRenameCellarDialog.value = false
            }
        )

    if (showDeleteCellarDialog.value)
        AlertDialog(
            onDismissRequest = { showDeleteCellarDialog.value = false },
            confirmButton = {
                IconButton(onClick = {
                    onDismissRequest()
                    onValidateDeleteCellar()
                    showDeleteCellarDialog.value=false
                }) {
                    Icon(imageVector = Icons.Outlined.Check, contentDescription = "")

                }
            },
            dismissButton = {
                IconButton(onClick = { showDeleteCellarDialog.value = false }) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "")

                }
            },
            title = {
                Text(
                    text = "Delete cellar"
                )
            },
            text = { Text(text = "Are you sur you want to delete the current cellar ? This cannot be undone") })
}