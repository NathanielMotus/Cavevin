package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["id","language"],tableName = "wine_color")
data class WineColor(
    val id:String,
    val language:String,
    val translation:String
){
    companion object{
        const val ID="id"
        const val LANGUAGE="language"
        const val TRANSLATION="translation"
        const val RED="red"
        const val WHITE="white"
        const val PINK="pink"
    }
}
