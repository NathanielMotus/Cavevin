package com.nathaniel.motus.cavevin.ui.bottle_list

import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

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
    modifier: Modifier = Modifier
) {
    val showNewCellarDialog = remember { mutableStateOf(false) }
    val showRenameCellarDialog = remember { mutableStateOf(false) }
    DropdownMenu(expanded = menuExpanded, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(
            text = { Text(text = "New cellar") },
            onClick = { showNewCellarDialog.value = true })
        DropdownMenuItem(
            text = { Text(text = "Rename cellar") },
            onClick = { showRenameCellarDialog.value = true })
        DropdownMenuItem(text = { Text(text = "Delete cellar") }, onClick = onValidateDeleteCellar)
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
        BottleListNewCellarDialog(
            onDismissRequest = { showNewCellarDialog.value = false },
            onValidate = {
                onDismissRequest()
                onValidateNewCellar(it)
                showNewCellarDialog.value=false
            })
}