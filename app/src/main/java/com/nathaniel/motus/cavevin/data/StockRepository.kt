package com.nathaniel.motus.cavevin.data

import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.data.cellar_database.Stock

class StockRepository(val database: CellarDatabase) {
    suspend fun updateStock(stock: Stock) = database.stockDao().update(stock)
    suspend fun insertStock(stock: Stock) = database.stockDao().insert(stock)
    suspend fun deleteStock(stock: Stock) = database.stockDao().delete(stock)
    suspend fun deleteCellarStocks(cellarId: Int)=database.stockDao().deleteCellarStocks(cellarId = cellarId)
    fun getStocks() = database.stockDao().getStocks()
    suspend fun getStockForBottleInCellar(bottleId: Int, cellarId: Int) =
        database.stockDao().getStockForBottleInCellar(bottleId, cellarId)
}