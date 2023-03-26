package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.data.cellar_database.WineStillness
import com.nathaniel.motus.cavevin.ui.theme.pinkWineColor
import com.nathaniel.motus.cavevin.ui.theme.redWineColor
import com.nathaniel.motus.cavevin.ui.theme.whiteWhineColor

@Composable
fun WineGlass(
    wineColor: String,
    wineStillness: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = if (wineStillness ==
            WineStillness.STILL
        ) painterResource(id = R.drawable.ic_baseline_wine_bar_full_48) else painterResource(
            id = R.drawable.ic_baseline_wine_bar_full_sparkling_48
        ), contentDescription = "", colorFilter = ColorFilter.tint(
            when (wineColor) {
                WineColor.RED -> redWineColor
                WineColor.WHITE -> whiteWhineColor
                else -> pinkWineColor
            }
        ),
        modifier = modifier.size(24.dp)
    )
}