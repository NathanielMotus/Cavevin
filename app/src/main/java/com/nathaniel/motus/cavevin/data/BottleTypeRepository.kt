package com.nathaniel.motus.cavevin.data

import com.nathaniel.motus.cavevin.data.cellar_database.BottleType
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.utils.findBaseLanguage

class BottleTypeRepository(val database: CellarDatabase) {

    suspend fun updateBottleType(bottleType: BottleType) =
        database.bottleTypeDao().update(bottleType)

    suspend fun insertBottleType(bottleType: BottleType) =
        database.bottleTypeDao().insert(bottleType)

    suspend fun deleteBottleType(bottleType: BottleType) =
        database.bottleTypeDao().delete(bottleType)

    fun getBottleTypes() = database.bottleTypeDao().getBottleTypes()

    suspend fun getBottleTypeIds() = database.bottleTypeDao().getBottleTypeIds()

    suspend fun findBottleTypeByIdAndLanguage(id: Int, language: String): BottleType =
        //or create it if not yet translated
        database.bottleTypeDao().findBottleTypeByIdAndLanguage(id, language)?: with(findBottleTypeByIdAndLanguage(id,
            findBaseLanguage(language,database.bottleTypeDao().getBottleTypeLanguagesForId(id)))){
            database.bottleTypeDao().insert(BottleType(id,this.standard,language,this.name,this.capacity))
            return findBottleTypeByIdAndLanguage(id,language)
        }
}