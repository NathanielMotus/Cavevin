package com.nathaniel.motus.cavevin.controller

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nathaniel.motus.cavevin.R
import androidx.annotation.RequiresApi
import android.os.Build
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.coroutineScope
import com.nathaniel.motus.cavevin.databinding.ActivityMainBinding
import com.nathaniel.motus.cavevin.model.*
import com.nathaniel.motus.cavevin.ui.bottle_detail.BottleDetailScreen
import com.nathaniel.motus.cavevin.ui.bottle_list.BottleListScreen
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModelFactory
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModelFactory
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val bottleListViewModel: BottleListViewModel by viewModels() {
        BottleListViewModelFactory(this.application)
    }

    private val bottleDetailViewModel: BottleDetailViewModel by viewModels() {
        BottleDetailViewModelFactory(this.application)
    }
    //Fragments declaration

    //todo : when loading database, ask for overwriting new bottle type names if a new release take into account language bottle type names
    //todo : add possibility to overwrite user entered bottle type names with built-in bottle type names at any moment


//    **********************************************************************************************
//    MainActivity events
//    **********************************************************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cleanUpDatabase()
        sharedPreferences
        if (Cellar.numberOfCellars > 0) Collections.sort(
            Cellar.cellarPool[currentCellarIndex].cellList,
            CellComparator
        )
    }

    override fun onDestroy() {
        //Save the datas when app is left
        super.onDestroy()
        saveDatas()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        checkPermissions()
        if (Cellar.numberOfCellars > 0) Collections.sort(
            Cellar.cellarPool[currentCellarIndex].cellList,
            CellComparator
        )
        //binding.composeView.setContent { BottleListScreen(viewModel = bottleListViewModel) }
        binding.composeView.setContent { BottleDetailScreen(viewModel = bottleDetailViewModel) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //Export database
        if (requestCode == REQUEST_URI_CREATE && resultCode == RESULT_OK) {
            val pathNameUri = data!!.data
            CellarStorageUtils.zipFileAtPath(
                applicationContext,
                File(filesDir, resources.getString(R.string.database_folder_name)).path,
                pathNameUri
            )
        }

        //Import database
        if (requestCode == REQUEST_URI_LOAD && resultCode == RESULT_OK) {
            val pathNameUri = data!!.data
            importDatabase(pathNameUri)
        }

        //Export CSV
        if (requestCode == REQUEST_CSV_URI_CREATE && resultCode == RESULT_OK) {
            val csvUri = data!!.data
            CellarStorageUtils.exportCellarToCsvFile(this, csvUri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults[WRITE_EXTERNAL_STORAGE_PERMISSION_INDEX] == PackageManager.PERMISSION_GRANTED) writeExternalStoragePermission =
                    true else writeExternalStoragePermission = false
                if (grantResults[CAMERA_PERMISSION_INDEX] == PackageManager.PERMISSION_GRANTED) cameraPermission =
                    true else cameraPermission = false
            }
            else -> {
            }
        }
    }

    //    **********************************************************************************************
//    Toolbar events
//    **********************************************************************************************
/*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
*/

    //    **********************************************************************************************
//    Navigation view events
//    **********************************************************************************************
    //    **********************************************************************************************
//    Callbacks
//    **********************************************************************************************

    //    **********************************************************************************************
//    Initialization subs
//    **********************************************************************************************

    //initialize shared preferences
//check current cellar index is relevant (in case of unexpected application fail)
    private val sharedPreferences: Unit
        private get() {
            //initialize shared preferences
            currentCellarIndex = getPreferences(MODE_PRIVATE).getInt(CURRENT_CELLAR_INDEX, 0)
            //check current cellar index is relevant (in case of unexpected application fail)
            if (currentCellarIndex >= Cellar.cellarPool.size) currentCellarIndex = 0
            currentTypeFilter =
                getPreferences(MODE_PRIVATE).getString(CURRENT_TYPE_FILTER, TYPE_FILTER_ALL)
            sSortOption = getPreferences(MODE_PRIVATE).getInt(CURRENT_SORT_OPTION, 0)
        }

    //    **********************************************************************************************
//    Working subs
//    **********************************************************************************************
    private fun loadDatas() {
        //load all the datas
        val saveDir = filesDir
        val saveURI = CellarStorageUtils.createOrGetFile(
            saveDir,
            resources.getString(R.string.database_folder_name),
            resources.getString(R.string.database_file_name)
        )
        CellarStorageUtils.loadDataBase(applicationContext, saveURI)
    }

    private fun saveDatas() {
        //save all the datas
        val saveDir = filesDir
        val saveFileName = CellarStorageUtils.createOrGetFile(
            saveDir,
            resources.getString(R.string.database_folder_name),
            resources.getString(R.string.database_file_name)
        )
        CellarStorageUtils.saveDataBase(saveFileName)
    }

    private fun importDatabase(pathNameUri: Uri?) {
        //import database from file chosen by user

        //delete older files
        CellarStorageUtils.deleteRecursive(
            File(
                filesDir,
                resources.getString(R.string.database_folder_name)
            )
        )
        CellarStorageUtils.unpackZip(
            applicationContext,
            filesDir.path,
            pathNameUri
        )
        loadDatas()
        currentCellarIndex = 0
    }

    private fun showStats() {
        //show stats
        val builder = AlertDialog.Builder(this)
        var message = """
            ${getString(R.string.main_activity_global_stats)}
            
            
            """.trimIndent()
        message = """
            $message${Cellar.numberOfCellars}${getString(R.string.main_activity_cellars)}
            
            """.trimIndent()
        message = """
            $message${Bottle.numberOfReferences}${getString(R.string.main_activity_references)}
            
            """.trimIndent()
        message = """
            $message${Cellar.totalStock()}${getString(R.string.main_activity_stocked_bottles)}
            
            
            
            """.trimIndent()
        message = """$message${Cellar.cellarPool[currentCellarIndex].cellarName} :

"""
        message = """
            $message${Cellar.cellarPool[currentCellarIndex].cellList.size}${getString(R.string.main_activity_ref_in_stock)}
            
            """.trimIndent()
        message = """
            $message${Cellar.cellarPool[currentCellarIndex].stock}${getString(R.string.main_activity_stocked_bottles_among_them)}
            
            """.trimIndent()
        message = """
            $message${Cellar.cellarPool[currentCellarIndex].getStock(RED_WINE)}${getString(R.string.main_activity_stock_red)}
            
            """.trimIndent()
        message = """
            $message${Cellar.cellarPool[currentCellarIndex].getStock(WHITE_WINE)}${getString(R.string.main_activity_stock_white)}
            
            """.trimIndent()
        message = """
            $message${Cellar.cellarPool[currentCellarIndex].getStock(PINK_WINE)}${getString(R.string.main_activity_stock_pink)}
            
            """.trimIndent()
        builder.setTitle(R.string.main_activity_stats)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which -> }
            .create()
            .show()
    }

    private fun cleanUpDatabase() {
        //remove all the unused items from database
        //starting with cells, then bottles
        //this sub is meant to be used only in development
        for (i in Cell.numberOfCells - 1 downTo 0) {
            if (Cell.cellPool[i].findUseCaseCellar(0) == null) Cell.cellPool[i].removeCell()
        }
        for (i in Bottle.numberOfReferences - 1 downTo 0) {
            if (Bottle.bottleCatalog[i].findUseCaseCell(0) == null) Bottle.bottleCatalog[i].removeBottleFromCatalog()
        }
    }

    //    **********************************************************************************************
//    Send intents
//    **********************************************************************************************
    private fun launchEditCellarActivity(cellPosition: Int) {
        //launch the cellar editor
        //navController.navigate(R.id.action_cellarItemListFragment_to_bottleFragment)
        /*
        val intent = Intent(this@MainActivity, EditCellarActivity::class.java)
        intent.putExtra(BUNDLE_EXTRA_CURRENT_CELLAR_INDEX, currentCellarIndex)
        intent.putExtra(BUNDLE_EXTRA_CELL_POSITION, cellPosition)
        this.startActivity(intent)

         */
    }

    private fun sendExportDataBaseIntent() {
        //export the whole database to a file chosen by user
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.type = "*/*"
        startActivityForResult(intent, REQUEST_URI_CREATE)
    }

    private fun sendExportCsvIntent() {
        //export the whole database to a file chosen by user in CSV format
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.type = "text/*"
        startActivityForResult(intent, REQUEST_CSV_URI_CREATE)
    }

    private fun sendImportDataBaseIntent() {
        //import the whole database from file chose by user
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, REQUEST_URI_LOAD)
    }

    //    **********************************************************************************************
//    Check permissions
//    **********************************************************************************************
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionString = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            ) requestPermissions(permissionString, REQUEST_PERMISSION)
        }
    }

    companion object {
        //**********************************************************************************************
        //variables de test
        private const val TAG = "MainActivity"

        //**********************************************************************************************
        var BUNDLE_EXTRA_CURRENT_CELLAR_INDEX = "BUNDLE_EXTRA_CURRENT_CELLAR_INDEX"
        var BUNDLE_EXTRA_CELL_POSITION = "BUNDLE_EXTRA_CELL_POSITION"

        //    **********************************************************************************************
        //    Getters
        //    **********************************************************************************************
        var currentCellarIndex = 0
            private set
        private const val TYPE_FILTER_RED = "red"
        private const val TYPE_FILTER_WHITE = "white"
        private const val TYPE_FILTER_PINK = "pink"
        private const val TYPE_FILTER_ALL = "all"
        var currentTypeFilter: String? = TYPE_FILTER_ALL
            private set
        private var sSortOption = 0
        private const val REQUEST_URI_CREATE = 100
        private const val REQUEST_URI_LOAD = 101
        private const val REQUEST_PERMISSION = 102
        private const val REQUEST_CSV_URI_CREATE = 103
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_INDEX = 0
        private const val CAMERA_PERMISSION_INDEX = 1

        //Following values are used to handle callback
        private var sMenuTag //to indicate where the callback comes from
                : String? = null
        private const val MENU_ITEM_CREATE_CELLAR = "CREATE CELLAR"
        private const val MENU_ITEM_RENAME_CELLAR = "RENAME CELLAR"
        private const val MENU_ITEM_CHOOSE_CELLAR = "CHOOSE CELLAR"
        private const val MENU_ITEM_DELETE_CELLAR = "DELETE CELLAR"

        // Types of wine
        private const val RED_WINE = "0"
        private const val WHITE_WINE = "1"
        private const val PINK_WINE = "2"

        //Shared preferences tags
        private const val CURRENT_CELLAR_INDEX = "Current cellar index"
        private const val CURRENT_TYPE_FILTER = "Current type filter"
        private const val CURRENT_SORT_OPTION = "Current sort options"

        //Permissions status
        var writeExternalStoragePermission = true
            private set
        var cameraPermission = true
            private set
    }
}