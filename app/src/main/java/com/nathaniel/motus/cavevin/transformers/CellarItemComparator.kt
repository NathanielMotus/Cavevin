package com.nathaniel.motus.cavevin.transformers

import com.nathaniel.motus.cavevin.data.CellarItem

class CellarItemComparator(val sortPattern: ArrayList<ArrayList<Int>>) : Comparator<CellarItem> {
    //ArrayList of (sortSequence[0..10], sortSense[0..10])
    //sortSense is +1 for ascending, -1 for descending
    //in db, sortSequence and sortPattern associated have the same id
    //0 : appellation
    //1 : domain
    //2 : cuvee
    //3 : vintage
    //4 : capacity
    //5 : stock
    //6 : rating
    //7 : price
    //8 : aging capacity
    //9 : neutral wine color
    //10 : neutral wine stillness

    companion object {
        const val APPELLATION_COMPARE_INDEX = 0
        const val DOMAIN_COMPARE_INDEX = 1
        const val CUVEE_COMPARE_INDEX = 2
        const val VINTAGE_COMPARE_INDEX = 3
        const val CAPACITY_COMPARE_INDEX = 4
        const val STOCK_COMPARE_INDEX = 5
        const val RATING_COMPARE_INDEX = 6
        const val PRICE_COMPARE_INDEX = 7
        const val AGING_CAPACITY_COMPARE_INDEX = 8
        const val NEUTRAL_WINE_COLOR_COMPARE_INDEX = 9
        const val NEUTRAL_WINE_STILLNESS_COMPARE_INDEX = 10
        const val ASCENDING_SORT=1
        const val DESCENDING_SORT=-1
    }

    private var comparisons = ArrayList(listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

    override fun compare(p0: CellarItem?, p1: CellarItem?): Int {

        //compare appellation
        if (p0 != null) {
            if (p1 != null) {
                comparisons[APPELLATION_COMPARE_INDEX] =
                    p1.appellation?.let { p0.appellation?.compareTo(it) }
            }
        }

        //compare domain
        if (p0 != null) {
            if (p1 != null) {
                comparisons[DOMAIN_COMPARE_INDEX] = p1.domain?.let { p0.domain?.compareTo(it) }
            }
        }

        //compare cuvee
        if (p0 != null) {
            if (p1 != null) {
                comparisons[CUVEE_COMPARE_INDEX] = p1.cuvee?.let { p0.cuvee?.compareTo(it) }
            }
        }

        //compare vintage
        if (p0 != null) {
            if (p1 != null) {
                comparisons[VINTAGE_COMPARE_INDEX] = p1.vintage?.let { p0.vintage?.compareTo(it) }
            }
        }

        //compare capacity
        if (p0 != null) {
            if (p1 != null) {
                comparisons[CAPACITY_COMPARE_INDEX] = p1.capacity.let { p0.capacity.compareTo(it) }
            }
        }

        //compare stock
        if (p0 != null) {
            if (p1 != null) {
                comparisons[STOCK_COMPARE_INDEX] = p1.quantity.let { p0.quantity.compareTo(it) }
            }
        }

        //compare rating
        if (p0 != null) {
            if (p1 != null) {
                comparisons[RATING_COMPARE_INDEX] = p1.rating.let { p0.rating.compareTo(it) }
            }
        }

        //compare price
        if (p0 != null) {
            if (p1 != null) {
                comparisons[PRICE_COMPARE_INDEX] = p1.price?.let { p0.price?.compareTo(it) }
            }
        }

        //compare aging capacity
        if (p0 != null) {
            if (p1 != null) {
                comparisons[AGING_CAPACITY_COMPARE_INDEX] =
                    p1.agingCapacity?.let { p0.agingCapacity?.compareTo(it) }
            }
        }

        //compare neutral wine color
        if (p0 != null) {
            if (p1 != null) {
                comparisons[NEUTRAL_WINE_COLOR_COMPARE_INDEX] =
                    p1.neutralWineColor?.let { p0.neutralWineColor?.compareTo(it) }
            }
        }

        //compare neutral wine stillness
        if (p0 != null) {
            if (p1 != null) {
                comparisons[NEUTRAL_WINE_STILLNESS_COMPARE_INDEX] =
                    p1.neutralWineStillness?.let { p0.neutralWineStillness?.compareTo(it) }
            }
        }

        var i = 0
        while (i < sortPattern[0].size-1 && comparisons[sortPattern[0][i]] == 0)
            i++
        return comparisons[sortPattern[0][i]] * sortPattern[1][i]
    }
}