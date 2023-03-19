package com.nathaniel.motus.cavevin.data

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils

class BottleImageRepository(val context: Context) {
    private val pathFile = context.filesDir

    suspend fun getBottleImage(imageName: String?): Bitmap? {
        return CellarStorageUtils.getBitmapFromInternalStorage(
            pathFile, context.resources.getString(
                R.string.photo_folder_name
            ), imageName
        )
    }

    suspend fun getBottleImageUri(imageName: String?): Uri? {

        return null
    }

    suspend fun getBottleImageThumbnail(imageName: String?): Bitmap? {

        return null
    }
}