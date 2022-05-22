package com.nathaniel.motus.cavevin.controller

import com.nathaniel.motus.cavevin.R
import android.content.*
import android.widget.Toast
import kotlin.Throws
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.nathaniel.motus.cavevin.model.*
import java.io.*
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object CellarStorageUtils {
    //Utilitary class meant to convert cellars, cells and bottles to string
    //to convert string to cellars, cells or bottles
    //save to and get from text files these strings
    //**********************************************************************************************
    //debug constants
    private const val TAG = "CellarStorageUtils"

    //**********************************************************************************************
    private const val SEPARATOR = "|"
    private const val CSV_SEPARATOR = ";"

    //    JSON for bottle
    private const val JSON_BOTTLES = "bottles"
    private const val JSON_APPELLATION = "appellation"
    private const val JSON_DOMAIN = "domain"
    private const val JSON_CUVEE = "cuvee"
    private const val JSON_VINTAGE = "vintage"
    private const val JSON_BOTTLE_NAME = "bottle name"
    private const val JSON_CAPACITY = "capacity"
    private const val JSON_TYPE = "type"
    private const val JSON_BOTTLE_COMMENT = "bottle comment"
    private const val JSON_BOTTLE_PHOTO_NAME = "photo name"

    //    JSON for cell
    private const val JSON_CELLS = "cells"
    private const val JSON_BOTTLE_INDEX = "bottle index"
    private const val JSON_ORIGIN = "origin"
    private const val JSON_STOCK = "stock"
    private const val JSON_CELL_COMMENT = "cell comment"

    //    JSON for cellar
    private const val JSON_CELLARS = "cellars"
    private const val JSON_CELL_INDEX = "cell index"
    private const val JSON_NAME = "name"

    //**********************************************************************************************
    //Converters
    //**********************************************************************************************
    fun bottleToString(bottle: Bottle?): String {
        //Convert a bottle into a string line to be written in a text file
        //ERROR : TO BE REDONE IF NEED BE
        return "ERROR : SUB NEEDS TO BE REDONE"
    }

    fun bottleToJsonObject(bottle: Bottle?): JsonObject {
//        convert a bottle to a JSON object
        val jsonObject = JsonObject()
        if (bottle != null) {
            jsonObject.addKeyValue(JSON_APPELLATION, bottle.appellation)
            jsonObject.addKeyValue(JSON_DOMAIN, bottle.domain)
            jsonObject.addKeyValue(JSON_CUVEE, bottle.cuvee)
            jsonObject.addKeyValue(JSON_TYPE, bottle.type)
            jsonObject.addKeyValue(JSON_VINTAGE, bottle.vintage)
            jsonObject.addKeyValue(JSON_BOTTLE_NAME, bottle.bottleName)
            jsonObject.addKeyValue(JSON_CAPACITY, java.lang.Double.toString(bottle.capacity))
            jsonObject.addKeyValue(JSON_BOTTLE_COMMENT, bottle.bottleComment)
            jsonObject.addKeyValue(JSON_BOTTLE_PHOTO_NAME, bottle.photoName)
        }
        return jsonObject
    }

    fun cellToString(cell: Cell?): String {
        //converts a cell into a string line to be written in a text file
        //bottle is defined by its index
        return if (cell != null) {
            var currentString = ""
            currentString = currentString + Bottle.bottleCatalog
                .indexOf(cell.bottle) + SEPARATOR
            currentString = currentString + cell.origin + SEPARATOR
            currentString = currentString + cell.stock + SEPARATOR
            currentString = currentString + cell.cellComment + SEPARATOR
            currentString
        } else "null"
    }

    fun cellToJsonObject(cell: Cell?): JsonObject {
        //convert a cell into a JsonObject
        //bottle is define by its index
        return if (cell != null) {
            val jsonObject = JsonObject()
            jsonObject.addKeyValue(
                JSON_BOTTLE_INDEX,
                Integer.toString(Bottle.bottleCatalog.indexOf(cell.bottle))
            )
            jsonObject.addKeyValue(JSON_ORIGIN, cell.origin)
            jsonObject.addKeyValue(
                JSON_STOCK,
                Integer.toString(cell.stock)
            )
            jsonObject.addKeyValue(JSON_CELL_COMMENT, cell.cellComment)
            jsonObject
        } else JsonObject()
    }

    fun cellarToString(cellar: Cellar?): String {
        //Converts a cellar into a text line to be written in a text file
        //it starts with the name of the cellar, then the refs of the cells
        return if (cellar != null) {
            var cellarString = cellar.cellarName + SEPARATOR
            for (i in cellar.cellList.indices) cellarString =
                cellarString + Cell.cellPool
                    .indexOf(cellar.cellList[i]) + SEPARATOR
            cellarString
        } else "null"
    }

    fun cellarToJsonObject(cellar: Cellar): JsonObject {
        //convert a cellar into a JsonObject
        //it starts with the name of the cellar, then the indexes of the cells
        val jsonObject = JsonObject()
        jsonObject.addKeyValue(JSON_NAME, cellar.cellarName)
        val objectArrayList = ArrayList<Any>()
        for (i in cellar.cellList.indices) {
            objectArrayList.add(Cell.cellPool.indexOf(cellar.cellList[i]))
        }
        jsonObject.addKeyValue(JSON_CELLS, objectArrayList)
        return jsonObject
    }

    fun bottleCatalogToJsonObject(): JsonObject {
        //change the bottle catalog into a JsonObject
        val jsonObject = JsonObject()
        val objectArrayList = ArrayList<Any>()
        for (i in Bottle.bottleCatalog.indices) {
            objectArrayList.add(bottleToJsonObject(Bottle.bottleCatalog[i]))
        }
        jsonObject.addKeyValue(JSON_BOTTLES, objectArrayList)
        return jsonObject
    }

    fun cellPoolToJsonObject(): JsonObject {
        //change the cell pool into a JsonObject
        val jsonObject = JsonObject()
        val objectArrayList = ArrayList<Any>()
        for (i in Cell.cellPool.indices) {
            objectArrayList.add(cellToJsonObject(Cell.cellPool[i]))
        }
        jsonObject.addKeyValue(JSON_CELLS, objectArrayList)
        return jsonObject
    }

    fun cellarPoolToJsonObject(): JsonObject {
        //change the cellar pool into a JsonObject
        val jsonObject = JsonObject()
        val jsonObjectArrayList = ArrayList<Any>()
        for (i in Cellar.cellarPool.indices) {
            jsonObjectArrayList.add(cellarToJsonObject(Cellar.cellarPool[i]))
        }
        jsonObject.addKeyValue(JSON_CELLARS, jsonObjectArrayList)
        return jsonObject
    }

    fun dataBaseToJsonObject(): JsonObject {
        //change the whole data base into a JsonObject
        val jsonObject = JsonObject()
        jsonObject.addJsonObject(bottleCatalogToJsonObject())
        jsonObject.addJsonObject(cellPoolToJsonObject())
        jsonObject.addJsonObject(cellarPoolToJsonObject())
        return jsonObject
    }

    fun cellToCsvLine(context: Context, cell: Cell): String {
        //Convert a cell to a string, meant to be added to a CSV file
        var csvString = ""
        val bottle = cell.bottle
        val type: String
        type = when (bottle.type) {
            "0" -> context.getString(R.string.wine_red)
            "1" -> context.getString(R.string.wine_white)
            else -> context.getString(R.string.wine_pink)
        }
        csvString = csvString + type + CSV_SEPARATOR
        csvString = csvString + bottle.appellation + CSV_SEPARATOR
        csvString = csvString + bottle.domain + CSV_SEPARATOR
        csvString = csvString + bottle.cuvee + CSV_SEPARATOR
        csvString = csvString + bottle.bottleName + CSV_SEPARATOR
        csvString = csvString + bottle.capacity + CSV_SEPARATOR
        csvString = csvString + bottle.vintage + CSV_SEPARATOR
        csvString = csvString + bottle.bottleComment + CSV_SEPARATOR
        csvString = csvString + cell.stock + CSV_SEPARATOR
        csvString = csvString + cell.origin + CSV_SEPARATOR
        csvString = csvString + cell.cellComment + CSV_SEPARATOR
        return csvString
    }

    //**********************************************************************************************
    //I/O methods
    //**********************************************************************************************
    fun createOrGetFile(destination: File?, folderName: String?, fileName: String?): File {
        val folder = File(destination, folderName)
        return File(folder, fileName)
    }

    fun deleteRecursive(fileOrFolder: File) {
        //delete folders and files within
        if (fileOrFolder.isDirectory) {
            for (child in fileOrFolder.listFiles()) {
                deleteRecursive(child)
            }
        }
        fileOrFolder.delete()
    }

    fun saveDataBase(file: File?) {
        //save the whole database
        try {
            file!!.parentFile.mkdirs()
            val fileWriter = FileWriter(file)
            val w: Writer = BufferedWriter(fileWriter)
            try {
                w.write(dataBaseToJsonObject().jsonToString())
                Log.i(TAG, dataBaseToJsonObject().jsonToString())
            } finally {
                w.close()
                Log.i(TAG, "Database saved")
            }
        } catch (e: IOException) {
            Log.i(TAG, "Failed saving database")
        }
    }

    fun loadDataBase(context: Context, file: File?) {
        //load the whole database
        var dataBaseString = ""
        if (file!!.exists()) {
            val bufferedReader: BufferedReader
            try {
                bufferedReader = BufferedReader(FileReader(file))
                try {
                    var readString = bufferedReader.readLine()
                    while (readString != null) {
                        dataBaseString = dataBaseString + readString
                        Log.i(TAG, dataBaseString)
                        //next line
                        readString = bufferedReader.readLine()
                    }
                } finally {
                    bufferedReader.close()
                    Log.i(TAG, "Database loaded")
                }
            } catch (e: IOException) {
                Log.i(TAG, "Failed load database")
            }
        }
        if (dataBaseString !== "") {
            val jsonDatabase = JsonObject.stringToJsonObject(dataBaseString)
            createDataBase(context, jsonDatabase)
        }
    }

    fun exportCellarToCsvFile(context: Context, uri: Uri?) {
        //Export the cellar to a CSV file

        //Header string
//        String csvHeader="Type;Appellation;Domaine;Cuvée;Bouteille;Capacité;Millésime;Commentaires sur la bouteille;Stock;Origine;Commentaires sur la mise en cave\n";
        val csvHeader = """
               ${context.resources.getString(R.string.wine_type)}$CSV_SEPARATOR${
            context.resources.getString(
                R.string.appellation
            )
        }$CSV_SEPARATOR${context.resources.getString(R.string.domain)}$CSV_SEPARATOR${
            context.resources.getString(
                R.string.cuvee
            )
        }$CSV_SEPARATOR${context.resources.getString(R.string.bottle)}$CSV_SEPARATOR${
            context.resources.getString(
                R.string.capacity
            )
        }$CSV_SEPARATOR${context.resources.getString(R.string.vintage)}$CSV_SEPARATOR${
            context.resources.getString(
                R.string.wine_comment
            )
        }$CSV_SEPARATOR${context.resources.getString(R.string.stock)}$CSV_SEPARATOR${
            context.resources.getString(
                R.string.origin
            )
        }$CSV_SEPARATOR${context.resources.getString(R.string.cellar_comment)}
               
               """.trimIndent()
        val outputStream: OutputStream?
        try {
            outputStream = context.contentResolver.openOutputStream(uri!!)
            val bw = BufferedWriter(OutputStreamWriter(outputStream, StandardCharsets.UTF_8))
            bw.write(csvHeader)
            for (i in Cellar.cellarPool[MainActivity.Companion.currentCellarIndex].cellList.indices) {
                bw.append(
                    """
    ${
                        cellToCsvLine(
                            context,
                            Cellar.cellarPool[MainActivity.Companion.currentCellarIndex].cellList[i]!!
                        )
                    }
    
    """.trimIndent()
                )
            }
            bw.flush()
            bw.close()
            Toast.makeText(context, "CSV created", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(context, "Failed export to CSV", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createDataBase(context: Context, jsonDataBase: JsonObject) {
        //create database from readfile JsonObject
        clearDataBase()
        var currentJsonObject = JsonObject()

        //Create bottles
        val bottleJsonObjectArrayList = jsonDataBase.getKeyValueArray(JSON_BOTTLES)
        var bottleName = ""
        var bottleCapacity: String
        for (i in bottleJsonObjectArrayList.indices) {
            currentJsonObject = bottleJsonObjectArrayList[i] as JsonObject
            bottleCapacity = currentJsonObject.getKeyValue(JSON_CAPACITY)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_melchior)) == 0) bottleName =
                context.resources.getString(R.string.bottle_melchior)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_nebuchadnezzar)) == 0) bottleName =
                context.resources.getString(R.string.bottle_nebuchadnezzar)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_balthazar)) == 0) bottleName =
                context.resources.getString(R.string.bottle_balthazar)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_salmanazar)) == 0) bottleName =
                context.resources.getString(R.string.bottle_salmanazar)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_methuselah)) == 0) bottleName =
                context.resources.getString(R.string.bottle_methuselah)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_jeroboam)) == 0) bottleName =
                context.resources.getString(R.string.bottle_jeroboam)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_rehoboam)) == 0) bottleName =
                context.resources.getString(R.string.bottle_rehoboam)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_marie_jeanne)) == 0) bottleName =
                context.resources.getString(R.string.bottle_marie_jeanne)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_magnum)) == 0) bottleName =
                context.resources.getString(R.string.bottle_magnum)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_pot)) == 0) bottleName =
                context.resources.getString(R.string.bottle_pot)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_demi)) == 0) bottleName =
                context.resources.getString(R.string.bottle_demi)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_quarter)) == 0) bottleName =
                context.resources.getString(R.string.bottle_quarter)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_piccolo)) == 0) bottleName =
                context.resources.getString(R.string.bottle_piccolo)
            if (bottleCapacity.compareTo(context.resources.getString(R.string.capacity_standard)) == 0) bottleName =
                context.resources.getString(R.string.bottle_standard)
            val bottle = Bottle(
                currentJsonObject.getKeyValue(JSON_APPELLATION),
                currentJsonObject.getKeyValue(JSON_DOMAIN),
                currentJsonObject.getKeyValue(JSON_CUVEE),
                currentJsonObject.getKeyValue(JSON_TYPE),
                currentJsonObject.getKeyValue(JSON_VINTAGE),
                bottleName, bottleCapacity.toDouble(),
                currentJsonObject.getKeyValue(JSON_BOTTLE_COMMENT),
                currentJsonObject.getKeyValue(JSON_BOTTLE_PHOTO_NAME),
                true
            )
        }

        //Create cells
        val cellJsonObjectArrayList = jsonDataBase.getKeyValueArray(JSON_CELLS)
        for (i in cellJsonObjectArrayList.indices) {
            currentJsonObject = cellJsonObjectArrayList[i] as JsonObject
            val cell = Cell(
                Bottle.bottleCatalog[currentJsonObject.getKeyValue(JSON_BOTTLE_INDEX)
                    .toInt()],
                currentJsonObject.getKeyValue(JSON_ORIGIN),
                currentJsonObject.getKeyValue(
                    JSON_STOCK
                ).toInt(),
                currentJsonObject.getKeyValue(JSON_CELL_COMMENT),
                true
            )
        }

        //Create cellars
        val cellarJsonObjectArrayList = jsonDataBase.getKeyValueArray(JSON_CELLARS)
        for (i in cellarJsonObjectArrayList.indices) {
            currentJsonObject = cellarJsonObjectArrayList[i] as JsonObject
            val cellObjectArrayList = currentJsonObject.getKeyValueArray(JSON_CELLS)
            val cellArrayList = ArrayList<Cell>()
            for (j in cellObjectArrayList.indices) {
                cellArrayList.add(Cell.cellPool[(cellObjectArrayList[j] as Int)])
            }
            val cellar = Cellar(currentJsonObject.getKeyValue(JSON_NAME), cellArrayList, true)
        }
    }

    fun clearDataBase() {
        //empty database
        Cellar.clearCellarPool()
        Cell.clearCellPool()
        Bottle.clearBottleCatalog()
    }

    fun saveBottleImageToInternalStorage(
        destination: File?,
        folderName: String?,
        bitmap: Bitmap?
    ): String {
        //save the photo to the internal storage and return the pathname
        //the photo name is a timestamp
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
        saveBitmapToInternalStorage(destination, folderName, timeStamp, bitmap)
        return timeStamp
    }

    fun saveBitmapToInternalStorage(
        destination: File?,
        folderName: String?,
        photoName: String?,
        bitmap: Bitmap?
    ) {
        //save any bitmap to any location
        val file = createOrGetFile(destination, folderName, photoName)
        try {
            file.parentFile.mkdirs()
            val outputStream: OutputStream = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun getBitmapFromInternalStorage(
        destination: File?,
        folderName: String?,
        fileName: String?
    ): Bitmap? {
        //get the photo in internal storage
        val file = createOrGetFile(destination, folderName, fileName)
        var bitmap: Bitmap? = null
        try {
            file.parentFile.mkdirs()
            val stream: InputStream = FileInputStream(file)
            bitmap = BitmapFactory.decodeStream(stream)
            stream.close()

            //check orientation
            val exifInterface = ExifInterface(file.toString())
            if (exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equals("6", ignoreCase = true)
            ) {
                bitmap = CellarPictureUtils.rotate(bitmap, 90)
            } else if (exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equals("8", ignoreCase = true)
            ) {
                bitmap = CellarPictureUtils.rotate(bitmap, 270)
            } else if (exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equals("3", ignoreCase = true)
            ) {
                bitmap = CellarPictureUtils.rotate(bitmap, 180)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun getBitmapFromUri(context: Context, uri: Uri?): Bitmap? {
        //get a bitmap from an Uri
        var bitmap: Bitmap? = null
        try {
            var stream = context.contentResolver.openInputStream(uri!!)
            bitmap = BitmapFactory.decodeStream(stream)
            stream!!.close()

            //check orientation
            stream = context.contentResolver.openInputStream(uri)
            val exifInterface = androidx.exifinterface.media.ExifInterface(
                stream!!
            )
            stream.close()
            if (exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equals("6", ignoreCase = true)
            ) {
                bitmap = CellarPictureUtils.rotate(bitmap, 90)
            } else if (exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equals("8", ignoreCase = true)
            ) {
                bitmap = CellarPictureUtils.rotate(bitmap, 270)
            } else if (exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equals("3", ignoreCase = true)
            ) {
                bitmap = CellarPictureUtils.rotate(bitmap, 180)
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Erreur", Toast.LENGTH_SHORT).show()
        }
        return bitmap
    }

    fun decodeSampledBitmapFromFile(
        destination: File?,
        folderName: String?,
        fileName: String?,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        val file = createOrGetFile(destination, folderName, fileName)
        var bitmap: Bitmap? = null

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        try {
            file.parentFile.mkdirs()
            options.inJustDecodeBounds = true
            val stream: InputStream = FileInputStream(file)
            bitmap = BitmapFactory.decodeStream(stream, null, options)
            stream.close()

            // Calculate inSampleSize
            options.inSampleSize =
                CellarPictureUtils.calculateInSampleSize(options, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            val stream1: InputStream = FileInputStream(file)
            options.inJustDecodeBounds = false
            bitmap = BitmapFactory.decodeStream(stream1, null, options)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            bitmap = null
        }
        return bitmap
    }

    fun deleteFileFromInternalStorage(destination: File?, folderName: String?, fileName: String?) {
        //delete a file frome internal storage
        //use to delete bottle photos
        val file = createOrGetFile(destination, folderName, fileName)
        val deleted = file.delete()
    }

    //    **********************************************************************************************
    //    Zip write methods
    //    **********************************************************************************************
    /*
     *
     * Zips a file at a location and places the resulting zip file at the toLocation
     * the toLocation is a URI, got by intent ACTION_CREATE_DOCUMENT
     */
    fun zipFileAtPath(context: Context, sourcePath: String, toLocation: Uri?): Boolean {
        val BUFFER = 2048
        val sourceFile = File(sourcePath)
        val dest: OutputStream?
        try {
            var origin: BufferedInputStream? = null
            dest = context.contentResolver.openOutputStream(toLocation!!)
            val out = ZipOutputStream(
                BufferedOutputStream(
                    dest
                )
            )
            if (sourceFile.isDirectory) {
                zipSubFolder(out, sourceFile, sourceFile.parent.length)
            } else {
                val data = ByteArray(BUFFER)
                val fi = FileInputStream(sourcePath)
                origin = BufferedInputStream(fi, BUFFER)
                val entry = ZipEntry(getLastPathComponent(sourcePath))
                entry.time = sourceFile.lastModified() // to keep modification time after unzipping
                out.putNextEntry(entry)
                var count: Int
                while (origin.read(data, 0, BUFFER).also { count = it } != -1) {
                    out.write(data, 0, count)
                }
            }
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /*
     *
     * Zips a subfolder
     *
     */
    @Throws(IOException::class)
    private fun zipSubFolder(
        out: ZipOutputStream, folder: File,
        basePathLength: Int
    ) {
        val BUFFER = 2048
        val fileList = folder.listFiles()
        var origin: BufferedInputStream? = null
        for (file in fileList) {
            if (file.isDirectory) {
                zipSubFolder(out, file, basePathLength)
            } else {
                val data = ByteArray(BUFFER)
                val unmodifiedFilePath = file.path
                val relativePath = unmodifiedFilePath
                    .substring(basePathLength)
                val fi = FileInputStream(unmodifiedFilePath)
                origin = BufferedInputStream(fi, BUFFER)
                val entry = ZipEntry(relativePath)
                entry.time = file.lastModified() // to keep modification time after unzipping
                out.putNextEntry(entry)
                var count: Int
                while (origin.read(data, 0, BUFFER).also { count = it } != -1) {
                    out.write(data, 0, count)
                }
                origin.close()
            }
        }
    }

    /*
     * gets the last path component
     *
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     */
    fun getLastPathComponent(filePath: String): String {
        val segments = filePath.split("/").toTypedArray()
        return if (segments.size == 0) "" else segments[segments.size - 1]
    }

    //    **********************************************************************************************
    //    Zip read methods
    //    **********************************************************************************************
    fun unpackZip(context: Context, destinationPath: String, zipUri: Uri?): Boolean {
        val `is`: InputStream?
        val zis: ZipInputStream
        try {
            var filename: String
            `is` = context.contentResolver.openInputStream(zipUri!!)
            zis = ZipInputStream(BufferedInputStream(`is`))
            var ze: ZipEntry
            val buffer = ByteArray(1024)
            var count: Int
            while (zis.nextEntry.also { ze = it } != null) {
                filename = ze.name
                Log.i(TAG, "Fichier lu : $filename")

                // Need to create directories if not exists, or
                // it will generate an Exception...
                val parentPath = File(destinationPath + filename).parentFile
                parentPath.mkdirs()
                Log.i(TAG, "Création du fichier : " + File(destinationPath + filename).path)
                val fout = FileOutputStream(File(destinationPath + filename).path)
                while (zis.read(buffer).also { count = it } != -1) {
                    fout.write(buffer, 0, count)
                }
                fout.close()
                zis.closeEntry()
            }
            zis.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i(TAG, "Extraction ratée")
            return false
        }
        Log.i(TAG, "Extraction réussie")
        return true
    }
}