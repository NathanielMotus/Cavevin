package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.ui.theme.pinkWineColor
import com.nathaniel.motus.cavevin.ui.theme.redWineColor
import com.nathaniel.motus.cavevin.ui.theme.whiteWhineColor

@Composable
fun WineColorRadioGroup(
    selectedWineColor: String,
    onWineColorChange: (wineColor: String) -> Unit,
    redWineTranslation: String = "RED",
    whiteWineTranslation: String = "WHITE",
    pinkWineTranslation: String = "PINK",
    modifier: Modifier = Modifier
) {
    Row() {
        WineColorRadioButton(
            wineColor = WineColor.RED,
            wineColorTranslation = redWineTranslation,
            wineColorTint = redWineColor,
            selectedWineColor = selectedWineColor,
            onWineColorChange = { onWineColorChange(WineColor.RED) }
        )
        WineColorRadioButton(
            wineColor = WineColor.WHITE,
            wineColorTranslation = whiteWineTranslation,
            wineColorTint = whiteWhineColor,
            selectedWineColor = selectedWineColor,
            onWineColorChange = { onWineColorChange(WineColor.WHITE) }
        )
        WineColorRadioButton(
            wineColor = WineColor.PINK,
            wineColorTranslation = pinkWineTranslation,
            wineColorTint = pinkWineColor,
            selectedWineColor = selectedWineColor,
            onWineColorChange = { onWineColorChange(WineColor.PINK) }
        )
    }
}

@Composable
private fun WineColorRadioButton(
    wineColor: String,
    wineColorTranslation: String,
    wineColorTint: Color,
    selectedWineColor: String,
    onWineColorChange: (wineColor: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedWineColor == wineColor,
            onClick = { onWineColorChange(wineColor) })
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_wine_bar_full_48),
            contentDescription = "",
            colorFilter = ColorFilter.tint(wineColorTint),
            modifier = modifier.size(24.dp)
        )
        Text(text = wineColorTranslation)
    }
}
