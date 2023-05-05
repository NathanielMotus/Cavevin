package com.nathaniel.motus.cavevin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nathaniel.motus.cavevin.data.cellar_database.Cellar
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase

class CellarRepository(val database: CellarDatabase) {

    suspend fun updateCellar(cellar: Cellar) = database.cellarDao().update(cellar)
    suspend fun insertCellar(cellar: Cellar) = database.cellarDao().insert(cellar)
    suspend fun deleteCellar(cellar: Cellar) = database.cellarDao().delete(cellar)
    suspend fun getCellars() = database.cellarDao().getCellars()
    suspend fun getLastCellarId() = database.cellarDao().getLastCellarId()

    fun getCellarEntries()=database.cellarDao().getCellarEntries()

    suspend fun getCellar(id:Int)=database.cellarDao().getCellar(id)

}