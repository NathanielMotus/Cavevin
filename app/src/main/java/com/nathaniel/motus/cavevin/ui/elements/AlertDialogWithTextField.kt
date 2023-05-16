package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogWithTextField(
    title: String?,
    label: String?,
    input: String? = "",
    onDismissRequest: () -> Unit,
    onValidate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(modifier = modifier.padding(16.dp)) {
                var textValue = remember { mutableStateOf(input ?: "") }
                var validValue = remember { mutableStateOf(false) }
                if (title != null) {
                    Text(text = title, style = MaterialTheme.typography.displaySmall)
                    Spacer(modifier = Modifier.size(16.dp))
                }
                OutlinedTextField(
                    value = textValue.value,
                    onValueChange = {
                        textValue.value = it
                        validValue.value = textValue.value != ""
                    },
                    label = { Text(text = label ?: "") }
                )
                Row(
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.size(32.dp))
                    IconButton(
                        onClick = { onValidate(textValue.value) },
                        enabled = validValue.value
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "",
                            tint = if (validValue.value) MaterialTheme.colorScheme.primary else Color.LightGray
                        )
                    }
                }
            }
        }
    }
}