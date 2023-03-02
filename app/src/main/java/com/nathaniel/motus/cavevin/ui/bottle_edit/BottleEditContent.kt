package com.nathaniel.motus.cavevin.ui.bottle_edit

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleEditContent(
    viewModel: BottleDetailViewModel,
    modifier: Modifier = Modifier
) {
    WineCellarMainTheme() {
        Surface() {
            Column {
                //val inputImageName by viewModel.imageName.observeAsState("")
                val inputImageName = ""
                val inputAppellation by viewModel.appellation.observeAsState("")
                val inputRating by viewModel.rating.observeAsState(0)
                val inputWineColor by viewModel.wineColor.observeAsState(initial = "")
                val redWineTranslation by viewModel.redWineTranslation.observeAsState("")
                val whiteWineTranslation by viewModel.whiteWineTranslation.observeAsState("")
                val pinkWineTranslation by viewModel.pinkWineTranslation.observeAsState(initial = "")
                val inputWineStillness by viewModel.wineStillness.observeAsState(initial = "")
                val stillWineTranslation by viewModel.stillWineTranslation.observeAsState(initial = "")
                val sparklingWineTranslation by viewModel.sparklingWineTranslation.observeAsState(
                    initial = ""
                )


                Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
                {
                    if (inputImageName != "" && inputImageName != null)
                        BottleImage(
                            imageName = inputImageName,
                            modifier = modifier.fillMaxWidth()
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

                OutlinedTextField(
                    value = inputAppellation,
                    onValueChange = { viewModel.onAppellationChange(it) },
                    label = { Text(text = stringResource(id = R.string.appellation)) },
                    modifier = modifier.fillMaxWidth()
                )

                val inputDomain by viewModel.domain.observeAsState("")
                OutlinedTextField(
                    value = inputDomain,
                    onValueChange = { viewModel.onDomainChange(it) },
                    label = { Text(text = stringResource(id = R.string.domain)) },
                    modifier = modifier.fillMaxWidth()
                )

                val inputCuvee by viewModel.cuvee.observeAsState("")
                OutlinedTextField(
                    value = inputCuvee,
                    onValueChange = { viewModel.onCuveeChange(it) },
                    label = { Text(text = stringResource(id = R.string.cuvee)) },
                    modifier = modifier.fillMaxWidth()
                )

                WineColorRadioGroup(
                    selectedWineColor = inputWineColor,
                    onWineColorChange = { wineColor: String -> viewModel.onWineColorChange(wineColor) },
                    redWineTranslation = redWineTranslation,
                    whiteWineTranslation = whiteWineTranslation,
                    pinkWineTranslation = pinkWineTranslation
                )

                WineStillnessRadioGroup(
                    selectedWineStillness = inputWineStillness,
                    onWineStillnessChange = {wineStillness:String -> viewModel.onWineStillnessChange(wineStillness)},
                    stillWineTranslation =stillWineTranslation ,
                    sparklingWineTranslation = sparklingWineTranslation
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