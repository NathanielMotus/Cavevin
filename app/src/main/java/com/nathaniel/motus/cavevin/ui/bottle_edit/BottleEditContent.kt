package com.nathaniel.motus.cavevin.ui.bottle_edit

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberAsyncImagePainter
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.ui.theme.WineCellarMainTheme
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleEditContent(
    viewModel: BottleDetailViewModel,
    modifier: Modifier = Modifier
) {
    WineCellarMainTheme {
        Column {
            val inputImageName by viewModel.imageName.observeAsState("")
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                BottleImage(imageName = inputImageName, modifier = modifier)
            }

            val inputAppellation by viewModel.appellation.observeAsState("")
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
    imageSize: Int = 200,
    imagePadding: Int = 8,
    modifier: Modifier
) {
    val context = LocalContext.current
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
    else
        Image(
            //todo add image placeholder
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_48), contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .size(imageSize.dp)
                .padding(imagePadding.dp)
        )
}