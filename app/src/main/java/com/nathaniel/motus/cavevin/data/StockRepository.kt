package com.nathaniel.motus.cavevin.data

import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.data.cellar_database.Stock

class StockRepository(val database: CellarDatabase) {
    suspend fun updateStock(stock: Stock)=database.stockDao().update(stock)
    suspend fun insertStock(stock: Stock)=database.stockDao().insert(stock)
    suspend fun deleteStock(stock: Stock)=database.stockDao().delete(stock)
    fun getStocks()=database.stockDao().getStocks()
}