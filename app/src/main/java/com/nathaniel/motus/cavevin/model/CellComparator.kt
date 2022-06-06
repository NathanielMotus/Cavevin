package com.nathaniel.motus.cavevin.model

import com.nathaniel.motus.cavevin.model.Bottle
import com.nathaniel.motus.cavevin.model.Cellar
import com.nathaniel.motus.cavevin.model.JsonObject
import com.nathaniel.motus.cavevin.model.CellComparator
import java.util.*

class CellComparator {

    companion object : Comparator<Cell> {
        //compare cells according appellation, domain, cuvee, vintage, stock
        //ascending or descending
        //in any order specified in ArrayList order (2,0,1,3,4) for example sort by domain first, then cuvee...
        //each fields is modified by ArrayList sortingSense, where 1 is upwards and -1 downwards
        private val compareResults = ArrayList(listOf(0, 0, 0, 0, 0))

        //    **********************************************************************************************
        //    Getters and setters
        //    **********************************************************************************************
        val sortingSense = ArrayList(listOf(1, 1, 1, 1, 1))
        val sortingOrder = ArrayList(listOf(0, 1, 2, 3, 4))
        fun setSortingSense(
            appellationSense: Int,
            domainSense: Int,
            cuveeSense: Int,
            vintageSense: Int,
            stockSense: Int
        ) {
            //set the sorting sense (increasing or decreasing)
            //1 for increasing, -1 for decreasing
            sortingSense[0] = appellationSense
            sortingSense[1] = domainSense
            sortingSense[2] = cuveeSense
            sortingSense[3] = vintageSense
            sortingSense[4] = stockSense
        }

        fun setSortingOrder(
            appellationOrder: Int,
            domainOrder: Int,
            cuveeOrder: Int,
            vintageOrder: Int,
            stockOrder: Int
        ) {
            //set the order for each sort keys (0 to 4)
            sortingOrder[0] = appellationOrder
            sortingOrder[1] = domainOrder
            sortingOrder[2] = cuveeOrder
            sortingOrder[3] = vintageOrder
            sortingOrder[4] = stockOrder
        }
        override fun compare(o1: Cell?, o2: Cell?): Int {

            //compare appellation
            if (o1 != null) {
                if (o2 != null) {
                    compareResults[0] = o1.bottle.appellation.toLowerCase()
                        .compareTo(o2.bottle.appellation.toLowerCase()) * sortingSense[0]
                }
            }

            //compare domain
            if (o1 != null) {
                if (o2 != null) {
                    compareResults[1] = o1.bottle.domain.toLowerCase()
                        .compareTo(o2.bottle.domain.toLowerCase()) * sortingSense[1]
                }
            }

            //compare cuvee
            if (o1 != null) {
                if (o2 != null) {
                    compareResults[2] = o1.bottle.cuvee.toLowerCase()
                        .compareTo(o2.bottle.cuvee.toLowerCase()) * sortingSense[2]
                }
            }

            //compare vintage
            if (o1 != null) {
                if (o2 != null) {
                    compareResults[3] =
                        o1.bottle.vintage.compareTo(o2.bottle.vintage) * sortingSense[3]
                }
            }

            //compare stock
            var cStock = 0
            if (o1 != null) {
                if (o2 != null) {
                    if (o1.stock > o2.stock) cStock = 1 else if (o1.stock < o2.stock) cStock = -1
                }
            }
            compareResults[4] = cStock * sortingSense[4]


            //return the comparison according to the sorting order
            var i = 0
            while (i < 4 && compareResults[sortingOrder.indexOf(i)] == 0) i++
            return compareResults[sortingOrder.indexOf(i)]
        }

    }
}