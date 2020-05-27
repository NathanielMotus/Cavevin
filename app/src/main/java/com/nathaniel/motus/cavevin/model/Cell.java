package com.nathaniel.motus.cavevin.model;

import android.util.Log;

import java.util.ArrayList;

public class Cell {
    //a cell is a place where you can store a bottle with a comment, a quantity, an origin and a statut
    //a cellar is an ArrayList of cells

    private static final String SEPARATOR="|";

    //**********************************************************************************************
    //The collection of cells

    //List of the cells
    private static ArrayList<Cell> sCellPool=new ArrayList<>();
    //**********************************************************************************************

    //the bottle
    private Bottle mBottle;

    //how the bottle was acquired
    private String mOrigin;

    //Stock
    private int mStock;

    //Comment proper to the cellar
    private String mCellComment;

    //Flag expanded, for recyclerview - is not saved in database
    public boolean isExpanded=false;

    //**********************************************************************************************
    //Getters and setters
    //**********************************************************************************************

    public Bottle getBottle() {
        return mBottle;
    }

    public void setBottle(Bottle bottle) {
        mBottle = bottle;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String origin) {
        mOrigin = origin;
    }

    public int getStock() {
        return mStock;
    }

    public void setStock(int stock) {
        mStock = stock;
    }

    public String getCellComment() {
        return mCellComment;
    }

    public void setCellComment(String cellComment) {
        mCellComment = cellComment;
    }

    public static int getNumberOfCells() {
        return sCellPool.size();
    }

    public static ArrayList<Cell> getCellPool() {
        return sCellPool;
    }

    //**********************************************************************************************
    //Constructors
    //**********************************************************************************************

    public Cell(Bottle bottle, String origin, int stock, String cellComment,Boolean isReferenced) {
        //Creates a cell which is reference in the CellPool is isReferenced
        mBottle = bottle;
        mOrigin = origin;
        mStock = stock;
        mCellComment = cellComment;

        if(isReferenced) {
            sCellPool.add(this);
        }
    }

    public Cell(Boolean isReferenced) {
        //Creates a cell
        //if isReferenced is true, it is referenced in the CellPool
        //if isReferenced is false, it is not
        mBottle=Bottle.getBottleCatalog().get(0);
        mOrigin="";
        mStock=0;
        mCellComment="";

        if(isReferenced) {
            sCellPool.add(this);
        }
    }

    //**********************************************************************************************
    //Modifiers
    //**********************************************************************************************

    public void clearCell(){
        //clear a cell of its content

        mBottle=Bottle.getBottleCatalog().get(0);
        mOrigin="";
        mStock=0;
        mCellComment="";
    }

    public void setCellParametersOf(Cell cell){
        //copy the parameters of cell to this

        this.setBottle(cell.getBottle());
        this.setOrigin(cell.getOrigin());
        this.setStock(cell.getStock());
        this.setCellComment(cell.getCellComment());
    }

    public void removeCell(){
        //remove cell from cellars and cell pool
        //if bottle of this has no use case, remove it from catalog

        Bottle bottle=this.getBottle();

        if (this.findUseCaseCellar(0)!=null) this.findUseCaseCellar(0).getCellList().remove(this);
        sCellPool.remove(this);

        if (bottle.findUseCaseCell(0) == null) bottle.removeBottleFromCatalog();
    }

    public static void clearCellPool(){
        //empty cell pool

        sCellPool.clear();
    }

    //**********************************************************************************************
    //Manipulators
    //**********************************************************************************************

    public Boolean isBottleUseCase(Bottle bottle){
        //returns true if the bottle is used in this cell

        if (mBottle==bottle) return true;
        else return false;
    }

    public Cellar findUseCaseCellar(int fromIndex){
        //find the first use case cellar of this

        if (fromIndex>=Cellar.getCellarPool().size()) return null;
        else{
            if (Cellar.getCellarPool().get(fromIndex).isCellUseCase(this)) return Cellar.getCellarPool().get(fromIndex);
            else return findUseCaseCellar(fromIndex+1);
        }
    }

}
