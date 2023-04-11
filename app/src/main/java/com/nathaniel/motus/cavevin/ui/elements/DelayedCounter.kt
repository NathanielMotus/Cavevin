package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        CounterButton(
            onClick = { counter++ },
            enabled = isEnabled,
            resourceId = R.drawable.outline_add_24
        )

        DelayedText(count = counter, onCountChange = { onCountChange(it) })

        CounterButton(
            onClick = { if (counter > 0) counter-- },
            enabled = isEnabled,
            resourceId = R.drawable.outline_remove_24
        )
    }
}

@Composable
private fun CounterButton(
    onClick: () -> Unit,
    enabled: Boolean,
    resourceId: Int,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        Image(painter = painterResource(id = resourceId), contentDescription = "")
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
                delay(200)
                onCountChange(count)
            } else isActivated = true
        })

    Text(
        text = count.toString(),
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}
//todo : try without delay