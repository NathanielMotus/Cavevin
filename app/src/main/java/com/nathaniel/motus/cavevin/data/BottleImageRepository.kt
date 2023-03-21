package com.nathaniel.motus.cavevin.data

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils

class BottleImageRepository(private val application: Application) {

    suspend fun getBottleImageBitmap(imageName: String?): Bitmap? {
        return CellarStorageUtils.getBitmapFromInternalStorage(
            application.applicationContext.filesDir,
            application.applicationContext.resources.getString(
                R.string.photo_folder_name
            ),
            imageName
        )
    }

    suspend fun getBottleImageUri(imageName: String?): Uri? {
        return if (imageName == null) null
        else
            Uri.parse(
                "content://" + application.applicationContext.packageName + "/"
                        + application.applicationContext.resources.getString(
                    R.string.photo_folder_name
                ) + "/" + imageName
            )
    }

    suspend fun getBottleBitmapThumbnail(imageName: String?): Bitmap? {
        return getBottleImageBitmap(imageName + application.applicationContext.resources.getString(R.string.photo_thumbnail_suffix))
    }
}