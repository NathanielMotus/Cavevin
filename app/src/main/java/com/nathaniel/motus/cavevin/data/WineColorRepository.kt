package com.nathaniel.motus.cavevin.data

import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.utils.findBaseLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WineColorRepository(val database: CellarDatabase) {
    suspend fun insertWineColor(wineColor: WineColor) = database.wineColorDao().insert(wineColor)

    suspend fun updateWineColor(wineColor: WineColor) = database.wineColorDao().update(wineColor)

    suspend fun deleteWineColor(wineColor: WineColor) = database.wineColorDao().delete(wineColor)

    fun getWineColors() = database.wineColorDao().getWineColors()

    private suspend fun getWineColorLanguages(id:String) = database.wineColorDao().getWineColorLanguages(id)

    suspend fun findWineColorTranslation(id: String, language: String): String =
        //or create it if not yet translated
        database.wineColorDao().findWineColorTranslation(id, language) ?: with(
            findWineColorTranslation(
                id,
                findBaseLanguage(language, getWineColorLanguages(id))
            )
        ) {
            database.wineColorDao().insert(WineColor(id,language, this))
            return findWineColorTranslation(id,language)
        }
}