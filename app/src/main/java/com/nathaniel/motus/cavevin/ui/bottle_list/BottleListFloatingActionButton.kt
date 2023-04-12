package com.nathaniel.motus.cavevin.ui.bottle_list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottleListFloatingActionButton(modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { /*TODO*/ },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = CircleShape,
        modifier = modifier.padding(PaddingValues(start = 0.dp, end = 75.dp))
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")

    }
}