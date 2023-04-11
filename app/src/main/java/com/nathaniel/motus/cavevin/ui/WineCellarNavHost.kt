package com.nathaniel.motus.cavevin.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nathaniel.motus.cavevin.ui.bottle_detail.BottleDetailScreen
import com.nathaniel.motus.cavevin.ui.bottle_edit.BottleEditScreen
import com.nathaniel.motus.cavevin.ui.bottle_list.BottleListScreen
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun WineCellarNavHost(
    bottleListViewModel: BottleListViewModel,
    bottleDetailViewModel: BottleDetailViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "list"
) {
    NavHost(modifier = modifier, navController = navController, startDestination = startDestination)
    {
        composable("list") {
            BottleListScreen(
                viewModel = bottleListViewModel,
                onNavigateToDetail = {
                    bottleDetailViewModel.updateBottleId(it)
                    navController.navigate("detail")
                })
        }
        composable("detail") {
            BottleDetailScreen(
                viewModel = bottleDetailViewModel,
                onEdit = { navController.navigate("edit") })
        }
        composable("edit") { BottleEditScreen(viewModel = bottleDetailViewModel) }
    }
}