package com.nathaniel.motus.cavevin.data

import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.data.cellar_database.WineStillness
import com.nathaniel.motus.cavevin.utils.findBaseLanguage
import org.intellij.lang.annotations.Language

class WineStillnessRepository(val database: CellarDatabase) {

    suspend fun insertWineStillness(wineStillness: WineStillness) =
        database.wineStillnessDao().insert(wineStillness)

    suspend fun updateWineStillness(wineStillness: WineStillness) =
        database.wineStillnessDao().update(wineStillness)

    suspend fun deleteWineStillness(wineStillness: WineStillness) =
        database.wineStillnessDao().delete(wineStillness)

    fun getWineStillnesses() = database.wineStillnessDao().getWineStillnesses()

    suspend fun getWineStillnessLanguages(id: String?) =
        database.wineStillnessDao().getWineStillnessLanguages(id)

    suspend fun findWineStillnessTranslation(id: String?, language: String): String? {
        //or create it if not yet translated
        if (id != null) {
            database.wineStillnessDao().findWineStillnessTranslation(id, language) ?: with(
                findWineStillnessTranslation(
                    id,
                    findBaseLanguage(language, getWineStillnessLanguages(id)!!)
                )
            ) {
                database.wineStillnessDao().insert(WineStillness(id, language, this!!))
                return findWineStillnessTranslation(id, language)
            }
        }
        return null
    }

}