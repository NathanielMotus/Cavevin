package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.navigation.Navigator
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "cellar")
data class Cellar(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name:String,
)
