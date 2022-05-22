package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BottleTypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bottleType: BottleType)

    @Update
    suspend fun update(bottleType: BottleType)

    @Delete
    suspend fun delete(bottleType: BottleType)

    @Query("SELECT * FROM bottle_type")
    fun getBottleTypes(): LiveData<List<BottleType>>

    @Query("SELECT id FROM bottle_type WHERE capacity=:capacity LIMIT 1")
    suspend fun findBottleTypeIdByCapacity(capacity: Double):Int

    @Query("SELECT DISTINCT language FROM bottle_type ")
    suspend fun getBottleTypeLanguages():List<String>

    @Query("SELECT DISTINCT id FROM bottle_type")
    suspend fun getBottleTypeIds():List<Int>

    @Query("SELECT * FROM bottle_type WHERE id=:id AND language=:language")
    suspend fun findBottleTypeByIdAndLanguage(id:Int,language:String):BottleType?

    @Query("SELECT language FROM bottle_type WHERE id=:id")
    suspend fun getBottleTypeLanguagesForId(id:Int):List<String>

}