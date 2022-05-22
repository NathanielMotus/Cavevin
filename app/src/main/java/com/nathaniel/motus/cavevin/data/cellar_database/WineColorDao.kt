package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WineColorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wineColor: WineColor)

    @Update
    suspend fun update(wineColor: WineColor)

    @Delete
    suspend fun delete(wineColor: WineColor)

    @Query("SELECT * FROM wine_color")
    fun getWineColors():LiveData<List<WineColor>>

    @Query("SELECT DISTINCT language FROM wine_color WHERE id=:id")
    //return languages available for a wine color
    suspend fun getWineColorLanguages(id:String):List<String>

    @Query("SELECT translation FROM wine_color WHERE language=:language and id=:id")
    //return translation for wine color in language
    suspend fun findWineColorTranslation(id:String,language:String):String?
}