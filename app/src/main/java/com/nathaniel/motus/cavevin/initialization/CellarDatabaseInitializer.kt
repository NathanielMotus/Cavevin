package com.nathaniel.motus.cavevin.initialization

import android.content.Context
import android.util.Log
import com.nathaniel.motus.cavevin.data.cellar_database.BottleType
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.data.cellar_database.WineStillness
import com.nathaniel.motus.cavevin.helper.CsvTableReader
import com.nathaniel.motus.cavevin.utils.BOTTLE_TYPE_INIT_FILE
import com.nathaniel.motus.cavevin.utils.WINE_COLOR_INIT_FILE
import com.nathaniel.motus.cavevin.utils.WINE_STILLNESS_INIT_FILE

class CellarDatabaseInitializer(
    private val context: Context
) {

    suspend fun populateWineColorTable() {
        val table = CsvTableReader(context.assets.open(WINE_COLOR_INIT_FILE)).getTable()
        for (i in 1 until table.size) CellarDatabase.getDatabase(context).wineColorDao().insert(
            WineColor(
                table[i][getIndex(WineColor.ID, table[0])],
                table[i][getIndex(WineColor.LANGUAGE, table[0])],
                table[i][getIndex(WineColor.TRANSLATION, table[0])]
            )
        )
    }

    suspend fun populateWineStillnessTable() {
        val table = CsvTableReader(context.assets.open(WINE_STILLNESS_INIT_FILE)).getTable()
        for (i in 1 until table.size)
            CellarDatabase.getDatabase(context).wineStillnessDao().insert(
                WineStillness(
                    table[i][getIndex(WineStillness.ID, table[0])],
                    table[i][getIndex(WineStillness.LANGUAGE, table[0])],
                    table[i][getIndex(WineStillness.TRANSLATION, table[0])]
                )
            )
    }

    suspend fun populateBottleTypeTable() {
        val table = CsvTableReader(context.assets.open(BOTTLE_TYPE_INIT_FILE)).getTable()
        for (i in 1 until table.size) {
            CellarDatabase.getDatabase(context).bottleTypeDao().insert(
                BottleType(
                    table[i][getIndex(BottleType.ID, table[0])].toInt(),
                    table[i][getIndex(BottleType.STANDARD,table[0])].toBoolean(),
                    table[i][getIndex(BottleType.LANGUAGE, table[0])],
                    table[i][getIndex(BottleType.NAME, table[0])],
                    table[i][getIndex(BottleType.CAPACITY, table[0])].toDouble()
                )
            )
        }
    }

    private fun getIndex(header: String, headers: List<String>): Int = headers.indexOf(header)
    //get index of string (here column name) in list of strings (here table header)
}