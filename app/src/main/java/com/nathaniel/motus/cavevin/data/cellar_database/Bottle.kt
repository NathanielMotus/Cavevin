package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.intellij.lang.annotations.Language

@Entity(tableName="bottle")
data class Bottle(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int=0,
    val appellation:String?,
    val domain:String?,
    val cuvee:String?,
    val vintage:Int?,
    @ColumnInfo(name = "wine_color")
    val wineColor:String,
    @ColumnInfo(name = "wine_stillness")
    val wineStillness:String,
    val comment:String?,
    @ColumnInfo(name="bottle_type_id")
    val bottleTypeId:Int,
    val price:Double?,
    val currency:String?,
    val agingCapacity:Int?,
    val origin:String?,
    @ColumnInfo(name = "rating")
    val rating:Int=0, //from 1 to 5, 0 if not rated
    val picture:String?  //ref to a picture
)
