package com.nathaniel.motus.cavevin.model

import com.nathaniel.motus.cavevin.model.Bottle
import com.nathaniel.motus.cavevin.model.Cellar
import com.nathaniel.motus.cavevin.model.JsonObject
import com.nathaniel.motus.cavevin.model.CellComparator
import java.util.ArrayList

class Cellar {
    //**********************************************************************************************
    //Parameters
    var cellarName: String

    //**********************************************************************************************
    //Getters and setters
    //**********************************************************************************************
    var cellList: ArrayList<Cell>

    //return number of bottles
    val stock: Int
        get() {
            //return number of bottles
            var count = 0
            for (i in cellList.indices) {
                count = count + (cellList[i]?.stock ?: 0)
            }
            return count
        }

    fun getStock(wineType: String?): Int {
        //return stock of a type
        var count = 0
        for (i in cellList.indices) {
            if (wineType?.let { cellList[i]?.bottle?.type?.compareTo(it) } == 0) count =
                count + (cellList[i]?.stock ?: 0)
        }
        return count
    }

    //**********************************************************************************************
    //Constructors
    //**********************************************************************************************
    constructor(cellarName: String, cellList: ArrayList<Cell>, isReferenced: Boolean) {
        //Creates a cellar which is referenced in the cellar pool if isReferenced
        this.cellarName = cellarName
        this.cellList = cellList
        if (isReferenced) {
            cellarPool.add(this)
        }
    }

    constructor(isReferenced: Boolean) {
        //creates a cellar which is referenced in the cellar pool only if isReferenced is true
        cellarName = ""
        cellList = ArrayList()
        if (isReferenced) {
            cellarPool.add(this)
        }
    }

    //**********************************************************************************************
    //Converters
    //**********************************************************************************************
    //**********************************************************************************************
    //Modifiers
    //**********************************************************************************************
    fun setCellarParametersOf(cellar: Cellar) {
        cellarName = cellar.cellarName
        cellList = cellar.cellList
    }

    fun destroyCells() {
//        destroy all the cells of this
//        and remove them from cell pool
        if (cellList.size > 0) {
            cellList[0]!!.removeCell()
            destroyCells()
        }
    }

    //**********************************************************************************************
    //Manipulators
    //**********************************************************************************************
    fun isCellUseCase(cell: Cell?): Boolean {
        //returns true if cell is used in this cellar
        return if (cellList.indexOf(cell) != -1) true else false
    }

    fun typeFiltered(typeFilter: String?): Cellar {
        //return this cellar filtered on its type
        //typeFilter can be "all", "red", "white" or "pink"
        val filteredCellar = Cellar(false)
        val typeQuery: String
        typeQuery = when (typeFilter) {
            "red" -> "0"
            "white" -> "1"
            "pink" -> "2"
            else -> return this
        }
        var i = 0
        while (i < cellList.size) {
            if ((cellList[i]?.bottle?.type?.compareTo(typeQuery) ?: 0)==0) filteredCellar.cellList.add(
                cellList[i]
            )
            i++
        }
        return filteredCellar
    }

    companion object {
        //a cellar is a collection of cells
        //Variables de débogage
        //**********************************************************************************************
        private const val TAG = "Cellar"

        //**********************************************************************************************
        //The collection of cellars
        val cellarPool = ArrayList<Cellar>()

        //**********************************************************************************************
        //  Types of wine
        private const val RED_WINE = "0"
        private const val WHITE_WINE = "1"
        private const val PINK_WINE = "2"
        val numberOfCellars: Int
            get() = cellarPool.size

        fun totalStock(): Int {
            //return overall number of bottles
            var count = 0
            for (i in cellarPool.indices) {
                count = count + cellarPool[i].stock
            }
            return count
        }

        fun clearCellarPool() {
            //empty cellar pool
            cellarPool.clear()
        }
    }
}