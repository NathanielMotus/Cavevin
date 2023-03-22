package com.nathaniel.motus.cavevin.ui.elements

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberAsyncImagePainter

@Composable
fun BottleImage(
    bottleImageBitmap: Bitmap?,
    bottleImageUri: Uri?,
    imageSize: Int = 200,
    imagePadding: Int = 8,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column() {
        Image(
            painter = rememberAsyncImagePainter(
                model = bottleImageBitmap
            ),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .size(imageSize.dp)
                .padding(imagePadding.dp)
                .clickable {
                   val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(
                        bottleImageUri, "image/*"
                    )
                    startActivity(context,intent,null)
                }
        )
    }
}