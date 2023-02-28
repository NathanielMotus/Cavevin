package com.nathaniel.motus.cavevin.ui.bottle_edit

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberAsyncImagePainter
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.ui.theme.*
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleEditContent(
    viewModel: BottleDetailViewModel,
    modifier: Modifier = Modifier
) {
    WineCellarMainTheme() {
        Column() {
            val inputImageName by viewModel.imageName.observeAsState("")
            val inputWineColor by viewModel.wineColor.observeAsState("")
            val inputAppellation by viewModel.appellation.observeAsState("")

            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
            {
                BottleImage(
                    imageName = inputImageName,
                    //imageName = null,
                    wineColor = inputWineColor,
                    appellation = inputAppellation,
                    modifier = modifier.fillMaxWidth()
                )
            }

            ImagePicker(thereIsAnImage = inputImageName != "", modifier = modifier.fillMaxWidth())

            OutlinedTextField(
                value = inputAppellation,
                onValueChange = { viewModel.onAppellationChange(it) },
                label = { Text(text = "Appellation") },
                modifier = modifier.fillMaxWidth()
            )

            val inputDomain by viewModel.domain.observeAsState("")
            OutlinedTextField(
                value = inputDomain,
                onValueChange = { viewModel.onDomainChange(it) },
                label = { Text(text = "Domain") },
                modifier = modifier.fillMaxWidth()
            )

            val inputCuvee by viewModel.cuvee.observeAsState("")
            OutlinedTextField(
                value = inputCuvee,
                onValueChange = { viewModel.onCuveeChange(it) },
                label = { Text(text = "Cuvee") },
                modifier = modifier.fillMaxWidth()
            )

        }
    }
}

@Composable
fun BottleImage(
    imageName: String?,
    wineColor: String,
    appellation: String?,
    imageSize: Int = 200,
    imagePadding: Int = 8,
    modifier: Modifier=Modifier
) {
    val context = LocalContext.current
    Column() {
        if (imageName != null)
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
        else {
            val placeHolderColor = when (wineColor) {
                WineColor.RED -> redWineColor
                WineColor.WHITE -> whiteWhineColor
                else -> pinkWineColor
            }
            val placeHolderName: String =
                if (appellation != "") appellation?.first().toString() else ""
            Box(
                modifier = modifier.requiredSize(imageSize.dp),
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
    }
}

@Composable
fun ImagePicker(
    thereIsAnImage: Boolean,
    modifier: Modifier=Modifier,
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
fun ImagePickerButton(
    onClick: () -> Unit = {},
    resourceID: Int,
    contentDescription: String = "",
    modifier: Modifier=Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = resourceID),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        )
    }
}