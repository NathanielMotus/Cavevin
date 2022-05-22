package com.nathaniel.motus.cavevin.model

import com.nathaniel.motus.cavevin.model.Bottle
import com.nathaniel.motus.cavevin.model.Cellar
import com.nathaniel.motus.cavevin.model.JsonObject
import com.nathaniel.motus.cavevin.model.CellComparator
import java.util.ArrayList

class Cell {
    //**********************************************************************************************
    //Getters and setters
    //**********************************************************************************************
    //**********************************************************************************************
    //the bottle
    var bottle: Bottle

    //how the bottle was acquired
    var origin: String

    //Stock
    var stock: Int

    //Comment proper to the cellar
    var cellComment: String

    //Flag expanded, for recyclerview - is not saved in database
    @JvmField
    var isExpanded = false

    //**********************************************************************************************
    //Constructors
    //**********************************************************************************************
    constructor(
        bottle: Bottle,
        origin: String,
        stock: Int,
        cellComment: String,
        isReferenced: Boolean
    ) {
        //Creates a cell which is reference in the CellPool is isReferenced
        this.bottle = bottle
        this.origin = origin
        this.stock = stock
        this.cellComment = cellComment
        if (isReferenced) {
            cellPool.add(this)
        }
    }

    constructor(isReferenced: Boolean) {
        //Creates a cell
        //if isReferenced is true, it is referenced in the CellPool
        //if isReferenced is false, it is not
        bottle = Bottle.Companion.bottleCatalog.get(0)
        origin = ""
        stock = 0
        cellComment = ""
        if (isReferenced) {
            cellPool.add(this)
        }
    }

    //**********************************************************************************************
    //Modifiers
    //**********************************************************************************************
    fun clearCell() {
        //clear a cell of its content
        bottle = Bottle.Companion.bottleCatalog.get(0)
        origin = ""
        stock = 0
        cellComment = ""
    }

    fun setCellParametersOf(cell: Cell) {
        //copy the parameters of cell to this
        bottle = cell.bottle
        origin = cell.origin
        stock = cell.stock
        cellComment = cell.cellComment
    }

    fun removeCell() {
        //remove cell from cellars and cell pool
        //if bottle of this has no use case, remove it from catalog
        val bottle = bottle
        if (findUseCaseCellar(0) != null) findUseCaseCellar(0)?.cellList?.remove(this)
        cellPool.remove(this)
        if (bottle.findUseCaseCell(0) == null) bottle.removeBottleFromCatalog()
    }

    //**********************************************************************************************
    //Manipulators
    //**********************************************************************************************
    fun isBottleUseCase(bottle: Bottle): Boolean {
        //returns true if the bottle is used in this cell
        return if (this.bottle === bottle) true else false
    }

    fun findUseCaseCellar(fromIndex: Int): Cellar? {
        //find the first use case cellar of this
        return if (fromIndex >= Cellar.Companion.cellarPool.size) null else {
            if (Cellar.Companion.cellarPool.get(fromIndex)
                    .isCellUseCase(this)
            ) Cellar.Companion.cellarPool.get(fromIndex) else findUseCaseCellar(fromIndex + 1)
        }
    }

    companion object {
        //a cell is a place where you can store a bottle with a comment, a quantity, an origin and a statut
        //a cellar is an ArrayList of cells
        private const val SEPARATOR = "|"

        //**********************************************************************************************
        //The collection of cells
        //List of the cells
        val cellPool = ArrayList<Cell>()
        val numberOfCells: Int
            get() = cellPool.size

        fun clearCellPool() {
            //empty cell pool
            cellPool.clear()
        }
    }
}