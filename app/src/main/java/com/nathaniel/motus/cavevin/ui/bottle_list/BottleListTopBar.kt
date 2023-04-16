package com.nathaniel.motus.cavevin.ui.bottle_list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleListTopBar(viewModel: BottleListViewModel, modifier: Modifier = Modifier) {
    val cellarName by viewModel.cellarName.observeAsState(initial = "")
    TopAppBar(title = {Text(text = cellarName)},
    navigationIcon = { IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Outlined.Menu, contentDescription = "")

    }})
}