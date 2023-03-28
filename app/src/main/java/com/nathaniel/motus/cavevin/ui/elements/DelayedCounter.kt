package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.LifecycleCoroutineScope
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.data.cellar_database.Stock
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.util.*
import kotlin.coroutines.coroutineContext

@Composable
fun DelayedCounter(
    count: Int,
    onCountChange: (Int) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    var counter = remember { mutableStateOf(count) }
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { counter.value++}, enabled = isEnabled) {
            Image(
                painter = painterResource(id = R.drawable.outline_add_24),
                contentDescription = ""
            )
        }

        Text(text = counter.value.toString(), style = MaterialTheme.typography.displaySmall)

        Button(onClick = { if(counter.value>0)counter.value-- }, enabled = isEnabled) {
            Image(
                painter = painterResource(id = R.drawable.outline_remove_24),
                contentDescription = ""
            )
        }
    }
}
