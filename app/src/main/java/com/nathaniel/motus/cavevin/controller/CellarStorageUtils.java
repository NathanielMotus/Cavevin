package com.nathaniel.motus.cavevin.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.nathaniel.motus.cavevin.model.Bottle;
import com.nathaniel.motus.cavevin.model.Cell;
import com.nathaniel.motus.cavevin.model.Cellar;
import com.nathaniel.motus.cavevin.model.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CellarStorageUtils {
    //Utilitary class meant to convert cellars, cells and bottles to string
    //to convert string to cellars, cells or bottles
    //save to and get from text files these strings

    //**********************************************************************************************
    //debug constants
    private static final String TAG="CellarStorageUtils";
    //**********************************************************************************************

    private static final String SEPARATOR="|";

//    JSON for bottle
    private static final String JSON_BOTTLES="bottles";
    private static final String JSON_APPELLATION="appellation";
    private static final String JSON_DOMAIN="domain";
    private static final String JSON_CUVEE="cuvee";
    private static final String JSON_VINTAGE="vintage";
    private static final String JSON_BOTTLE_NAME="bottle name";
    private static final String JSON_CAPACITY="capacity";
    private static final String JSON_TYPE="type";
    private static final String JSON_BOTTLE_COMMENT="bottle comment";
    private static final String JSON_BOTTLE_PHOTO_NAME="photo name";

//    JSON for cell
    private static final String JSON_CELLS="cells";
    private static final String JSON_BOTTLE_INDEX="bottle index";
    private static final String JSON_ORIGIN="origin";
    private static final String JSON_STOCK="stock";
    private static final String JSON_CELL_COMMENT="cell comment";

//    JSON for cellar
    private static final String JSON_CELLARS="cellars";
    private static final String JSON_CELL_INDEX="cell index";
    private static final String JSON_NAME="name";

    //**********************************************************************************************
    //Constructor, private not to instantiate the class
    //**********************************************************************************************
    private CellarStorageUtils(){}

    //**********************************************************************************************
    //Converters
    //**********************************************************************************************

    public static String bottleToString(Bottle bottle){
        //Convert a bottle into a string line to be written in a text file
        //ERROR : TO BE REDONE IF NEED BE

        return "ERROR : SUB NEEDS TO BE REDONE";
    }

    public static JsonObject bottleToJsonObject(Bottle bottle) {
//        convert a bottle to a JSON object

        JsonObject jsonObject=new JsonObject();
        if (bottle != null) {
            jsonObject.addKeyValue(JSON_APPELLATION,bottle.getAppellation());
            jsonObject.addKeyValue(JSON_DOMAIN,bottle.getDomain());
            jsonObject.addKeyValue(JSON_CUVEE,bottle.getCuvee());
            jsonObject.addKeyValue(JSON_TYPE,bottle.getType());
            jsonObject.addKeyValue(JSON_VINTAGE,bottle.getVintage());
            jsonObject.addKeyValue(JSON_BOTTLE_NAME,bottle.getBottleName());
            jsonObject.addKeyValue(JSON_CAPACITY,Float.toString(bottle.getCapacity()));
            jsonObject.addKeyValue(JSON_BOTTLE_COMMENT,bottle.getBottleComment());
            jsonObject.addKeyValue(JSON_BOTTLE_PHOTO_NAME,bottle.getPhotoName());
        }
        return jsonObject;
    }

    public static String cellToString(Cell cell){
        //converts a cell into a string line to be written in a text file
        //bottle is defined by its index

        if (cell!=null) {

            String currentString = "";

            currentString = currentString + Bottle.getBottleCatalog().indexOf(cell.getBottle()) + SEPARATOR;
            currentString = currentString + cell.getOrigin() + SEPARATOR;
            currentString = currentString + cell.getStock() + SEPARATOR;
            currentString = currentString + cell.getCellComment() + SEPARATOR;

            return currentString;
        }
        else return "null";
    }

    public static JsonObject cellToJsonObject(Cell cell){
        //convert a cell into a JsonObject
        //bottle is define by its index

        if (cell!=null){
            JsonObject jsonObject=new JsonObject();

            jsonObject.addKeyValue(JSON_BOTTLE_INDEX,Integer.toString(Bottle.getBottleCatalog().indexOf(cell.getBottle())));
            jsonObject.addKeyValue(JSON_ORIGIN,cell.getOrigin());
            jsonObject.addKeyValue(JSON_STOCK,Integer.toString(cell.getStock()));
            jsonObject.addKeyValue(JSON_CELL_COMMENT,cell.getCellComment());

            return jsonObject;
        }
        else return new JsonObject();
    }

    public static String cellarToString(Cellar cellar){
        //Converts a cellar into a text line to be written in a text file
        //it starts with the name of the cellar, then the refs of the cells

        if (cellar!=null) {
            String cellarString = cellar.getCellarName() + SEPARATOR;

            for (int i = 0; i < cellar.getCellList().size(); i++)
                cellarString = cellarString + Cell.getCellPool().indexOf(cellar.getCellList().get(i)) + SEPARATOR;

            return cellarString;
        }
        else return "null";
    }

    public static JsonObject cellarToJsonObject(Cellar cellar){
        //convert a cellar into a JsonObject
        //it starts with the name of the cellar, then the indexes of the cells

        JsonObject jsonObject=new JsonObject();

        jsonObject.addKeyValue(JSON_NAME,cellar.getCellarName());

        ArrayList<Object> objectArrayList=new ArrayList<>();

        for (int i=0;i<cellar.getCellList().size();i++){
            objectArrayList.add(Cell.getCellPool().indexOf(cellar.getCellList().get(i)));
        }

        jsonObject.addKeyValue(JSON_CELLS,objectArrayList);

        return jsonObject;
    }

    public static JsonObject bottleCatalogToJsonObject(){
        //change the bottle catalog into a JsonObject

        JsonObject jsonObject=new JsonObject();

        ArrayList<Object> objectArrayList=new ArrayList<>();

        for (int i=0;i<Bottle.getBottleCatalog().size();i++){
            objectArrayList.add(bottleToJsonObject(Bottle.getBottleCatalog().get(i)));
        }

        jsonObject.addKeyValue(JSON_BOTTLES,objectArrayList);

        return jsonObject;
    }

    public static JsonObject cellPoolToJsonObject(){
        //change the cell pool into a JsonObject

        JsonObject jsonObject=new JsonObject();

        ArrayList<Object> objectArrayList=new ArrayList<>();

        for (int i=0;i<Cell.getCellPool().size();i++){
            objectArrayList.add(cellToJsonObject(Cell.getCellPool().get(i)));
        }

        jsonObject.addKeyValue(JSON_CELLS,objectArrayList);

        return jsonObject;
    }

    public static JsonObject cellarPoolToJsonObject(){
        //change the cellar pool into a JsonObject

        JsonObject jsonObject=new JsonObject();

        ArrayList<Object> jsonObjectArrayList=new ArrayList<>();

        for (int i=0;i<Cellar.getCellarPool().size();i++){
            jsonObjectArrayList.add(cellarToJsonObject(Cellar.getCellarPool().get(i)));
        }

        jsonObject.addKeyValue(JSON_CELLARS,jsonObjectArrayList);

        return jsonObject;
    }

    public static JsonObject dataBaseToJsonObject(){
        //change the whole data base into a JsonObject

        JsonObject jsonObject=new JsonObject();

        jsonObject.addJsonObject(bottleCatalogToJsonObject());
        jsonObject.addJsonObject(cellPoolToJsonObject());
        jsonObject.addJsonObject(cellarPoolToJsonObject());

        return jsonObject;

    }

    //**********************************************************************************************
    //I/O methods
    //**********************************************************************************************

    public static File createOrGetFile(File destination,String folderName, String fileName){
        File folder = new File(destination,folderName);
        return new File(folder,fileName);
    }

    public static void saveDataBase(File file){
        //save the whole database

        try{
            file.getParentFile().mkdirs();
            FileWriter fileWriter=new FileWriter(file);
            Writer w=new BufferedWriter(fileWriter);

            try{
                w.write(dataBaseToJsonObject().jsonToString());
                Log.i(TAG,dataBaseToJsonObject().jsonToString());
            }finally {
                w.close();
                Log.i(TAG,"Database enregistrée");
            }
        }catch (IOException e){
            Log.i(TAG,"Enregistrement de la database raté");
        }
    }

    public static void loadDataBase(File file){
        //load the whole database

        String dataBaseString= "";
        if (file.exists()){
            BufferedReader bufferedReader;
            try{
                bufferedReader=new BufferedReader(new FileReader(file));
                try{
                    String readString=bufferedReader.readLine();
                    while (readString!=null){
                        dataBaseString=dataBaseString+readString;
                        Log.i(TAG,dataBaseString);
                        //next line
                        readString=bufferedReader.readLine();
                    }
                }finally {
                    bufferedReader.close();
                    Log.i(TAG, "Chargement de la database réussi");
                }
            }catch (IOException e){
                Log.i(TAG,"Chargement de la database raté");
            }
        }
        if (dataBaseString!="") {
            JsonObject jsonDatabase = JsonObject.stringToJsonObject(dataBaseString);
            createDataBase(jsonDatabase);
        }
    }

    public static void writeDatabaseToExportFile(Context context,Uri uri){
        //write the database once the export file is open

        OutputStream outputStream;
        try {
            outputStream=context.getContentResolver().openOutputStream(uri);
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(CellarStorageUtils.dataBaseToJsonObject().jsonToString());
            bw.flush();
            bw.close();
            Toast.makeText(context,"DataBase saved",Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            Toast.makeText(context,"Save failed",Toast.LENGTH_SHORT).show();
        }
    }

    public static void readDataBaseFromImportFile(Context context,Uri uri){
        //read the database once the import file is open

        InputStream inputStream;
        String dataBaseString="";
        try {
            inputStream=context.getContentResolver().openInputStream(uri);
            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
            String readString=br.readLine();
            while (readString!=null){
                dataBaseString=dataBaseString+readString;
                readString=br.readLine();
            }
        }catch (IOException e){
            Toast.makeText(context,"Load failed",Toast.LENGTH_SHORT).show();
        }
        JsonObject jsonDatabase=new JsonObject();
        jsonDatabase=JsonObject.stringToJsonObject(dataBaseString);
        clearDataBase();
        createDataBase(jsonDatabase);
    }

    private static void createDataBase(JsonObject jsonDataBase){
        //create database from readfile JsonObject

        clearDataBase();

        JsonObject currentJsonObject=new JsonObject();

        //Create bottles
        ArrayList<Object> bottleJsonObjectArrayList=jsonDataBase.getKeyValueArray(JSON_BOTTLES);

        for (int i=0;i<bottleJsonObjectArrayList.size();i++){
            currentJsonObject=(JsonObject)bottleJsonObjectArrayList.get(i);
            Bottle bottle=new Bottle(currentJsonObject.getKeyValue(JSON_APPELLATION),currentJsonObject.getKeyValue(JSON_DOMAIN),currentJsonObject.getKeyValue(JSON_CUVEE),
                    currentJsonObject.getKeyValue(JSON_TYPE),currentJsonObject.getKeyValue(JSON_VINTAGE),currentJsonObject.getKeyValue(JSON_BOTTLE_NAME),
                    Float.parseFloat(currentJsonObject.getKeyValue(JSON_CAPACITY)),currentJsonObject.getKeyValue(JSON_BOTTLE_COMMENT),
                    currentJsonObject.getKeyValue(JSON_BOTTLE_PHOTO_NAME),true);
        }

        //Create cells
        ArrayList<Object> cellJsonObjectArrayList=jsonDataBase.getKeyValueArray(JSON_CELLS);
        for (int i=0;i<cellJsonObjectArrayList.size();i++){
            currentJsonObject=(JsonObject)cellJsonObjectArrayList.get(i);
            Cell cell=new Cell(Bottle.getBottleCatalog().get(Integer.parseInt(currentJsonObject.getKeyValue(JSON_BOTTLE_INDEX))),currentJsonObject.getKeyValue(JSON_ORIGIN),
                    Integer.parseInt(currentJsonObject.getKeyValue(JSON_STOCK)),currentJsonObject.getKeyValue(JSON_CELL_COMMENT),true);
        }

        //Create cellars
        ArrayList<Object> cellarJsonObjectArrayList=jsonDataBase.getKeyValueArray(JSON_CELLARS);
        for (int i=0;i<cellarJsonObjectArrayList.size();i++){
            currentJsonObject=(JsonObject)cellarJsonObjectArrayList.get(i);
            ArrayList<Object> cellObjectArrayList=currentJsonObject.getKeyValueArray(JSON_CELLS);
            ArrayList<Cell> cellArrayList=new ArrayList<>();
            for (int j=0;j<cellObjectArrayList.size();j++){
                cellArrayList.add(Cell.getCellPool().get((Integer)(cellObjectArrayList.get(j))));
            }
            Cellar cellar=new Cellar(currentJsonObject.getKeyValue(JSON_NAME),cellArrayList,true);
        }
    }

    public static void clearDataBase(){
        //empty database

        Cellar.clearCellarPool();
        Cell.clearCellPool();
        Bottle.clearBottleCatalog();
    }

    public static String saveBottleImageToInternalStorage(File destination,String folderName,Bitmap bitmap){
        //save the photo to the internal storage and return the pathname
        //the photo name is a timestamp

        String timeStamp=new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File file=createOrGetFile(destination,folderName,timeStamp);

        try{
            file.getParentFile().mkdirs();
            OutputStream stream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return timeStamp;
    }

    public static Bitmap getBitmapFromInternalStorage(File destination,String folderName,String fileName){
        //get the photo in internal storage

        File file=createOrGetFile(destination,folderName,fileName);
        Bitmap bitmap=null;

        try{
            file.getParentFile().mkdirs();
            InputStream stream=new FileInputStream(file);
            bitmap= BitmapFactory.decodeStream(stream);
            stream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }


    public static Bitmap getBitmapFromUri(Context context, Uri uri){
        //get a bitmap from an Uri

        Bitmap bitmap=null;
        try {
            InputStream stream = context.getContentResolver().openInputStream(uri);
            bitmap=BitmapFactory.decodeStream(stream);
        }catch (FileNotFoundException e){
            Toast.makeText(context,"Erreur",Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromFile(File destination,String folderName,String fileName,int reqWidth, int reqHeight) {

        File file = createOrGetFile(destination, folderName, fileName);
        Bitmap bitmap = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            file.getParentFile().mkdirs();
            options.inJustDecodeBounds = true;
            InputStream stream = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(stream, null, options);
            stream.close();

            // Calculate inSampleSize
            options.inSampleSize = CellarPictureUtils.calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            InputStream stream1=new FileInputStream(file);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(stream1, null, options);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    public static void deleteFileFromInternalStorage(File destination,String folderName, String fileName){
        //delete a file frome internal storage
        //use to delete bottle photos

        File file=createOrGetFile(destination,folderName,fileName);
        boolean deleted=file.delete();
    }
}
