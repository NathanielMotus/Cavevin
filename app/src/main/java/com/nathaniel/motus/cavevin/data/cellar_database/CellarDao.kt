package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.room.*
import com.nathaniel.motus.cavevin.data.CellarEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface CellarDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cellar: Cellar)

    @Update
    suspend fun update(cellar: Cellar)

    @Delete
    suspend fun delete(cellar: Cellar)

    @Query("SELECT * FROM cellar")
    fun getCellars(): Flow<List<Cellar>>

    @Query("SELECT id FROM cellar ORDER BY id DESC LIMIT 1")
    suspend fun getLastCellarId(): Int?

    @Query("SELECT * FROM cellar WHERE id=:id LIMIT 1")
    fun getCellar(id: Int): Flow<Cellar>

    @Query(
        "SELECT cellar_id," +
                "bottle_id," +
                "quantity " +
                "FROM cellar JOIN stock ON cellar.id=stock.cellar_id " +
                "JOIN bottle ON bottle.id=stock.bottle_id "
    )
    fun getCellarEntries(): Flow<List<CellarEntry>>
}
