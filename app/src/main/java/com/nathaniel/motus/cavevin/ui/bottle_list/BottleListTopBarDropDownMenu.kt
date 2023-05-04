package com.nathaniel.motus.cavevin.ui.bottle_list

import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottleListTopBarDropDownMenu(
    onNewCellarClick: () -> Unit,
    onDeleteCellarClick: () -> Unit,
    onBackUpClick: () -> Unit,
    onRestoreBackUpClick: () -> Unit,
    onExportClick: () -> Unit,
    menuExpanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(expanded = menuExpanded, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(text = { Text(text = "New cellar") }, onClick = onNewCellarClick)
        DropdownMenuItem(text = { Text(text = "Delete cellar") }, onClick = onDeleteCellarClick)
        Divider()
        DropdownMenuItem(text = { Text(text = "Backup") }, onClick = onBackUpClick)
        DropdownMenuItem(text = { Text(text = "Restore backup") }, onClick = onRestoreBackUpClick)
        Divider()
        DropdownMenuItem(text = { Text(text = "Export") }, onClick = onExportClick)
    }
    BottleListNewCellarDialog(onDismissRequest = { /*TODO*/ }, onValidate = { println(it)})
}