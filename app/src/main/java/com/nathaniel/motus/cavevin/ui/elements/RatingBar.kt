package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.nathaniel.motus.cavevin.R

@Composable
fun RatingBar(
    rating: Int = 0,
    isEditable: Boolean = false,
    onRatingChange: (rating: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row() {
        if (isEditable)
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_clear_48),
                contentDescription = "Clear",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = modifier.clickable { onRatingChange(0) })
        for (index in 0..4) {
            RatingStar(index = index,
                isEditable = isEditable,
                isOn = rating > index,
                onClick = { index: Int ->
                    onRatingChange(index)
                })
        }
    }
}

@Composable
private fun RatingStar(
    index: Int,
    isEditable: Boolean,
    isOn: Boolean = true,
    onClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = if (isOn) painterResource(id = R.drawable.ic_baseline_star_rate_48) else painterResource(
            id = R.drawable.ic_baseline_star_outline_48
        ),
        contentDescription = "",
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
        modifier = if (isEditable) modifier.clickable { onClick(index + 1) } else modifier
    )
}