package com.nathaniel.motus.cavevin.ui.elements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.utils.defaultCurrencyCode
import java.util.Currency

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun SecondaryWineData(
    agingCapacity: Int?,
    price: Double?,
    currency: String?,
    modifier: Modifier = Modifier
) {
    Column() {
        if (agingCapacity != null)
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_event_available_48),
                    contentDescription = "",
                    modifier = modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = modifier.size(8.dp))
                Text(text = "$agingCapacity", style = MaterialTheme.typography.bodyLarge)
            }

        if (price != null) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.outline_receipt_long_24),
                    contentDescription = "",
                    modifier = modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = modifier.size(8.dp))
                Text(
                    text = if (currency!=null && currency.length==3) "$price ${Currency.getInstance(currency).symbol}"
                    else "$price ${Currency.getInstance(defaultCurrencyCode()).symbol}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}