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
    val quantity: Int,
)
//todo : cellarEntry should be (cellarId,BottleId,Quantity)
//todo : cellarItem should be (appellation,domain,cuvee,bottleName,capacity,vintage,wineColor,wineStillness,quantity,comment,rating,picture) translated from cellarEntry
