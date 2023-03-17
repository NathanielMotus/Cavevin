package com.nathaniel.motus.cavevin.ui.elements

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils

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
                    ContextCompat.startActivity(context, intent, null)
                }
        )
    }
}