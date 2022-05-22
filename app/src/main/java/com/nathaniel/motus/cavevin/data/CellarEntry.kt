package com.nathaniel.motus.cavevin.data

import android.content.Context
import android.media.Rating
import androidx.room.ColumnInfo
import com.nathaniel.motus.cavevin.data.cellar_database.BottleType
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase

data class CellarEntry(
    @ColumnInfo(name = "cellar_id")
    val cellarId: Int,
    @ColumnInfo(name = "bottle_id")
    val bottleId: Int,
    /*
    val appellation: String?,
    val domain: String?,
    val cuvee: String?,
    val vintage: String?,
    @ColumnInfo(name = "wine_color")
    val wineColor: String?,
    @ColumnInfo(name = "wine_stillness")
    val wineStillness: String?,
    @ColumnInfo(name = "bottle_type_id")
    val bottleTypeId: Int,
    @ColumnInfo(name = "bottle_type_name")
    val bottleTypeName: String,
    @ColumnInfo(name = "bottle_type_capacity")
    val bottleTypeCapacity: Double,
    val comment: String?,

     */
    val quantity: Int,
    /*
    val rating: Int,
    val picture: String?

     */
)
//todo : cellarEntry should be (cellarId,BottleId,Quantity)
//todo : cellarItem should be (appellation,domain,cuvee,vintage,wineColor,wineStillness,quantity,comment,rating,picture) translated from cellarEntry
