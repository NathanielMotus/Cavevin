package com.nathaniel.motus.cavevin.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.model.Bottle;
import com.nathaniel.motus.cavevin.model.Cell;
import com.nathaniel.motus.cavevin.model.Cellar;
import com.nathaniel.motus.cavevin.model.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CellarStorageUtils {
    //Utilitary class meant to convert cellars, cells and bottles to string
    //to convert string to cellars, cells or bottles
    //save to and get from text files these strings

    //**********************************************************************************************
    //debug constants
    private static final String TAG="CellarStorageUtils";
    //**********************************************************************************************

    private static final String SEPARATOR="|";
    private static final String CSV_SEPARATOR=";";

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

    public static String cellToCsvLine(Context context,Cell cell) {
        //Convert a cell to a string, meant to be added to a CSV file

        String csvString="";
        Bottle bottle=cell.getBottle();

        String type;
        switch (bottle.getType()) {
            case "0":
                type=context.getString(R.string.wine_red);
                break;
            case "1":
                type=context.getString(R.string.wine_white);
                break;
            default:
                type= context.getString(R.string.wine_pink);
        }

        csvString=csvString+type+CSV_SEPARATOR;
        csvString=csvString+bottle.getAppellation()+CSV_SEPARATOR;
        csvString=csvString+bottle.getDomain()+CSV_SEPARATOR;
        csvString=csvString+bottle.getCuvee()+CSV_SEPARATOR;
        csvString=csvString+bottle.getBottleName()+CSV_SEPARATOR;
        csvString=csvString+bottle.getCapacity() +CSV_SEPARATOR;
        csvString=csvString+bottle.getVintage()+CSV_SEPARATOR;
        csvString=csvString+bottle.getBottleComment()+CSV_SEPARATOR;
        csvString=csvString+cell.getStock()+CSV_SEPARATOR;
        csvString=csvString+cell.getOrigin()+CSV_SEPARATOR;
        csvString=csvString+cell.getCellComment()+CSV_SEPARATOR;

        return csvString;
    }

    //**********************************************************************************************
    //I/O methods
    //**********************************************************************************************

    public static File createOrGetFile(File destination,String folderName, String fileName){
        File folder = new File(destination,folderName);
        return new File(folder,fileName);
    }

    public static void deleteRecursive(File fileOrFolder) {
        //delete folders and files within

        if (fileOrFolder.isDirectory()) {
            for (File child : fileOrFolder.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrFolder.delete();
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
                Log.i(TAG,"Database saved");
            }
        }catch (IOException e){
            Log.i(TAG,"Failed saving database");
        }
    }

    public static void loadDataBase(Context context,File file){
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
                    Log.i(TAG, "Database loaded");
                }
            }catch (IOException e){
                Log.i(TAG,"Failed load database");
            }
        }
        if (dataBaseString!="") {
            JsonObject jsonDatabase = JsonObject.stringToJsonObject(dataBaseString);
            createDataBase(context,jsonDatabase);
        }
    }

    public static void exportCellarToCsvFile(Context context,Uri uri){
        //Export the cellar to a CSV file

        //Header string
//        String csvHeader="Type;Appellation;Domaine;Cuvée;Bouteille;Capacité;Millésime;Commentaires sur la bouteille;Stock;Origine;Commentaires sur la mise en cave\n";
        String csvHeader=context.getResources().getString(R.string.wine_type)+CSV_SEPARATOR+
                context.getResources().getString(R.string.appellation)+CSV_SEPARATOR+
                context.getResources().getString(R.string.domain)+CSV_SEPARATOR+
                context.getResources().getString(R.string.cuvee)+CSV_SEPARATOR+
                context.getResources().getString(R.string.bottle)+CSV_SEPARATOR+
                context.getResources().getString(R.string.capacity)+CSV_SEPARATOR+
                context.getResources().getString(R.string.vintage)+CSV_SEPARATOR+
                context.getResources().getString(R.string.wine_comment)+CSV_SEPARATOR+
                context.getResources().getString(R.string.stock)+CSV_SEPARATOR+
                context.getResources().getString(R.string.origin)+CSV_SEPARATOR+
                context.getResources().getString(R.string.cellar_comment)+"\n";

        OutputStream outputStream;
        try {
            outputStream=context.getContentResolver().openOutputStream(uri);
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            bw.write(csvHeader);
            for (int i=0;i<Cellar.getCellarPool().get(MainActivity.getCurrentCellarIndex()).getCellList().size();i++){
                bw.append(cellToCsvLine(context,Cellar.getCellarPool().get(MainActivity.getCurrentCellarIndex()).getCellList().get(i))+"\n");
            }
            bw.flush();
            bw.close();
            Toast.makeText(context,"CSV created",Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            Toast.makeText(context,"Failed export to CSV",Toast.LENGTH_SHORT).show();
        }
    }

    private static void createDataBase(Context context,JsonObject jsonDataBase){
        //create database from readfile JsonObject

        clearDataBase();

        JsonObject currentJsonObject=new JsonObject();

        //Create bottles
        ArrayList<Object> bottleJsonObjectArrayList=jsonDataBase.getKeyValueArray(JSON_BOTTLES);

        String bottleName="";
        String bottleCapacity;
        for (int i=0;i<bottleJsonObjectArrayList.size();i++){
            currentJsonObject=(JsonObject)bottleJsonObjectArrayList.get(i);
            bottleCapacity=currentJsonObject.getKeyValue(JSON_CAPACITY);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_melchior))==0) bottleName=context.getResources().getString(R.string.bottle_melchior);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_nebuchadnezzar))==0) bottleName=context.getResources().getString(R.string.bottle_nebuchadnezzar);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_balthazar))==0) bottleName=context.getResources().getString(R.string.bottle_balthazar);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_salmanazar))==0) bottleName=context.getResources().getString(R.string.bottle_salmanazar);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_methuselah))==0) bottleName=context.getResources().getString(R.string.bottle_methuselah);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_jeroboam))==0) bottleName=context.getResources().getString(R.string.bottle_jeroboam);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_rehoboam))==0) bottleName=context.getResources().getString(R.string.bottle_rehoboam);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_marie_jeanne))==0) bottleName=context.getResources().getString(R.string.bottle_marie_jeanne);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_magnum))==0) bottleName=context.getResources().getString(R.string.bottle_magnum);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_pot))==0) bottleName=context.getResources().getString(R.string.bottle_pot);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_demi))==0) bottleName=context.getResources().getString(R.string.bottle_demi);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_quarter))==0) bottleName=context.getResources().getString(R.string.bottle_quarter);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_piccolo))==0) bottleName=context.getResources().getString(R.string.bottle_piccolo);
            if (bottleCapacity.compareTo(context.getResources().getString(R.string.capacity_standard))==0) bottleName=context.getResources().getString(R.string.bottle_standard);

            Bottle bottle=new Bottle(currentJsonObject.getKeyValue(JSON_APPELLATION),
                    currentJsonObject.getKeyValue(JSON_DOMAIN),
                    currentJsonObject.getKeyValue(JSON_CUVEE),
                    currentJsonObject.getKeyValue(JSON_TYPE),
                    currentJsonObject.getKeyValue(JSON_VINTAGE),
                    bottleName,
                    Float.parseFloat(bottleCapacity),
                    currentJsonObject.getKeyValue(JSON_BOTTLE_COMMENT),
                    currentJsonObject.getKeyValue(JSON_BOTTLE_PHOTO_NAME),
                    true);
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

        String timeStamp=new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        saveBitmapToInternalStorage(destination,folderName,timeStamp,bitmap);
        return timeStamp;
    }

    public static void saveBitmapToInternalStorage(File destination,String folderName,String photoName,Bitmap bitmap){
        //save any bitmap to any location

        File file=createOrGetFile(destination,folderName,photoName);

        try {
            file.getParentFile().mkdirs();
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

            //check orientation
            ExifInterface exifInterface=new ExifInterface(file.toString());
            if(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
                bitmap= CellarPictureUtils.rotate(bitmap, 90);
            } else if(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
                bitmap= CellarPictureUtils.rotate(bitmap, 270);
            } else if(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
                bitmap= CellarPictureUtils.rotate(bitmap, 180);
            }

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
            stream.close();

            //check orientation
            stream=context.getContentResolver().openInputStream(uri);
            androidx.exifinterface.media.ExifInterface exifInterface=new androidx.exifinterface.media.ExifInterface(stream);
            stream.close();

            if(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
                bitmap= CellarPictureUtils.rotate(bitmap, 90);
            } else if(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
                bitmap= CellarPictureUtils.rotate(bitmap, 270);
            } else if(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
                bitmap= CellarPictureUtils.rotate(bitmap, 180);
            }


        }catch (IOException e){
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

//    **********************************************************************************************
//    Zip write methods
//    **********************************************************************************************
    /*
     *
     * Zips a file at a location and places the resulting zip file at the toLocation
     * the toLocation is a URI, got by intent ACTION_CREATE_DOCUMENT
     */

    public static boolean zipFileAtPath(Context context,String sourcePath, Uri toLocation) {
        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        OutputStream dest;
        try {
            BufferedInputStream origin = null;
            dest = context.getContentResolver().openOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                entry.setTime(sourceFile.lastModified()); // to keep modification time after unzipping
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     *
     * Zips a subfolder
     *
     */

    private static void zipSubFolder(ZipOutputStream out, File folder,
                              int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                entry.setTime(file.lastModified()); // to keep modification time after unzipping
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    /*
     * gets the last path component
     *
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     */
    public static String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        if (segments.length == 0)
            return "";
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

//    **********************************************************************************************
//    Zip read methods
//    **********************************************************************************************

    public static boolean unpackZip(Context context,String destinationPath, Uri zipUri)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = context.getContentResolver().openInputStream(zipUri);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();
                Log.i(TAG,"Fichier lu : "+filename);

                // Need to create directories if not exists, or
                // it will generate an Exception...
                File parentPath=new File(destinationPath+filename).getParentFile();
                parentPath.mkdirs();

                Log.i(TAG,"Création du fichier : "+new File(destinationPath+filename).getPath());
                FileOutputStream fout = new FileOutputStream(new File(destinationPath+filename).getPath());

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Log.i(TAG,"Extraction ratée");
            return false;
        }

        Log.i(TAG,"Extraction réussie");
        return true;
    }

}
