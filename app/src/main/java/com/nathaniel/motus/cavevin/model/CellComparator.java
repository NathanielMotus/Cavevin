package com.nathaniel.motus.cavevin.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CellComparator implements Comparator<Cell> {
    //compare cells according appellation, domain, cuvee, vintage, stock
    //ascending or descending
    //in any order specified in ArrayList order (2,0,1,3,4) for example sort by domain first, then cuvee...
    //each fields is modified by ArrayList sortingSense, where 1 is upwards and -1 downwards

    private static ArrayList<Integer> compareResults=new ArrayList<>(Arrays.asList(0,0,0,0,0));
    private static ArrayList<Integer> sortingSense=new ArrayList<>(Arrays.asList(1,1,1,1,1));
    private static ArrayList<Integer> sortingOrder=new ArrayList<>(Arrays.asList(0,1,2,3,4));

    @Override
    public int compare(Cell o1, Cell o2) {

        //compare appellation
        compareResults.set(0,o1.getBottle().getAppellation().compareTo(o2.getBottle().getAppellation())*sortingSense.get(0));

        //compare domain
        compareResults.set(1,o1.getBottle().getDomain().compareTo(o2.getBottle().getDomain())*sortingSense.get(1));

        //compare cuvee
        compareResults.set(2,o1.getBottle().getCuvee().compareTo(o2.getBottle().getCuvee())*sortingSense.get(2));

        //compare vintage
        compareResults.set(3,o1.getBottle().getVintage().compareTo(o2.getBottle().getVintage())*sortingSense.get(3));

        //compare stock
        int cStock=0;
        if (o1.getStock()>o2.getStock()) cStock=1;
        else if (o1.getStock()<o2.getStock()) cStock=-1;
        compareResults.set(4,cStock*sortingSense.get(4));


        //return the comparison according to the sorting order
        int i=0;
        while (i<4 && compareResults.get(sortingOrder.indexOf(i))==0) i++;

        return compareResults.get(sortingOrder.indexOf(i));
    }
//    **********************************************************************************************
//    Getters and setters
//    **********************************************************************************************

    public static ArrayList<Integer> getSortingSense() {
        return sortingSense;
    }

    public static void setSortingSense(int appellationSense,int domainSense,int cuveeSense,int vintageSense,int stockSense) {
        //set the sorting sense (increasing or decreasing)
        //1 for increasing, -1 for decreasing
        CellComparator.sortingSense.set(0,appellationSense);
        CellComparator.sortingSense.set(1,domainSense);
        CellComparator.sortingSense.set(2,cuveeSense);
        CellComparator.sortingSense.set(3,vintageSense);
        CellComparator.sortingSense.set(4,stockSense);
    }

    public static ArrayList<Integer> getSortingOrder() {
        return sortingOrder;
    }

    public static void setSortingOrder(int appellationOrder,int domainOrder,int cuveeOrder,int vintageOrder,int stockOrder) {
        //set the order for each sort keys (0 to 4)
        CellComparator.sortingOrder.set(0,appellationOrder);
        CellComparator.sortingOrder.set(1,domainOrder);
        CellComparator.sortingOrder.set(2,cuveeOrder);
        CellComparator.sortingOrder.set(3,vintageOrder);
        CellComparator.sortingOrder.set(4,stockOrder);
    }
}
