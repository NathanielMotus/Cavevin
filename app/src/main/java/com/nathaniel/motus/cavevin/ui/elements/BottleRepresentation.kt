package com.nathaniel.motus.cavevin.ui.elements

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.*

@Composable
fun BottleRepresentation(
    bottleImageBitmap: Bitmap?,
    bottleImageUri: Uri?,
    wineColor: String,
    appellation:String?,
    imageSize:Int=200
){
    Box(modifier = Modifier, contentAlignment = Alignment.TopCenter)
    {
        if (bottleImageBitmap != null)
            BottleImage(
                bottleImageBitmap = bottleImageBitmap,
                bottleImageUri = bottleImageUri,
                imageSize=imageSize
            )
        else
            BottleImagePlaceHolder(
                wineColor = wineColor,
                appellation = appellation,
                imageSize=imageSize
            )
    }
}