package com.nathaniel.motus.cavevin.ui.bottle_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nathaniel.motus.cavevin.ui.theme.WineCellarMainTheme
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleDetailScreen(
    viewModel: BottleDetailViewModel,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    WineCellarMainTheme() {
        Scaffold(topBar = {
            BottleDetailTopBar(
                viewModel = viewModel,
                onEdit = onEdit
            )
        }) { paddingValues ->
            BottleDetailContent(viewModel = viewModel, modifier = modifier.padding(paddingValues))
        }
    }
}