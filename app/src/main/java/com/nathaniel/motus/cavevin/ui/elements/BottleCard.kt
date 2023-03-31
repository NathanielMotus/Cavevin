package com.nathaniel.motus.cavevin.ui.elements

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.Placeable
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
        CardLayout {
            Box(modifier = modifier.padding(top = 8.dp)) {
                BottleRepresentation(
                    bottleImageBitmap = bottleImageBitmap,
                    bottleImageUri = bottleImageUri,
                    wineColor = wineColor,
                    appellation = appellation,
                    imageSize = imageSize
                )
            }

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

@Composable
private fun CardLayout(
    //Force first and last composables width as constraints
    //and middle composable height as constraints
    //todo optimize
    content: @Composable () -> Unit
) = Layout(content = content) { measurables, constraints ->
    val firstWidth = measurables.first().minIntrinsicWidth(constraints.maxHeight)
    val lastWidth = measurables.last().minIntrinsicWidth(constraints.maxHeight)
    val midHeight = measurables[1].minIntrinsicHeight(constraints.maxWidth - firstWidth - lastWidth)

    val placeables: MutableList<Placeable> = mutableListOf()
    for (i in 0..measurables.size - 1)
        if (i == 0) placeables.add(
            measurables[i].measure(
                Constraints(
                    maxWidth = constraints.maxWidth,
                    maxHeight = midHeight
                )
            )
        )
        else if (i in 1..measurables.size - 2) placeables.add(
            measurables[i].measure(
                Constraints(
                    maxWidth = constraints.maxWidth - firstWidth - lastWidth,
                    maxHeight = constraints.maxHeight
                )
            )
        )
        else placeables.add(
            measurables[i].measure(
                Constraints(
                    maxWidth = constraints.maxWidth,
                    maxHeight = midHeight
                )
            )
        )


    layout(constraints.maxWidth, midHeight) {
        var xPosition = 0

        for (i in 0..placeables.size - 2) {
            placeables[i].placeRelative(xPosition, 0)
            xPosition += placeables[i].width
        }

        xPosition = constraints.maxWidth - placeables.last().width
        placeables.last().placeRelative(xPosition, 0)

    }
}