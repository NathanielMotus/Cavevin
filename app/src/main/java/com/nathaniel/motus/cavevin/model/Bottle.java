package com.nathaniel.motus.cavevin.model;

import java.util.ArrayList;

public class Bottle {

    //**********************************************************************************************
    //Variables de déboggage
    private static final String TAG="Bottle";
    //**********************************************************************************************

    private static final String SEPARATOR="|";

    //**********************************************************************************************
    //The collection of bottles

    //Liste des références
    private static ArrayList<Bottle> sBottleCatalog =new ArrayList<>();
    //**********************************************************************************************

    //**********************************************************************************************
    //Parameters

    //Couleur du vin : Rouge, Blanc ou Rosé (0, 1 ou 2)
    private String mType;

    //Contenance de la bouteille
    /*
    Le piccolo : 20 cl (1/4 de bouteille)
    La chopine ou le quart : 25 cl (1/3 de bouteille)
    Le demi ou la fillette : 37,5 cl (1/2 bouteille)
    Le pot : 46cl (2/3 de bouteille)
    La bouteille : 75 cl
    Le magnum : 1.5 L (2 bouteilles)
    La Marie-Jeanne ou le double magnum : 3 L (4 bouteilles)
    Le réhoboam : 4.50 L (6 bouteilles)
    Le jéroboam : 5 L (presque 7 bouteilles)
    Le mathusalem ou l’impériale : 6 L (8 bouteilles)
    Le salmanazar : 9 L (12 bouteilles)
    Le balthazar : 12 L (16 bouteilles)
    Le nabuchodonosor : 15 L(20 bouteilles)
    Le melchior : 18 L (24 bouteilles)
    */
    private String mBottleName; //nom de la bouteille
    private float mCapacity;     //volume en litres

    //Millésime
    private String mVintage;

    //Appellation
    private String mAppellation;

    //Domaine
    private String mDomain;

    //Cuvée
    private String mCuvee;

    //Commentaire : champ libre propre à la bouteille
    private String mBottleComment;

    //**********************************************************************************************
    //Getters and setters
    //**********************************************************************************************

    public static int getNumberOfReferences() {
        return sBottleCatalog.size();
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getBottleName() {
        return mBottleName;
    }

    public void setBottleName(String bottleName) {
        mBottleName = bottleName;
    }

    public float getCapacity() {
        return mCapacity;
    }

    public void setCapacity(float capacity) {
        mCapacity = capacity;
    }

    public String getVintage() {
        return mVintage;
    }

    public void setVintage(String vintage) {
        mVintage = vintage;
    }

    public String getAppellation() {
        return mAppellation;
    }

    public void setAppellation(String appellation) {
        mAppellation = appellation;
    }

    public String getDomain() {
        return mDomain;
    }

    public void setDomain(String domain) {
        mDomain = domain;
    }

    public String getCuvee() {
        return mCuvee;
    }

    public void setCuvee(String cuvee) {
        mCuvee = cuvee;
    }

    public String getBottleComment() {
        return mBottleComment;
    }

    public void setBottleComment(String bottleComment) {
        mBottleComment = bottleComment;
    }

    public static ArrayList<Bottle> getBottleCatalog() {
        return sBottleCatalog;
    }

    //**********************************************************************************************
    //Constructors
    //**********************************************************************************************

    public Bottle(Boolean isReferenced){
        //create a new bottle
        //if isReferenced is true, it is referenced in the BottleCatalog
        //if isReferenced is false, it is not referenced
        mType="";
        mBottleName="";
        mCapacity=0.0f;
        mVintage="";
        mAppellation="";
        mDomain="";
        mCuvee="";
        mBottleComment ="";

        if(isReferenced) {
            sBottleCatalog.add(this);
        }
    }

    public Bottle(String appellation,String domain,String cuvee,String type, String vintage,String bottleName, Float capacity,String bottleComment,Boolean isReferenced){
        //creates a complete bottle

        mAppellation=appellation;
        mDomain=domain;
        mCuvee=cuvee;
        mType=type;
        mVintage=vintage;
        mBottleName=bottleName;
        mCapacity=capacity;
        mBottleComment=bottleComment;

        if(isReferenced) {
            sBottleCatalog.add(this);
        }

    }

    //**********************************************************************************************
    //Modifiers
    //**********************************************************************************************

    public void clearBottle(){
        //Clear all the fields of a bottle

        mType="";
        mBottleName="";
        mCapacity=0.0f;
        mVintage="";
        mAppellation="";
        mDomain="";
        mCuvee="";
        mBottleComment="";
    }

    public void setBottleParametersOf(Bottle bottle){
        //copy the parameters of bottle to this

        this.setAppellation(bottle.getAppellation());
        this.setDomain(bottle.getDomain());
        this.setCuvee(bottle.getCuvee());
        this.setType(bottle.getType());
        this.setVintage(bottle.getVintage());
        this.setBottleName(bottle.getBottleName());
        this.setCapacity(bottle.getCapacity());
        this.setBottleComment(bottle.getBottleComment());
    }

    public void removeBottleFromCatalog(){
        //removes bottle from the bottle catalog
        //removes every use cases cells too
        //and finally removes these cells entries from the cellars

        while (this.findUseCaseCell(0)!=null) this.findUseCaseCell(0).removeCell();
        sBottleCatalog.remove(this);
    }

    public static void clearBottleCatalog(){
        //empty bottle catalog

        sBottleCatalog.clear();
    }

    //**********************************************************************************************
    //Manipulators
    //**********************************************************************************************

    public Cell findUseCaseCell(int fromIndex){
        //find the first use case cell of this from fromIndex

        if (fromIndex>=Cell.getCellPool().size()) return null;
        else
            if (Cell.getCellPool().get(fromIndex).isBottleUseCase(this)) return Cell.getCellPool().get(fromIndex);
            else return findUseCaseCell(fromIndex+1);
    }




}
