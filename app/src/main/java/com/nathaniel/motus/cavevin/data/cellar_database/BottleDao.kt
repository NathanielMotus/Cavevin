package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BottleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bottle: Bottle)

    @Update
    suspend fun update(bottle: Bottle)

    @Delete
    suspend fun delete(bottle: Bottle)

    @Query("SELECT id FROM bottle ORDER BY ID DESC LIMIT 1")
    suspend fun getLastBottleId():Int?

    @Query("SELECT * FROM bottle WHERE id=:id")
    suspend fun findBottleById(id:Int):Bottle

    @Query("SELECT DISTINCT appellation FROM bottle")
    suspend fun getAppellations():List<String>

    @Query("SELECT DISTINCT domain FROM bottle")
    suspend fun getDomains():List<String>

    @Query("SELECT DISTINCT cuvee FROM bottle")
    suspend fun getCuvees():List<String>
}