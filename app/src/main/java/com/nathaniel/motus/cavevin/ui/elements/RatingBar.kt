package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nathaniel.motus.cavevin.R

@Composable
fun RatingBar(
    onRatingChange: (rating: Int) -> Unit,
    modifier: Modifier = Modifier,
    rating: Int = 0,
    isEditable: Boolean = false,
    size: Int = 24
) {
    Row(modifier = modifier.padding(4.dp)) {
        if (isEditable)
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_clear_48),
                contentDescription = "Clear",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = modifier
                    .clickable { onRatingChange(0) }
                    .size(size = size.dp))
        for (index in 0..4) {
            RatingStar(
                index = index,
                isEditable = isEditable,
                isOn = rating > index,
                onClick = { pos: Int ->
                    onRatingChange(pos)
                },
                size = size
            )
        }
    }
}

@Composable
private fun RatingStar(
    index: Int,
    isEditable: Boolean,
    onClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    isOn: Boolean=true,
    size: Int = 24
) {
    Image(
        painter = if (isOn) painterResource(id = R.drawable.ic_baseline_star_rate_48) else painterResource(
            id = R.drawable.ic_baseline_star_outline_48
        ),
        contentDescription = "",
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
        modifier = if (isEditable) modifier
            .clickable { onClick(index + 1) }
            .size(size = size.dp)
        else modifier
            .size(size = size.dp)
    )
}