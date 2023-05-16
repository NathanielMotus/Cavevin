package com.nathaniel.motus.cavevin.ui.bottle_list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.data.cellar_database.Cellar
import com.nathaniel.motus.cavevin.ui.elements.AlertDialogWithSpinner
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleListTopBar(viewModel: BottleListViewModel, modifier: Modifier = Modifier) {
    val cellarId by viewModel.currentCellarId.observeAsState(initial = 1)
    val cellarName by viewModel.cellarName.observeAsState(initial = "")
    val menuExpanded = remember { mutableStateOf(false) }
    val cellarsForSpinner by viewModel.cellarsForSpinner.observeAsState(initial = null)
    val selectedCellarForSpinner by viewModel.selectedCellarForSpinner.observeAsState(initial = null)
    TopAppBar(title = { Text(text = cellarName) },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Menu, contentDescription = "")

            }
        },
        actions = {
            var showSpinner = remember { mutableStateOf(false) }

            if (cellarsForSpinner != null)
                IconButton(onClick = { showSpinner.value = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_warehouse_24),
                        contentDescription = ""
                    )
                    if (showSpinner.value)
                        AlertDialogWithSpinner(
                            title = "Change cellar",
                            items = cellarsForSpinner!!,
                            selectedItem = selectedCellarForSpinner!!,
                            onDismissRequest = { showSpinner.value = false },
                            onSelectionChanged = { cellar: Pair<Int, String> ->
                                run {
                                    viewModel.onCellarIdChange(cellar.first)
                                    showSpinner.value = false
                                }
                            }
                        )
                }
            IconButton(onClick = { menuExpanded.value = !menuExpanded.value }) {
                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "")
            }
            BottleListTopBarDropDownMenu(
                onValidateNewCellar = { viewModel.insertAndOpenCellar(Cellar(0, it)) },
                onValidateRenameCellar = { viewModel.updateCellar(Cellar(cellarId, it)) },
                onValidateDeleteCellar = {viewModel.clearAndDeleteCellar(cellarId)},
                onValidateBackUp = {},
                onValidateRestoreBackUp = {},
                onValidateExport = {},
                menuExpanded = menuExpanded.value,
                onDismissRequest = { menuExpanded.value = false },
                cellarName = cellarName
            )
        }
    )
}