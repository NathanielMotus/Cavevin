package com.nathaniel.motus.cavevin.data.cellar_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StockDao {

    @Insert
    suspend fun insert(stock: Stock)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(stock: Stock)

    @Delete
    suspend fun delete(stock: Stock)

    @Query("SELECT * FROM stock")
    fun getStocks(): LiveData<List<Stock>>

    @Query("SELECT quantity FROM stock where cellar_id=:cellarId AND bottle_id=:bottleId")
    suspend fun getStockForBottleInCellar(bottleId:Int,cellarId:Int):Int

    @Query("DELETE FROM stock where cellar_id=:cellarId")
    suspend fun deleteCellarStocks(cellarId: Int)
}