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
import com.nathaniel.motus.cavevin.R
import kotlinx.coroutines.delay

@Composable
fun DelayedCounter(
    count: Int, onCountChange: (Int) -> Unit, isEnabled: Boolean, modifier: Modifier = Modifier
) {
    var counter by remember { mutableStateOf(count) }
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { counter++ }, enabled = isEnabled) {
            Image(
                painter = painterResource(id = R.drawable.outline_add_24), contentDescription = ""
            )
        }

        DelayedText(count = counter, onCountChange = { onCountChange(it) })

        Button(onClick = { if (counter > 0) counter-- }, enabled = isEnabled) {
            Image(
                painter = painterResource(id = R.drawable.outline_remove_24),
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun DelayedText(
    count: Int,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isActivated by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = count,
        block = {
            if (isActivated) {
                delay(400)
                onCountChange(count)
            } else isActivated = true
        })

    Text(
        text = count.toString(),
        style = MaterialTheme.typography.displaySmall,
        modifier = modifier
    )
}
