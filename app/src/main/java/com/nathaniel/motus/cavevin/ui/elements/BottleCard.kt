package com.nathaniel.motus.cavevin.ui.elements

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

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
    modifier: Modifier=Modifier,
    imageSize: Int = 80
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
    //and max composable height as constraints
    content: @Composable () -> Unit
) = Layout(content = content) { measurables, constraints ->

    val measurableWidth: MutableList<Int> = mutableListOf()
    measurableWidth.add(measurables.first().minIntrinsicWidth(constraints.maxHeight))
    measurableWidth.add(measurables.last().minIntrinsicWidth(constraints.maxHeight))

    val midHeight = measurables.maxOfOrNull {
        it.minIntrinsicHeight(
            constraints.maxWidth - measurableWidth.first() - measurableWidth.last()
        )
    }

    if (measurables.size > 2)
        for (i in 1..measurables.size - 2)
            measurableWidth.add(
                measurableWidth.size - 1,
                (constraints.maxWidth - measurableWidth.first() - measurableWidth.last()) / (measurables.size - 2)
            )

    val placeables: MutableList<Placeable> = mutableListOf()
    for (i in measurables.indices)
        placeables.add(
            measurables[i].measure(
                Constraints(
                    minWidth = measurableWidth[i],
                    maxWidth = measurableWidth[i],
                    maxHeight = midHeight ?: 0
                )
            )
        )

    layout(constraints.maxWidth, midHeight ?: 0) {
        var xPosition = 0

        placeables.forEach {
            it.placeRelative(xPosition, 0)
            xPosition += it.width
        }

    }
}