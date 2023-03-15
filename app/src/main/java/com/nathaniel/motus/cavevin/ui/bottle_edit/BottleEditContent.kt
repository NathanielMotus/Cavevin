package com.nathaniel.motus.cavevin.ui.bottle_edit

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Currency
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberAsyncImagePainter
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.data.cellar_database.WineStillness
import com.nathaniel.motus.cavevin.ui.theme.*
import com.nathaniel.motus.cavevin.utils.availableCurrencies
import com.nathaniel.motus.cavevin.utils.defaultCurrencyCode
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BottleEditContent(
    viewModel: BottleDetailViewModel,
    modifier: Modifier = Modifier
) {
    WineCellarMainTheme {

        val focusManager = LocalFocusManager.current
        Surface(modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { focusManager.clearFocus() }
        ) {

            Column(
                modifier = modifier.verticalScroll(rememberScrollState())
            ) {
                val inputImageName by viewModel.imageName.observeAsState("")
                //val inputImageName = ""
                val inputAppellation by viewModel.appellation.observeAsState("")
                val inputDomain by viewModel.domain.observeAsState("")
                val inputCuvee by viewModel.cuvee.observeAsState("")
                val inputRating by viewModel.rating.observeAsState(0)
                val inputWineColor by viewModel.wineColor.observeAsState(initial = "")
                val redWineTranslation by viewModel.redWineTranslation.observeAsState("")
                val whiteWineTranslation by viewModel.whiteWineTranslation.observeAsState("")
                val pinkWineTranslation by viewModel.pinkWineTranslation.observeAsState(initial = "")
                val inputWineStillness by viewModel.wineStillness.observeAsState(initial = "")
                val stillWineTranslation by viewModel.stillWineTranslation.observeAsState(
                    initial = ""
                )
                val sparklingWineTranslation by viewModel.sparklingWineTranslation.observeAsState(
                    initial = ""
                )
                val bottleTypesAndCapacities by viewModel.bottleTypesAndCapacities.observeAsState(
                    initial = listOf(Pair(0, ""))
                )
                val inputSelectedBottleTypeItem by viewModel.selectedBottleTypeItem.observeAsState(
                    initial = Pair(0, "")
                )
                val appellations by viewModel.appellations.observeAsState(listOf())
                val domains by viewModel.domains.observeAsState(initial = listOf())
                val cuvees by viewModel.cuvees.observeAsState(initial = listOf())
                val inputVintage by viewModel.vintage.observeAsState(initial = null)
                val inputOrigin by viewModel.origin.observeAsState(initial = "")
                val inputComment by viewModel.comment.observeAsState(initial = "")
                val inputPrice by viewModel.price.observeAsState(initial = null)
                val inputCurrency by viewModel.currency.observeAsState(initial = null)
                val inputAgingCapacity by viewModel.agingCapacity.observeAsState(initial = null)

                Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
                {
                    if (inputImageName != "" && inputImageName != null)
                        BottleImage(
                            imageName = inputImageName
                        )
                    else
                        BottleImagePlaceHolder(
                            wineColor = inputWineColor,
                            appellation = inputAppellation
                        )
                }

                ImagePicker(
                    thereIsAnImage = inputImageName != "",
                    modifier = modifier.fillMaxWidth()
                )

                Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    RatingBar(
                        rating = inputRating,
                        onRatingChange = { rating: Int -> viewModel.onRatingChange(rating) },
                        isEditable = true
                    )
                }

                AutocompleteTextInput(
                    value = inputAppellation,
                    onValueChange = { viewModel.onAppellationChange(it) },
                    labelText = stringResource(id = R.string.appellation),
                    suggestions = appellations
                )

                AutocompleteTextInput(
                    value = inputDomain, onValueChange = { viewModel.onDomainChange(it) },
                    labelText = stringResource(id = R.string.domain), suggestions = domains
                )

                AutocompleteTextInput(
                    value = inputCuvee,
                    onValueChange = { viewModel.onCuveeChange(it) },
                    labelText = stringResource(
                        id = R.string.cuvee
                    ),
                    suggestions = cuvees
                )

                WineColorRadioGroup(
                    selectedWineColor = inputWineColor,
                    onWineColorChange = { wineColor: String ->
                        viewModel.onWineColorChange(
                            wineColor
                        )
                    },
                    redWineTranslation = redWineTranslation,
                    whiteWineTranslation = whiteWineTranslation,
                    pinkWineTranslation = pinkWineTranslation
                )

                WineStillnessRadioGroup(
                    selectedWineStillness = inputWineStillness,
                    onWineStillnessChange = { wineStillness: String ->
                        viewModel.onWineStillnessChange(
                            wineStillness
                        )
                    },
                    stillWineTranslation = stillWineTranslation,
                    sparklingWineTranslation = sparklingWineTranslation
                )

                PairSpinner(
                    itemList = bottleTypesAndCapacities,
                    selectedItem = inputSelectedBottleTypeItem,
                    onSelectionChanged = { item: Pair<Int, String> ->
                        viewModel.onBottleTypeIdChange(
                            item.first
                        )
                    },
                    labelText = stringResource(id = R.string.bottle_type)
                )

                GenericOutlinedIntegerField(
                    value = inputVintage,
                    onValueChange = { viewModel.onVintageChange(it) },
                    labelText = stringResource(
                        id = R.string.vintage
                    )
                )

                GenericOutlineTextField(
                    value = inputOrigin,
                    onValueChange = { viewModel.onOriginChange(it) },
                    labelText = stringResource(
                        id = R.string.origin
                    )
                )

                GenericOutlineTextField(
                    value = inputComment,
                    onValueChange = { viewModel.onCommentChange(it) },
                    labelText = stringResource(R.string.comments)
                )

                OutlinedPriceEditor(
                    price = inputPrice,
                    currency = inputCurrency,
                    onPriceChange = { price: Double? -> viewModel.onPriceChange(price) },
                    onCurrencyChange = { currency: String? ->
                        viewModel.onCurrencyChange(
                            currency
                        )
                    },
                    priceLabelText = stringResource(id = R.string.price),
                )

                GenericOutlinedIntegerField(
                    value = inputAgingCapacity,
                    onValueChange = { agingCapacity: Int? ->
                        viewModel.onAgingCapacityChange(agingCapacity)
                    },
                    labelText = stringResource(id = R.string.aging_capacity)
                )
            }
        }
    }
}

@Composable
fun BottleImage(
    imageName: String?,
    imageSize: Int = 200,
    imagePadding: Int = 8,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column() {
        Image(
            painter = rememberAsyncImagePainter(
                model = CellarStorageUtils.getBitmapFromInternalStorage(
                    LocalContext.current.filesDir,
                    LocalContext.current.resources.getString(R.string.photo_folder_name),
                    imageName
                )
            ), contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .size(imageSize.dp)
                .padding(imagePadding.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(
                        CellarPictureUtils.getUriFromFileProvider(
                            context,
                            imageName!!
                        ), "image/*"
                    )
                    startActivity(context, intent, null)
                }
        )
    }
}

@Composable
fun BottleImagePlaceHolder(
    wineColor: String,
    appellation: String?,
    imageSize: Int = 200,
    imagePadding: Int = 8,
    modifier: Modifier = Modifier
) {
    val placeHolderColor = when (wineColor) {
        WineColor.RED -> redWineColor
        WineColor.WHITE -> whiteWhineColor
        else -> pinkWineColor
    }
    val placeHolderName: String =
        if (appellation != "") appellation?.first().toString() else ""
    Box(
        modifier = modifier
            .requiredSize(imageSize.dp)
            .padding(imagePadding.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = modifier
                .drawBehind {
                    drawCircle(
                        color = placeHolderColor,
                        radius = this.size.maxDimension
                    )
                },
            color = MaterialTheme.colorScheme.onPrimary,
            text = placeHolderName,
            fontSize = (imageSize / 5).sp
        )
    }
}

@Composable
fun ImagePicker(
    thereIsAnImage: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (thereIsAnImage)
            ImagePickerButton(resourceID = R.drawable.outline_delete_forever_24)

        ImagePickerButton(resourceID = R.drawable.outline_folder_24)

        ImagePickerButton(resourceID = R.drawable.outline_camera_alt_24)
    }
}

@Composable
private fun ImagePickerButton(
    onClick: () -> Unit = {},
    resourceID: Int,
    contentDescription: String = "",
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = resourceID),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        )
    }
}

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

@Composable
fun WineColorRadioGroup(
    selectedWineColor: String,
    onWineColorChange: (wineColor: String) -> Unit,
    redWineTranslation: String = "RED",
    whiteWineTranslation: String = "WHITE",
    pinkWineTranslation: String = "PINK",
    modifier: Modifier = Modifier
) {
    Row() {
        WineColorRadioButton(
            wineColor = WineColor.RED,
            wineColorTranslation = redWineTranslation,
            wineColorTint = redWineColor,
            selectedWineColor = selectedWineColor,
            onWineColorChange = { onWineColorChange(WineColor.RED) }
        )
        WineColorRadioButton(
            wineColor = WineColor.WHITE,
            wineColorTranslation = whiteWineTranslation,
            wineColorTint = whiteWhineColor,
            selectedWineColor = selectedWineColor,
            onWineColorChange = { onWineColorChange(WineColor.WHITE) }
        )
        WineColorRadioButton(
            wineColor = WineColor.PINK,
            wineColorTranslation = pinkWineTranslation,
            wineColorTint = pinkWineColor,
            selectedWineColor = selectedWineColor,
            onWineColorChange = { onWineColorChange(WineColor.PINK) }
        )
    }
}

@Composable
private fun WineColorRadioButton(
    wineColor: String,
    wineColorTranslation: String,
    wineColorTint: Color,
    selectedWineColor: String,
    onWineColorChange: (wineColor: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedWineColor == wineColor,
            onClick = { onWineColorChange(wineColor) })
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_wine_bar_full_48),
            contentDescription = "",
            colorFilter = ColorFilter.tint(wineColorTint),
            modifier = modifier.size(24.dp)
        )
        Text(text = wineColorTranslation)
    }
}

@Composable
private fun WineStillnessRadioButton(
    wineStillness: String,
    wineStillnessTranslation: String,
    selectedWineStillness: String,
    iconId: Int,
    onWineStillnessChange: (wineStillness: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = wineStillness == selectedWineStillness,
            onClick = { onWineStillnessChange(wineStillness) })
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = modifier.size(24.dp)
        )
        Text(text = wineStillnessTranslation)

    }
}

@Composable
fun WineStillnessRadioGroup(
    selectedWineStillness: String,
    onWineStillnessChange: (wineStillness: String) -> Unit,
    stillWineTranslation: String,
    sparklingWineTranslation: String,
    modifier: Modifier = Modifier
) {
    Row() {
        WineStillnessRadioButton(
            wineStillness = WineStillness.STILL,
            wineStillnessTranslation = stillWineTranslation,
            selectedWineStillness = selectedWineStillness,
            iconId = R.drawable.ic_baseline_wine_bar_full_48,
            onWineStillnessChange = { wineStillness: String -> onWineStillnessChange(wineStillness) }
        )
        WineStillnessRadioButton(
            wineStillness = WineStillness.SPARKLING,
            wineStillnessTranslation = sparklingWineTranslation,
            selectedWineStillness = selectedWineStillness,
            iconId = R.drawable.ic_baseline_wine_bar_full_sparkling_48,
            onWineStillnessChange = { wineStillness: String -> onWineStillnessChange(wineStillness) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PairSpinner(
    itemList: List<Pair<Int, String>>,
    selectedItem: Pair<Int, String>,
    onSelectionChanged: (Pair<Int, String>) -> Unit,
    labelText: String = "Label",
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {

        TextField(
            readOnly = true,
            value = selectedItem.second,
            onValueChange = {},
            label = {
                Text(
                    text = labelText
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier
                .exposedDropdownSize(matchTextFieldWidth = true)
                .requiredSize(300.dp)
        ) {
            itemList.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.second) },
                    onClick = {
                        onSelectionChanged(item)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutocompleteTextInput(
    value: String,
    onValueChange: (value: String) -> Unit,
    suggestions: List<String>,
    labelText: String = "",
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = value,
            onValueChange = { value: String -> onValueChange(value) },
            label = { Text(text = labelText) },
            modifier = modifier
                .menuAnchor(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                expanded = false
            })
        )

        val filterSuggestions =
            suggestions.filter { it.contains(value, ignoreCase = true) }.sorted()
        if (filterSuggestions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = modifier.exposedDropdownSize(matchTextFieldWidth = true)
            ) {
                filterSuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        onClick = {
                            onValueChange(suggestion)
                            expanded = false
                            focusManager.clearFocus()
                        },
                        text = { Text(text = suggestion) })
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericOutlineTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    labelText: String = "",
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = { it -> onValueChange(it) },
        label = { Text(text = labelText) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericOutlinedIntegerField(
    value: Int?,
    onValueChange: (value: Int?) -> Unit,
    labelText: String = "",
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { onValueChange(it.toIntOrNull()) },
        label = {
            Text(
                text = labelText
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

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
