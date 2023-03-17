package com.nathaniel.motus.cavevin.ui.elements

import android.icu.util.Currency
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.nathaniel.motus.cavevin.utils.availableCurrencies
import com.nathaniel.motus.cavevin.utils.defaultCurrencyCode

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedPriceEditor(
    price: Double?,
    currency: String?,
    onPriceChange: (price: Double?) -> Unit,
    onCurrencyChange: (currency: String) -> Unit,
    priceLabelText: String = "",
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.Bottom) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = price?.toString() ?: "",
            onValueChange = { onPriceChange(it.toDoubleOrNull()) },
            label = { Text(text = priceLabelText) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = if (currency != null && currency != "") Currency.getInstance(currency).symbol else Currency.getInstance(
                    defaultCurrencyCode()
                ).symbol,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = modifier
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = modifier
                    .exposedDropdownSize(matchTextFieldWidth = true)
            ) {
                availableCurrencies().forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(text = currency.symbol) },
                        onClick = {
                            onCurrencyChange(currency.currencyCode)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}