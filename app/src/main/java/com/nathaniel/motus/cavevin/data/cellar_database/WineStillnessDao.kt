package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WineStillnessDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wineStillness: WineStillness)

    @Update
    suspend fun update(wineStillness: WineStillness)

    @Delete
    suspend fun delete(wineStillness: WineStillness)

    @Query("SELECT * FROM wine_stillness")
    fun getWineStillnesses():LiveData<List<WineStillness>>

    @Query("SELECT language FROM wine_stillness WHERE id=:id")
    //return languages available for a stillness
    suspend fun getWineStillnessLanguages(id:String?):List<String>?

    @Query("SELECT translation FROM wine_stillness WHERE language=:language AND id=:id")
    //return translation of a stillness for a language
    suspend fun findWineStillnessTranslation(id:String?,language:String?):String?
}