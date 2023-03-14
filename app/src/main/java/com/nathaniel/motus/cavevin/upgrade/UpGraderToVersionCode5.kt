package com.nathaniel.motus.cavevin.upgrade

import android.content.Context
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.data.cellar_database.Bottle
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.data.cellar_database.Stock
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.model.Cellar
import kotlinx.coroutines.*
import java.lang.Exception

class UpGraderToVersionCode5(
    private val context: Context
) {
    suspend fun execute() {
        loadFormerDatabase()
        convertFormerDatabase()
    }

    private fun loadFormerDatabase() {
        //load all the data
        val saveDir = context.applicationContext.filesDir
        val saveURI = CellarStorageUtils.createOrGetFile(
            saveDir,
            context.applicationContext.resources.getString(R.string.database_folder_name),
            context.applicationContext.resources.getString(R.string.database_file_name)
        )
        CellarStorageUtils.loadDataBase(context.applicationContext, saveURI)
    }

    private suspend fun convertFormerDatabase() {
        println("Convert former database")
        Cellar.cellarPool.forEach { convertCellar(it) }
    }

    private suspend fun convertCellar(formerCellar: Cellar) {
        CellarDatabase.getDatabase(context).cellarDao().insert(
            com.nathaniel.motus.cavevin.data.cellar_database.Cellar(
                0,
                formerCellar.cellarName
            )
        )
        Log.i("TEST", "lastId in convertCellar : ${CellarDatabase.getDatabase(context).cellarDao().getLastCellarId()}")
        CellarDatabase.getDatabase(context).cellarDao().getLastCellarId()?.let {
            convertCells(
                it,
                formerCellar
            )
        }
    }

    private suspend fun convertCells(cellarId: Int, formerCellar: Cellar) {
        formerCellar.cellList.forEach {
            CellarDatabase.getDatabase(context).bottleDao().insert(
                Bottle(
                    0,
                    it.bottle.appellation,
                    it.bottle.domain,
                    it.bottle.cuvee,
                    it.bottle.vintage.toIntOrNull(),
                    when (it.bottle.type) {
                        "1" -> WineColor.WHITE
                        "2" -> WineColor.PINK
                        else -> WineColor.RED
                    },
                    "still",
                    it.bottle.bottleComment + it.cellComment,
                    safeFindBottleTypeById(it.bottle.capacity),
                    null,
                    null,
                    null,
                    it.origin,
                    0,
                    it.bottle.photoName
                )
            )

            CellarDatabase.getDatabase(context).bottleDao().getLastBottleId()
                ?.let { it1 -> Stock(cellarId, it1, it.stock) }
                ?.let { it2 -> CellarDatabase.getDatabase(context).stockDao().insert(it2) }

        }
    }

    private suspend fun safeFindBottleTypeById(capacity: Double): Int {
        val id = try {
            CellarDatabase.getDatabase(context).bottleTypeDao().findBottleTypeIdByCapacity(capacity)
        } catch (e: Exception) {
            CellarDatabase.getDatabase(context).bottleTypeDao().findBottleTypeIdByCapacity(0.75)
            Log.i("TEST", "Exception for capacity $capacity")
        }
        return id
        //todo : add a report to warn user of errors
    }


}