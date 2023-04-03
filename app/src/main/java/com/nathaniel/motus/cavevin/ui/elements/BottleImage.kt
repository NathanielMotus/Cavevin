package com.nathaniel.motus.cavevin.ui.elements

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberAsyncImagePainter

@Composable
fun BottleImage(
    bottleImageBitmap: Bitmap?,
    bottleImageUri: Uri?,
    modifier: Modifier=Modifier,
    imageSize: Int = 200,
) {
    val context = LocalContext.current
    Image(
        painter = rememberAsyncImagePainter(
            model = bottleImageBitmap
        ),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .padding(4.dp)
            .size(width = imageSize.dp, height = (imageSize*1.5).dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(
                    bottleImageUri, "image/*"
                )
                startActivity(context, intent, null)
            }
    )
}