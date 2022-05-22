package com.nathaniel.motus.cavevin.data

import com.nathaniel.motus.cavevin.data.cellar_database.Bottle
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase

class BottleRepository(val database: CellarDatabase) {

    suspend fun updateBottle(bottle: Bottle)=database.bottleDao().update(bottle)

    suspend fun insertBottle(bottle: Bottle)=database.bottleDao().insert(bottle)

    suspend fun deleteBottle(bottle: Bottle)=database.bottleDao().delete(bottle)

    suspend fun findBottleById(id:Int)=database.bottleDao().findBottleById(id)
}