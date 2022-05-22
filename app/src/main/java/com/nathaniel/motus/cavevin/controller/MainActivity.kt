package com.nathaniel.motus.cavevin.controller

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.nathaniel.motus.cavevin.view.MyRecyclerViewAdapter.onItemClickedListener
import com.nathaniel.motus.cavevin.view.BottomBarFragment.onBottomBarClickedListener
import com.google.android.material.navigation.NavigationView
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment.onCellarChoiceFragmentClickListener
import com.nathaniel.motus.cavevin.view.EditDialogFragment.onEditDialogClickListener
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.TextView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.os.Bundle
import com.nathaniel.motus.cavevin.R
import androidx.annotation.RequiresApi
import android.os.Build
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import android.net.Uri
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.coroutineScope
import com.nathaniel.motus.cavevin.model.*
import com.nathaniel.motus.cavevin.view.*
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel
import com.nathaniel.motus.cavevin.viewmodels.CellarViewModelFactory
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity(), onItemClickedListener, onBottomBarClickedListener,
    NavigationView.OnNavigationItemSelectedListener, onCellarChoiceFragmentClickListener,
    onEditDialogClickListener {
    //Fragments declaration
    private var cellarFragment: CellarFragment? = null
    private var cellarItemListFragment:CellarItemListFragment?=null
    private var bottomBarFragment: BottomBarFragment? = null

    //Views declaration
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var headerTitle: TextView
    private lateinit var navigationView: NavigationView
    private lateinit var sortOption0: RadioButton
    private lateinit var sortOption1: RadioButton
    private lateinit var sortOption2: RadioButton
    private lateinit var sortGroup: RadioGroup

    private val bottleListViewModel: BottleListViewModel by viewModels {
        CellarViewModelFactory(
            this.application
        )
    }
    //todo : when loading database, ask for overwriting new bottle type names if a new release take into account language bottle type names
    //todo : add possibility to overwrite user entered bottle type names with built-in bottle type names at any moment

    //test function


    //    **********************************************************************************************
//    MainActivity events
//    **********************************************************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get and configure the views
        //loadDatas()
        //if (Cellar.numberOfCellars == 0) prepareFirstUse()
        cleanUpDatabase()
        sharedPreferences
        configureToolBar()
        configureDrawerLayout()
        configureNavigationView()
        configureBottomBar()
        configureSortOptions()
        if (Cellar.numberOfCellars > 0) Collections.sort(
            Cellar.cellarPool[currentCellarIndex].cellList,
            CellComparator
        )
        setDrawerCellarTitle()
        configureAndShowCellarFragment()
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_activity_main_add_entry -> {
                //                create a new entry
                launchEditCellarActivity(-1)
                true
            }
            R.id.menu_activity_main_delete_cellar -> {
                //                delete a chosen cellar
                if (Cellar.cellarPool.size > 1) {
                    showCellarChoiceDialogFragment(
                        getString(R.string.main_activity_delete_cellar),
                        MENU_ITEM_DELETE_CELLAR
                    )
                } else Toast.makeText(
                    this,
                    R.string.main_activity_at_least_one_cellar,
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
            R.id.menu_activity_main_export -> {
                if (writeExternalStoragePermission) sendExportDataBaseIntent()
                true
            }
            R.id.menu_activity_main_import -> {
                sendImportDataBaseIntent()
                true
            }
            R.id.menu_activity_main_csv_export -> {
                sendExportCsvIntent()
                true
            }
            R.id.menu_activity_main_stats -> {
                showStats()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //    **********************************************************************************************
//    Navigation view events
//    **********************************************************************************************
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        //manage navigation view menu clicks
        val id = menuItem.itemId
        when (id) {
            R.id.activity_main_drawer_choose_cellar -> showCellarChoiceDialogFragment(
                getString(R.string.main_activity_choose_cellar),
                MENU_ITEM_CHOOSE_CELLAR
            )
            R.id.activity_main_drawer_create_cellar -> showEditDialogFragment(
                getString(R.string.main_activity_new_cellar),
                getString(R.string.main_activity_cellar_name),
                "",
                MENU_ITEM_CREATE_CELLAR
            )
            R.id.activity_main_drawer_rename_cellar -> showEditDialogFragment(
                getString(R.string.main_activity_rename_cellar),
                getString(R.string.main_activity_new_cellar_name),
                Cellar.cellarPool[currentCellarIndex].cellarName,
                MENU_ITEM_RENAME_CELLAR
            )
            else -> {
            }
        }
        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    //    **********************************************************************************************
//    Callbacks
//    **********************************************************************************************
    override fun onItemClicked(view: View?, position: Int) {

        //Case edit button clicked
        if (view != null) {
            if (view.id == R.id.recycler_cellar_row_edit_image) launchEditCellarActivity(
                position
            )
        }

        //Case photo clicked
        if (view != null) {
            if (view.id == R.id.recycler_cellar_row_photo_image) {
                val photoName =
                    Cellar.cellarPool[currentCellarIndex].cellList[position].bottle.photoName
                if (photoName.compareTo("") != 0) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(
                        CellarPictureUtils.getUriFromFileProvider(
                            applicationContext,
                            photoName
                        ), "image/*"
                    )
                    startActivity(intent)
                }
            }
        }
    }

    override fun onBottomBarItemClicked(v: View?, buttonTag: String?) {
        when (buttonTag) {
            "red" -> currentTypeFilter = TYPE_FILTER_RED
            "white" -> currentTypeFilter = TYPE_FILTER_WHITE
            "pink" -> currentTypeFilter = TYPE_FILTER_PINK
            else -> currentTypeFilter = TYPE_FILTER_ALL
        }
        val preferences = getPreferences(MODE_PRIVATE)
        preferences.edit().putString(CURRENT_TYPE_FILTER, currentTypeFilter).apply()
        cellarFragment!!.updateCellarRecyclerView(
            Cellar.cellarPool[currentCellarIndex].typeFiltered(
                currentTypeFilter
            )
        )
    }

    override fun onCellarChoiceFragmentClick(position: Int) {
//        coming back from CellarChoiceDialog
//        sMenuTag decides what to do
        when (sMenuTag) {
            MENU_ITEM_CHOOSE_CELLAR -> {
                //                set chosen cellar as new current cellar
                currentCellarIndex = position
                val preferences = getPreferences(MODE_PRIVATE)
                preferences.edit().putInt(CURRENT_CELLAR_INDEX, currentCellarIndex).apply()
            }
            MENU_ITEM_DELETE_CELLAR -> {
                //                delete selected cellar
                val cellar = Cellar.cellarPool[currentCellarIndex]
                Cellar.cellarPool[position].destroyCells()
                Cellar.cellarPool.removeAt(position)
                currentCellarIndex = Cellar.cellarPool.indexOf(cellar)
                if (currentCellarIndex == -1) currentCellarIndex = 0
            }
            else -> {
            }
        }
        cellarFragment!!.updateCellarRecyclerView(
            Cellar.cellarPool[currentCellarIndex].typeFiltered(
                currentTypeFilter
            )
        )
        setDrawerCellarTitle()
    }

    override fun onEditDialogClick(v: View?, inputText: String?) {
//        Coming back from EditDialog
//        sMenuTag decides what to do
        when (sMenuTag) {
            MENU_ITEM_CREATE_CELLAR -> {
                //                Create a new cellar and set it as current cellar
//                and update display
                val cellar = inputText?.let { Cellar(it, ArrayList(), true) }
                currentCellarIndex = Cellar.cellarPool.size - 1
                cellarFragment!!.updateCellarRecyclerView(
                    Cellar.cellarPool[currentCellarIndex].typeFiltered(
                        currentTypeFilter
                    )
                )
            }
            MENU_ITEM_RENAME_CELLAR -> //                rename the current cellar
                Cellar.cellarPool[currentCellarIndex].cellarName = inputText!!
            else -> {
            }
        }
        //        Done on every callback
        setDrawerCellarTitle()
    }

    //    **********************************************************************************************
//    Initialization subs
//    **********************************************************************************************
    private fun configureAndShowCellarFragment() {
/*        cellarFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_frame_layout) as CellarFragment?
        if (cellarFragment == null) {
            cellarFragment = CellarFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_frame_layout, cellarFragment!!).commit()
        }

 */
        cellarItemListFragment=supportFragmentManager.findFragmentById(R.id.activity_main_frame_layout) as CellarItemListFragment?
        if (cellarItemListFragment==null){
            cellarItemListFragment= CellarItemListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_frame_layout,cellarItemListFragment!!).commit()
        }
    }

    private fun configureBottomBar() {
        bottomBarFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_bottom_bar_frame_layout) as BottomBarFragment?
        if (bottomBarFragment == null) {
            bottomBarFragment = BottomBarFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_bottom_bar_frame_layout, bottomBarFragment!!).commit()
        }
    }

    private fun configureNavigationView() {
        //configure navigation view
        navigationView = findViewById(R.id.activity_main_navigation_view)
        headerTitle =
            navigationView.getHeaderView(0).findViewById(R.id.activity_main_drawer_cellar_title)
        sortOption0 = navigationView.findViewById(R.id.activity_main_drawer_sort_0_radio)
        sortOption1 = navigationView.findViewById(R.id.activity_main_drawer_sort_1_radio)
        sortOption2 = navigationView.findViewById(R.id.activity_main_drawer_sort_2_radio)
        sortGroup = navigationView.findViewById(R.id.activity_main_drawer_sort_radio_group)
        navigationView.setNavigationItemSelectedListener(this)

        //Put listener on radiogroup
        sortGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            sSortOption = 0
            if (checkedId == sortOption1.getId()) sSortOption = 1
            if (checkedId == sortOption2.getId()) sSortOption = 2
            val preferences = getPreferences(MODE_PRIVATE)
            preferences.edit().putInt(CURRENT_SORT_OPTION, sSortOption).apply()
            configureSortOptions()
            if (Cellar.cellarPool.size > 0) Collections.sort(
                Cellar.cellarPool[currentCellarIndex].cellList,
                CellComparator
            )
            if (cellarFragment != null) cellarFragment!!.updateCellarRecyclerView(
                Cellar.cellarPool[currentCellarIndex].typeFiltered(
                    currentTypeFilter
                )
            )
            drawerLayout!!.closeDrawer(GravityCompat.START)
        })
    }

    private fun configureDrawerLayout() {
        //configure drawer layout
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setDrawerCellarTitle() {
        //set the title at the cellar name
        if (Cellar.cellarPool.size != 0) headerTitle!!.text =
            Cellar.cellarPool[currentCellarIndex].cellarName
    }

    private fun configureToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun configureSortOptions() {
        //initalize sort options radios
        when (sSortOption) {
            1 -> {
                //Vintage+/App+/Dom+/Cuv+/Stock-
                sortOption1!!.isChecked = true
                CellComparator.setSortingOrder(1, 2, 3, 0, 4)
                CellComparator.setSortingSense(1, 1, 1, 1, -1)
            }
            2 -> {
                //Stock-/App+/Dom+/Cuv+/Vintage+
                sortOption2!!.isChecked = true
                CellComparator.setSortingOrder(1, 2, 3, 4, 0)
                CellComparator.setSortingSense(1, 1, 1, 1, -1)
            }
            else -> {
                //case 0
                //App+/Dom+/Cuv+/Vintage+/Stock-
                sortOption0!!.isChecked = true
                CellComparator.setSortingOrder(0, 1, 2, 3, 4)
                CellComparator.setSortingSense(1, 1, 1, 1, -1)
            }
        }
    }

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
        setDrawerCellarTitle()
    }

    private fun showCellarChoiceDialogFragment(dialogTitle: String, menuTag: String) {
        //prompt to chose current cellar
        sMenuTag = menuTag
        val fm = supportFragmentManager
        val dialogFragment = CellarChoiceDialogFragment.newInstance(dialogTitle)
        dialogFragment.show(fm, "fragment_cellar_choice")
    }

    private fun showEditDialogFragment(
        dialogTitle: String,
        invite: String,
        preFilledInput: String,
        menuTag: String
    ) {
//        display edit dialog
        sMenuTag = menuTag
        val fm = supportFragmentManager
        val edf = EditDialogFragment.newInstance(dialogTitle, invite, preFilledInput)
        edf.show(fm, "fragment_edit_dialog")
    }

    private fun showSortOptionsEditor() {
        //user can create their own sort options
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

    private fun prepareFirstUse() {
        //manage the first use

        //create a first cellar
        val cellArrayList = ArrayList<Cell>()
        val cellar = Cellar(getString(R.string.main_activity_my_cellar), cellArrayList, true)
    }

    //    **********************************************************************************************
//    Send intents
//    **********************************************************************************************
    private fun launchEditCellarActivity(cellPosition: Int) {
        //launch the cellar editor
        val intent = Intent(this@MainActivity, EditCellarActivity::class.java)
        intent.putExtra(BUNDLE_EXTRA_CURRENT_CELLAR_INDEX, currentCellarIndex)
        intent.putExtra(BUNDLE_EXTRA_CELL_POSITION, cellPosition)
        this.startActivity(intent)
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