package com.nathaniel.motus.cavevin.ui.bottle_detail

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleDetailTopBar(
    viewModel: BottleDetailViewModel,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as AppCompatActivity
    val barTitle by viewModel.appellation.observeAsState(initial = "")
    TopAppBar(title = { Text(text = barTitle) }, navigationIcon = {
        IconButton(onClick = { activity.onBackPressedDispatcher.onBackPressed() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")

        }
    },
        actions = {
            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "")
            }
        })
}