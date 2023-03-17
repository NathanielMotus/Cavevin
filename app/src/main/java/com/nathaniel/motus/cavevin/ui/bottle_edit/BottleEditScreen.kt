package com.nathaniel.motus.cavevin.ui.bottle_edit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nathaniel.motus.cavevin.ui.theme.WineCellarMainTheme
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleEditScreen(
    viewModel: BottleDetailViewModel,
    modifier: Modifier = Modifier
) {
    WineCellarMainTheme() {
        Scaffold(topBar = { TopAppBar(title = { Text(text = "Edit") }) }) { paddingValues ->
            BottleEditContent(viewModel = viewModel, modifier = modifier.padding(paddingValues))

        }
    }
}