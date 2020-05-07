package com.nathaniel.motus.cavevin.model;

import android.util.Log;

import java.util.ArrayList;

public class Cellar {
    //a cellar is a collection of cells

    //Variables de débogage
    //**********************************************************************************************
    private static final String TAG="Cellar";
    //**********************************************************************************************
    //The collection of cellars
    private static ArrayList<Cellar> sCellarPool=new ArrayList<>();
    //**********************************************************************************************
//  Types of wine

    private static final String RED_WINE="0";
    private static final String WHITE_WINE="1";
    private static final String PINK_WINE="2";
    //**********************************************************************************************
    //Parameters
    private String mCellarName;
    private ArrayList<Cell> mCellList;

    //**********************************************************************************************
    //Getters and setters
    //**********************************************************************************************

    public ArrayList<Cell> getCellList() {
        return mCellList;
    }

    public void setCellList(ArrayList<Cell> cellList) {
        mCellList = cellList;
    }

    public String getCellarName() {
        return mCellarName;
    }

    public void setCellarName(String cellarName) {
        mCellarName = cellarName;
    }

    public static int getNumberOfCellars() {
        return sCellarPool.size();
    }

    public static ArrayList<Cellar> getCellarPool() {
        return sCellarPool;
    }

    public int getStock(){
        //return number of bottles

        int count=0;
        for (int i=0;i<getCellList().size();i++){
            count=count+getCellList().get(i).getStock();
        }
        return count;
    }

    public int getStock(String wineType){
        //return stock of a type

        int count=0;
        for (int i=0;i<getCellList().size();i++){
            if (getCellList().get(i).getBottle().getType().compareTo(wineType)==0) count=count+getCellList().get(i).getStock();
        }
        return count;
    }

    public static int totalStock(){
        //return overall number of bottles

        int count=0;
        for (int i=0;i<sCellarPool.size();i++){
            count=count+sCellarPool.get(i).getStock();
        }
        return count;
    }

    //**********************************************************************************************
    //Constructors
    //**********************************************************************************************

    public Cellar(String cellarName, ArrayList<Cell> cellList, Boolean isReferenced) {
        //Creates a cellar which is referenced in the cellar pool if isReferenced

        mCellarName = cellarName;
        mCellList = cellList;

        if (isReferenced) {
            sCellarPool.add(this);
        }
    }

    public Cellar(Boolean isReferenced) {
        //creates a cellar which is referenced in the cellar pool only if isReferenced is true

        mCellarName="";
        mCellList=new ArrayList<>();

        if (isReferenced){
            sCellarPool.add(this);
        }
    }

    //**********************************************************************************************
    //Converters
    //**********************************************************************************************

    //**********************************************************************************************
    //Modifiers
    //**********************************************************************************************

    public void setCellarParametersOf(Cellar cellar){
        this.setCellarName(cellar.getCellarName());
        this.setCellList(cellar.getCellList());
    }

    public void destroyCells(){
//        destroy all the cells of this
//        and remove them from cell pool

        if (this.mCellList.size()>0) {
            this.mCellList.get(0).removeCell();
            this.destroyCells();
        }
    }

    public static void clearCellarPool(){
        //empty cellar pool

        sCellarPool.clear();
    }
    //**********************************************************************************************
    //Manipulators
    //**********************************************************************************************

    public Boolean isCellUseCase(Cell cell){
        //returns true if cell is used in this cellar

        if(mCellList.indexOf(cell)!=-1) return true;
        else return false;
    }

    public Cellar typeFiltered(String typeFilter){
        //return this cellar filtered on its type
        //typeFilter can be "all", "red", "white" or "pink"

        Cellar filteredCellar=new Cellar(false);
        String typeQuery;

        switch (typeFilter){
            case "red":
                typeQuery="0";
                break;
            case "white":
                typeQuery="1";
                break;
            case "pink":
                typeQuery="2";
                break;
            default:
                return this;
        }
        int i=0;
        while (i<this.getCellList().size()){
            if (this.getCellList().get(i).getBottle().getType().compareTo(typeQuery)==0)
                filteredCellar.getCellList().add(this.getCellList().get(i));
            i++;
        }

        return filteredCellar;
    }



}
