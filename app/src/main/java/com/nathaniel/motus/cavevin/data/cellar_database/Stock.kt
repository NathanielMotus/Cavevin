package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock", primaryKeys = ["cellar_id","bottle_id"])
data class Stock(
    @ColumnInfo(name ="cellar_id")
    val cellarId:Int,
    @ColumnInfo(name = "bottle_id")
    val bottleId:Int,
    val quantity:Int
)
