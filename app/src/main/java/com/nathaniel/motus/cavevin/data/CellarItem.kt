package com.nathaniel.motus.cavevin.data

import android.graphics.Bitmap
import android.net.Uri

data class CellarItem(
    val cellarId:Int,
    val bottleId: Int,
    val appellation: String?,
    val domain: String?,
    val cuvee: String?,
    val vintage: Int?,
    val wineColor: String,
    val wineStillness: String,
    val capacity:Double,
    val bottleTypeAndCapacity: Pair<Int, String>,
    val stock: Int,
    val price: Double?,
    val agingCapacity: Int?,
    val comment: String?,
    val rating: Int,
    val bottleImageBitmap: Bitmap?,
    val bottleImageUri: Uri?
)
