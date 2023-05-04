package com.nathaniel.motus.cavevin.ui.bottle_list

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleListNewCellarDialog(
    onDismissRequest: () -> Unit,
    onValidate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = modifier.padding(16.dp)) {
                var cellarName= remember { mutableStateOf("")}
                Text(text = "New cellar", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(value = cellarName.value, onValueChange = { cellarName.value=it }, label = {
                    Text(
                        text = "Cellar name"
                    )
                })
                Row(
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(imageVector = Icons.Outlined.Clear, contentDescription = "")
                    }
                    Spacer(modifier = Modifier.size(32.dp))
                    IconButton(onClick = { onValidate(cellarName.value) }) {
                        Icon(imageVector = Icons.Outlined.Check, contentDescription = "")
                    }
                }
            }
        }

    }
}