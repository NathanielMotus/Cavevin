package com.nathaniel.motus.cavevin.data

import androidx.room.ColumnInfo

data class CellarEntry(
    @ColumnInfo(name = "cellar_id")
    val cellarId: Int,
    @ColumnInfo(name = "bottle_id")
    val bottleId: Int,
    val quantity: Int
)
