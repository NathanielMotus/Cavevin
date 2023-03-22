package com.nathaniel.motus.cavevin.data

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils

class BottleImageRepository(private val application: Application) {

    suspend fun getBottleImageBitmap(imageName: String?): Bitmap? {
        return if (imageName != null)
            CellarStorageUtils.getBitmapFromInternalStorage(
                application.applicationContext.filesDir,
                application.applicationContext.resources.getString(
                    R.string.photo_folder_name
                ),
                imageName
            )
        else null
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

    suspend fun replaceBottleImage(formerImageName: String?, newBottleBitmap: Bitmap?): String? {
        if (formerImageName != null)
            CellarStorageUtils.deleteFileFromInternalStorage(
                application.filesDir,
                application.resources.getString(R.string.photo_folder_name),
                formerImageName
            )

        return if (newBottleBitmap != null)
            CellarStorageUtils.saveBottleImageToInternalStorage(
                application.filesDir,
                application.resources.getString(R.string.photo_folder_name),
                newBottleBitmap
            )
        else null
    }
}