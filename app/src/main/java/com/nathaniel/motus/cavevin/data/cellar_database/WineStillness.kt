package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["id","language"],tableName = "wine_stillness")
data class WineStillness(
    val id:String,
    @ColumnInfo(name = LANGUAGE)
    val language:String,
    val translation:String
){
    companion object{
        const val ID="id"
        const val LANGUAGE="language"
        const val TRANSLATION="translation"
        const val STILL="still"
        const val SPARKLING="sparkling"
    }
}
