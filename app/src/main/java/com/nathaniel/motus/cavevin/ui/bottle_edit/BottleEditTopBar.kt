package com.nathaniel.motus.cavevin.ui.bottle_edit

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleEditTopBar(viewModel: BottleDetailViewModel, modifier: Modifier = Modifier) {
    val activity = LocalContext.current as AppCompatActivity
    TopAppBar(
        title = { Text(text = stringResource(R.string.edit_bottle)) },
        navigationIcon = {
            IconButton(onClick = { activity.onBackPressedDispatcher.onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")

            }
        },
        actions = {
            IconButton(onClick = {
                viewModel.loadBottleDetailViewModel()
                activity.onBackPressedDispatcher.onBackPressed()
            }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "")

            }
            IconButton(onClick = {
                viewModel.onValidate()
                activity.onBackPressedDispatcher.onBackPressed()
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")

            }
        },
        modifier = modifier
    )
}