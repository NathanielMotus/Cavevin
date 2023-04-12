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
    onNavigateToDetail: (bottleId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    WineCellarMainTheme {
        Scaffold(floatingActionButton = { BottleListFloatingActionButton()}) { paddingValues ->
            BottleListContent(
                viewModel = viewModel,
                onNavigateToDetail = { onNavigateToDetail(it) },
                modifier = modifier.padding(paddingValues)
            )
        }

    }
}