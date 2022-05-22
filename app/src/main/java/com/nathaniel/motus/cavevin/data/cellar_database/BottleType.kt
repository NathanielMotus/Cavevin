package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.intellij.lang.annotations.Language

@Entity(primaryKeys = ["id","language"], tableName = "bottle_type")
data class BottleType(
    @ColumnInfo(name= ID)
    val id:Int,
    val standard:Boolean,
    //standard id is 1-1000
    //custom id is >1000
    @ColumnInfo(name= LANGUAGE)
    val language: String,   //format abc_DEF
    @ColumnInfo(name= NAME)
    val name:String,
    @ColumnInfo(name= CAPACITY)
    val capacity:Double
){
    companion object{
        const val ID="id"
        const val STANDARD="standard"
        const val LANGUAGE="language"
        const val NAME="name"
        const val CAPACITY="capacity"
    }
}
