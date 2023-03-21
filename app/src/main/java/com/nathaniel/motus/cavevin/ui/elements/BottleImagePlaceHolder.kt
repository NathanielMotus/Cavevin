package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.ui.theme.pinkWineColor
import com.nathaniel.motus.cavevin.ui.theme.redWineColor
import com.nathaniel.motus.cavevin.ui.theme.whiteWhineColor

@Composable
fun BottleImagePlaceHolder(
    wineColor: String,
    appellation: String?,
    imageSize: Int = 200,
    imagePadding: Int = 8,
    modifier: Modifier = Modifier
) {
    val placeHolderColor = when (wineColor) {
        WineColor.RED -> redWineColor
        WineColor.WHITE -> whiteWhineColor
        else -> pinkWineColor
    }
    val placeHolderName: String =
        if (appellation != "") appellation?.first().toString() else "?"
    Box(
        modifier = modifier
            .requiredSize(imageSize.dp)
            .padding(imagePadding.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = modifier
                .drawBehind {
                    drawCircle(
                        color = placeHolderColor,
                        radius = this.size.maxDimension
                    )
                },
            color = MaterialTheme.colorScheme.onPrimary,
            text = placeHolderName,
            fontSize = (imageSize / 5).sp
        )
    }
}