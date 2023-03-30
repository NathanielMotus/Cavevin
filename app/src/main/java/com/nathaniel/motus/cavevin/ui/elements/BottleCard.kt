package com.nathaniel.motus.cavevin.ui.elements

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor

@Composable
fun BottleCard(
    bottleImageBitmap: Bitmap?,
    bottleImageUri: Uri?,
    appellation: String?,
    domain: String?,
    cuvee: String?,
    vintage: Int?,
    bottleTypeAndCapacity: Pair<Int, String>,
    wineColor: String,
    wineStillness: String,
    rating: Int,
    stock: Int,
    imageSize: Int = 120,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = modifier.padding(4.dp)
        ) {
            BottleRepresentation(
                bottleImageBitmap = bottleImageBitmap,
                bottleImageUri = bottleImageUri,
                wineColor = wineColor,
                appellation = appellation,
                imageSize = imageSize
            )

            Column() {
                MainWineData(
                    appellation = appellation,
                    domain = domain,
                    cuvee = cuvee,
                    wineColor = wineColor,
                    wineStillness = wineStillness,
                    vintage = vintage,
                    bottleTypeAndCapacity = bottleTypeAndCapacity
                )
                RatingBar(onRatingChange = {}, rating = rating, size = 24)
            }

            DelayedCounter(count = stock, onCountChange = {}, isEnabled = true)
        }
    }
}