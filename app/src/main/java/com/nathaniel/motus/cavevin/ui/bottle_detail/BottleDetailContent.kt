package com.nathaniel.motus.cavevin.ui.bottle_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.data.cellar_database.WineStillness
import com.nathaniel.motus.cavevin.ui.elements.*
import com.nathaniel.motus.cavevin.utils.defaultCurrencyCode
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel
import com.nathaniel.motus.cavevin.R


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun BottleDetailContent(
    viewModel: BottleDetailViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        val bottleImageBitmap by viewModel.bottleImageBitmap.observeAsState(initial = null)
        val bottleImageUri by viewModel.bottleImageUri.observeAsState(initial = null)
        val rating by viewModel.rating.observeAsState(initial = 0)
        val appellation by viewModel.appellation.observeAsState(initial = null)
        val domain by viewModel.domain.observeAsState(initial = null)
        val cuvee by viewModel.cuvee.observeAsState(initial = null)
        val wineColor by viewModel.wineColor.observeAsState(initial = WineColor.RED)
        val wineStillness by viewModel.wineStillness.observeAsState(initial = WineStillness.STILL)
        val vintage by viewModel.vintage.observeAsState(initial = null)
        val agingCapacity by viewModel.agingCapacity.observeAsState(initial = null)
        val price by viewModel.price.observeAsState(initial = null)
        val currency by viewModel.currency.observeAsState(initial = defaultCurrencyCode())
        val origin by viewModel.origin.observeAsState(initial = null)
        val comment by viewModel.comment.observeAsState(initial = null)
        val bottleTypeAndCapacity by viewModel.selectedBottleTypeItem.observeAsState(
            initial = Pair(
                0,
                ""
            )
        )

        Box(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            BottleRepresentation(
                bottleImageBitmap = bottleImageBitmap,
                bottleImageUri = bottleImageUri,
                wineColor = wineColor,
                appellation = appellation
            )
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            RatingBar(onRatingChange = {}, rating = rating, isEditable = false, size = 48)
        }

        BottleDetailCard {
            MainWineData(
                appellation = appellation,
                domain = domain,
                cuvee = cuvee,
                wineColor = wineColor,
                wineStillness = wineStillness,
                vintage = vintage,
                bottleTypeAndCapacity = bottleTypeAndCapacity
            )
        }

        if (agingCapacity != null || price != null)
            BottleDetailCard {
                SecondaryWineData(agingCapacity = agingCapacity, price = price, currency = currency)
            }

        if (origin != null && origin != "")
            BottleDetailCard {
                Column {
                    Text(
                        text = stringResource(id = R.string.origin),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = origin!!, style = MaterialTheme.typography.bodyLarge)
                }
            }

        if (comment != null && comment != "")
            BottleDetailCard {
                Column {
                    Text(
                        text = stringResource(id = R.string.comments),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = comment!!, style = MaterialTheme.typography.bodyLarge)
                }
            }

        BottleCard(
            bottleImageBitmap = bottleImageBitmap,
            bottleImageUri = bottleImageUri,
            appellation = appellation,
            domain = domain,
            cuvee = cuvee,
            vintage = vintage,
            bottleTypeAndCapacity = bottleTypeAndCapacity,
            wineColor = wineColor,
            wineStillness = wineStillness,
            rating = rating,
            stock = 88
        )

    }
}