package com.nathaniel.motus.cavevin.ui.bottle_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.nathaniel.motus.cavevin.ui.elements.BottleCard
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel

@Composable
fun BottleListContent(
    viewModel: BottleListViewModel,
    modifier: Modifier = Modifier,
    onCountChange:(Int)->Unit={}
) {
    val cellarItems by viewModel.cellarItems.observeAsState(initial = null)

    LazyColumn(modifier=modifier){
        cellarItems?.forEach {
            item { BottleCard(
                bottleImageBitmap = it.bottleImageBitmap,
                bottleImageUri = it.bottleImageUri,
                appellation = it.appellation,
                domain = it.domain,
                cuvee = it.cuvee,
                vintage = it.vintage,
                bottleTypeAndCapacity = it.bottleTypeAndCapacity,
                wineColor = it.wineColor,
                wineStillness = it.wineStillness,
                rating = it.rating,
                stock =it.stock,
                onCountChange={onCountChange(it)}
            ) }
        }
    }
}