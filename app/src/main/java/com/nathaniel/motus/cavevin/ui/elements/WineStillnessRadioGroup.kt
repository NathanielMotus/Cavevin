package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.data.cellar_database.WineStillness

@Composable
fun WineStillnessRadioGroup(
    selectedWineStillness: String,
    onWineStillnessChange: (wineStillness: String) -> Unit,
    stillWineTranslation: String,
    sparklingWineTranslation: String,
    modifier: Modifier = Modifier
) {
    Row() {
        WineStillnessRadioButton(
            wineStillness = WineStillness.STILL,
            wineStillnessTranslation = stillWineTranslation,
            selectedWineStillness = selectedWineStillness,
            iconId = R.drawable.ic_baseline_wine_bar_full_48,
            onWineStillnessChange = { wineStillness: String -> onWineStillnessChange(wineStillness) }
        )
        WineStillnessRadioButton(
            wineStillness = WineStillness.SPARKLING,
            wineStillnessTranslation = sparklingWineTranslation,
            selectedWineStillness = selectedWineStillness,
            iconId = R.drawable.ic_baseline_wine_bar_full_sparkling_48,
            onWineStillnessChange = { wineStillness: String -> onWineStillnessChange(wineStillness) }
        )
    }
}

@Composable
private fun WineStillnessRadioButton(
    wineStillness: String,
    wineStillnessTranslation: String,
    selectedWineStillness: String,
    iconId: Int,
    onWineStillnessChange: (wineStillness: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = wineStillness == selectedWineStillness,
            onClick = { onWineStillnessChange(wineStillness) })
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = modifier.size(24.dp)
        )
        Text(text = wineStillnessTranslation)
    }
}
