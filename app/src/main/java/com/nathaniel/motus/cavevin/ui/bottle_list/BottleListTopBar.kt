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
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleListTopBar(viewModel: BottleListViewModel, modifier: Modifier = Modifier) {
    val cellarName by viewModel.cellarName.observeAsState(initial = "")
    val menuExpanded = remember { mutableStateOf(false) }
    TopAppBar(title = { Text(text = cellarName) },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Menu, contentDescription = "")

            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_warehouse_24),
                    contentDescription = ""
                )
            }
            IconButton(onClick = { menuExpanded.value = !menuExpanded.value }) {
                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "")
            }
            BottleListTopBarDropDownMenu(
                onNewCellarClick = {},
                onDeleteCellarClick = {},
                onBackUpClick = {},
                onRestoreBackUpClick = {},
                onExportClick = {},
                menuExpanded = menuExpanded.value,
                onDismissRequest = { menuExpanded.value = false })
        })
}