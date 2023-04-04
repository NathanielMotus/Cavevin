package com.nathaniel.motus.cavevin.ui.bottle_list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nathaniel.motus.cavevin.ui.theme.WineCellarMainTheme
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleListScreen(
    viewModel: BottleListViewModel,
    modifier: Modifier = Modifier
) {
    WineCellarMainTheme {
        Scaffold() { paddingValues ->
            BottleListContent(viewModel = viewModel, modifier = modifier.padding(paddingValues), onCountChange = { println("Count : $it")})

        }

    }
}