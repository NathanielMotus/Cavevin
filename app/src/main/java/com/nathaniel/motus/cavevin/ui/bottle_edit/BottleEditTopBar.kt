package com.nathaniel.motus.cavevin.ui.bottle_edit

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleEditTopBar(viewModel: BottleDetailViewModel, modifier: Modifier = Modifier) {
    val activity = LocalContext.current as Activity
    TopAppBar(title = { Text(text = stringResource(R.string.edit_bottle)) },
        navigationIcon = {
            IconButton(onClick = { activity.onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")

            }
        },
        actions = {
            IconButton(onClick = { activity.onBackPressed() }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "")

            }
            IconButton(onClick = {
                viewModel.onValidate()
                activity.onBackPressed()
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")

            }
        }
    )
}