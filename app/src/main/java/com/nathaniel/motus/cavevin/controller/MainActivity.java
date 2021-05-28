package com.nathaniel.motus.cavevin.controller;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.model.Bottle;
import com.nathaniel.motus.cavevin.model.Cell;
import com.nathaniel.motus.cavevin.model.CellComparator;
import com.nathaniel.motus.cavevin.model.Cellar;
import com.nathaniel.motus.cavevin.view.BottomBarFragment;
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment;
import com.nathaniel.motus.cavevin.view.CellarFragment;
import com.nathaniel.motus.cavevin.view.EditDialogFragment;
import com.nathaniel.motus.cavevin.view.MyRecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.onItemClickedListener,
        BottomBarFragment.onBottomBarClickedListener,
        NavigationView.OnNavigationItemSelectedListener,
        CellarChoiceDialogFragment.onCellarChoiceFragmentClickListener,
        EditDialogFragment.onEditDialogClickListener {

    //**********************************************************************************************
    //variables de test
    private static final String TAG="MainActivity";
    //**********************************************************************************************

    public static String BUNDLE_EXTRA_CURRENT_CELLAR_INDEX="BUNDLE_EXTRA_CURRENT_CELLAR_INDEX";
    public static String BUNDLE_EXTRA_CELL_POSITION="BUNDLE_EXTRA_CELL_POSITION";
    private static int sCurrentCellarIndex=0;
    private static final String TYPE_FILTER_RED="red";
    private static final String TYPE_FILTER_WHITE="white";
    private static final String TYPE_FILTER_PINK="pink";
    private static final String TYPE_FILTER_ALL="all";
    private static String sCurrentTypeFilter=TYPE_FILTER_ALL;

    private static int sSortOption=0;
    private static final int REQUEST_URI_CREATE=100;
    private static final int REQUEST_URI_LOAD=101;
    private static final int REQUEST_PERMISSION =102;
    private static final int REQUEST_CSV_URI_CREATE=103;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_INDEX=0;
    private static final int CAMERA_PERMISSION_INDEX=1;

    //Following values are used to handle callback
    private static String sMenuTag;//to indicate where the callback comes from
    private static final String MENU_ITEM_CREATE_CELLAR="CREATE CELLAR";
    private static final String MENU_ITEM_RENAME_CELLAR="RENAME CELLAR";
    private static final String MENU_ITEM_CHOOSE_CELLAR="CHOOSE CELLAR";
    private static final String MENU_ITEM_DELETE_CELLAR="DELETE CELLAR";

    // Types of wine
    private static final String RED_WINE="0";
    private static final String WHITE_WINE="1";
    private static final String PINK_WINE="2";

    //Shared preferences tags
    private static final String CURRENT_CELLAR_INDEX="Current cellar index";
    private static final String CURRENT_TYPE_FILTER="Current type filter";
    private static final String CURRENT_SORT_OPTION ="Current sort options";


    //Fragments declaration
    private CellarFragment cellarFragment;
    private BottomBarFragment bottomBarFragment;

    //Views declaration
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private TextView headerTitle;
    private NavigationView navigationView;
    private RadioButton sortOption0;
    private RadioButton sortOption1;
    private RadioButton sortOption2;
    private RadioGroup sortGroup;

    //Permissions status
    private static boolean sWriteExternalStoragePermission=true;
    private static boolean sCameraPermission=true;

//    **********************************************************************************************
//    MainActivity events
//    **********************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get and configure the views
        loadDatas();
        if (Cellar.getNumberOfCellars()==0) prepareFirstUse();
        cleanUpDatabase();
        getSharedPreferences();

        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        configureBottomBar();
        configureSortOptions();

        if (Cellar.getNumberOfCellars()>0) Collections.sort(Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList(),new CellComparator());

        setDrawerCellarTitle();
        configureAndShowCellarFragment();
    }

    @Override
    protected void onDestroy() {
        //Save the datas when app is left
        super.onDestroy();

        saveDatas();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
        if (Cellar.getNumberOfCellars()>0) Collections.sort(Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList(),new CellComparator());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Export database
        if (requestCode==REQUEST_URI_CREATE && resultCode==RESULT_OK) {
            Uri pathNameUri=data.getData();
            CellarStorageUtils.zipFileAtPath(getApplicationContext(),
                    new File(getFilesDir(),getResources().getString(R.string.database_folder_name)).getPath(),
                    pathNameUri);
        }

        //Import database
        if (requestCode==REQUEST_URI_LOAD && resultCode==RESULT_OK){
            Uri pathNameUri=data.getData();
            importDatabase(pathNameUri);
        }

        //Export CSV
        if (requestCode == REQUEST_CSV_URI_CREATE && resultCode == RESULT_OK) {
            Uri csvUri=data.getData();
            CellarStorageUtils.exportCellarToCsvFile(this,csvUri);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if(grantResults[WRITE_EXTERNAL_STORAGE_PERMISSION_INDEX]==PackageManager.PERMISSION_GRANTED)
                    sWriteExternalStoragePermission=true;
                else
                    sWriteExternalStoragePermission=false;
                if (grantResults[CAMERA_PERMISSION_INDEX]==PackageManager.PERMISSION_GRANTED)
                    sCameraPermission=true;
                else
                    sCameraPermission=false;
                break;
            default:

        }
    }

//    **********************************************************************************************
//    Toolbar events
//    **********************************************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_activity_main_add_entry:
//                create a new entry
                launchEditCellarActivity(-1);
                return true;

            case R.id.menu_activity_main_delete_cellar:
//                delete a chosen cellar
                if (Cellar.getCellarPool().size()>1){
                    showCellarChoiceDialogFragment(getString(R.string.main_activity_delete_cellar),MENU_ITEM_DELETE_CELLAR);
                }else Toast.makeText(this, R.string.main_activity_at_least_one_cellar,Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_activity_main_export:
                if (sWriteExternalStoragePermission)
                    sendExportDataBaseIntent();
                return true;

            case R.id.menu_activity_main_import:
                sendImportDataBaseIntent();
                return true;

            case R.id.menu_activity_main_csv_export:
                sendExportCsvIntent();
                return true;

            case R.id.menu_activity_main_stats:
                showStats();
                return true;

            default :
                return super.onOptionsItemSelected(item);
        }
    }

//    **********************************************************************************************
//    Navigation view events
//    **********************************************************************************************

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //manage navigation view menu clicks

        int id=menuItem.getItemId();

        switch (id){
            case R.id.activity_main_drawer_choose_cellar:
                showCellarChoiceDialogFragment(getString(R.string.main_activity_choose_cellar),MENU_ITEM_CHOOSE_CELLAR);
                break;
            case R.id.activity_main_drawer_create_cellar:
                showEditDialogFragment(getString(R.string.main_activity_new_cellar),getString(R.string.main_activity_cellar_name),"",MENU_ITEM_CREATE_CELLAR);
                break;
            case R.id.activity_main_drawer_rename_cellar:
                showEditDialogFragment(getString(R.string.main_activity_rename_cellar),getString(R.string.main_activity_new_cellar_name),Cellar.getCellarPool().get(sCurrentCellarIndex).getCellarName(),MENU_ITEM_RENAME_CELLAR);
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }



//    **********************************************************************************************
//    Getters
//    **********************************************************************************************

    public static int getCurrentCellarIndex() {
        return sCurrentCellarIndex;
    }

    public static String getCurrentTypeFilter() {
        return sCurrentTypeFilter;
    }

    public static boolean getWriteExternalStoragePermission() {
        return sWriteExternalStoragePermission;
    }

    public static boolean getCameraPermission() {
        return sCameraPermission;
    }

    //    **********************************************************************************************
//    Callbacks
//    **********************************************************************************************

    @Override
    public void onItemClicked(View view,int position) {

        //Case edit button clicked
        if (view.getId()==R.id.recycler_cellar_row_edit_image)
            launchEditCellarActivity(position);

        //Case photo clicked
        if (view.getId() == R.id.recycler_cellar_row_photo_image) {
            String photoName=Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().get(position).getBottle().getPhotoName();
            if (photoName.compareTo("") != 0) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(CellarPictureUtils.getUriFromFileProvider(getApplicationContext(),photoName),"image/*");
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBottomBarItemClicked(View v, String buttonTag) {
        switch (buttonTag){
            case "red":
                sCurrentTypeFilter=TYPE_FILTER_RED;
                break;
            case "white":
                sCurrentTypeFilter=TYPE_FILTER_WHITE;
                break;
            case "pink":
                sCurrentTypeFilter=TYPE_FILTER_PINK;
                break;
            default:
                sCurrentTypeFilter=TYPE_FILTER_ALL;
        }
        SharedPreferences preferences=getPreferences(MODE_PRIVATE);
        preferences.edit().putString(CURRENT_TYPE_FILTER,sCurrentTypeFilter).apply();
        cellarFragment.updateCellarRecyclerView(Cellar.getCellarPool().get(sCurrentCellarIndex).typeFiltered(sCurrentTypeFilter));
    }

    @Override
    public void onCellarChoiceFragmentClick(int position) {
//        coming back from CellarChoiceDialog
//        sMenuTag decides what to do

        switch (sMenuTag){
            case MENU_ITEM_CHOOSE_CELLAR:
//                set chosen cellar as new current cellar
                sCurrentCellarIndex=position;
                SharedPreferences preferences=getPreferences(MODE_PRIVATE);
                preferences.edit().putInt(CURRENT_CELLAR_INDEX,sCurrentCellarIndex).apply();
                break;

            case MENU_ITEM_DELETE_CELLAR:
//                delete selected cellar
                Cellar cellar=Cellar.getCellarPool().get(sCurrentCellarIndex);
                Cellar.getCellarPool().get(position).destroyCells();
                Cellar.getCellarPool().remove(position);
                sCurrentCellarIndex=Cellar.getCellarPool().indexOf(cellar);
                if (sCurrentCellarIndex==-1) sCurrentCellarIndex=0;
                break;

            default:
                break;
        }

        cellarFragment.updateCellarRecyclerView(Cellar.getCellarPool().get(sCurrentCellarIndex).typeFiltered(sCurrentTypeFilter));
        setDrawerCellarTitle();
    }

    @Override
    public void onEditDialogClick(View v, String inputText) {
//        Coming back from EditDialog
//        sMenuTag decides what to do

        switch (sMenuTag){
            case MENU_ITEM_CREATE_CELLAR:
//                Create a new cellar and set it as current cellar
//                and update display
                Cellar cellar=new Cellar(inputText,new ArrayList<Cell>(),true);
                sCurrentCellarIndex=Cellar.getCellarPool().size()-1;
                cellarFragment.updateCellarRecyclerView(Cellar.getCellarPool().get(sCurrentCellarIndex).typeFiltered(sCurrentTypeFilter));
                break;

            case MENU_ITEM_RENAME_CELLAR:
//                rename the current cellar
                Cellar.getCellarPool().get(sCurrentCellarIndex).setCellarName(inputText);
                break;

            default:
                break;
        }
//        Done on every callback
        setDrawerCellarTitle();
    }

//    **********************************************************************************************
//    Initialization subs
//    **********************************************************************************************

    private void configureAndShowCellarFragment(){
        cellarFragment=(CellarFragment)getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (cellarFragment==null){
            cellarFragment=new CellarFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_main_frame_layout,cellarFragment).commit();
        }
    }

    private void configureBottomBar(){
        bottomBarFragment=(BottomBarFragment)getSupportFragmentManager().findFragmentById(R.id.activity_main_bottom_bar_frame_layout);
        if (bottomBarFragment==null){
            bottomBarFragment=new BottomBarFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_main_bottom_bar_frame_layout,bottomBarFragment).commit();
        }
    }

    private void configureNavigationView(){
        //configure navigation view

        navigationView=findViewById(R.id.activity_main_navigation_view);
        headerTitle=navigationView.getHeaderView(0).findViewById(R.id.activity_main_drawer_cellar_title);
        sortOption0=navigationView.findViewById(R.id.activity_main_drawer_sort_0_radio);
        sortOption1=navigationView.findViewById(R.id.activity_main_drawer_sort_1_radio);
        sortOption2=navigationView.findViewById(R.id.activity_main_drawer_sort_2_radio);
        sortGroup=navigationView.findViewById(R.id.activity_main_drawer_sort_radio_group);
        navigationView.setNavigationItemSelectedListener(this);

        //Put listener on radiogroup
        sortGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                sSortOption=0;
                if (checkedId==sortOption1.getId()) sSortOption=1;
                if (checkedId==sortOption2.getId()) sSortOption=2;
                SharedPreferences preferences=getPreferences(MODE_PRIVATE);
                preferences.edit().putInt(CURRENT_SORT_OPTION,sSortOption).apply();

                configureSortOptions();
                if (Cellar.getCellarPool().size()>0)
                    Collections.sort(Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList(),new CellComparator());

                if (cellarFragment!=null)
                cellarFragment.updateCellarRecyclerView(Cellar.getCellarPool().get(sCurrentCellarIndex).typeFiltered(sCurrentTypeFilter));
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });
    }

    private void configureDrawerLayout(){
        //configure drawer layout
        drawerLayout=findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void setDrawerCellarTitle(){
        //set the title at the cellar name

        if (Cellar.getCellarPool().size()!=0)
        headerTitle.setText(Cellar.getCellarPool().get(sCurrentCellarIndex).getCellarName());
    }

    private void configureToolBar(){
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureSortOptions(){
        //initalize sort options radios

        switch (sSortOption){
            case 1:
                //Vintage+/App+/Dom+/Cuv+/Stock-
                sortOption1.setChecked(true);
                CellComparator.setSortingOrder(1,2,3,0,4);
                CellComparator.setSortingSense(1,1,1,1,-1);
                break;
            case 2:
                //Stock-/App+/Dom+/Cuv+/Vintage+
                sortOption2.setChecked(true);
                CellComparator.setSortingOrder(1,2,3,4,0);
                CellComparator.setSortingSense(1,1,1,1,-1);
                break;
            default:
                //case 0
                //App+/Dom+/Cuv+/Vintage+/Stock-
                sortOption0.setChecked(true);
                CellComparator.setSortingOrder(0,1,2,3,4);
                CellComparator.setSortingSense(1,1,1,1,-1);
        }
    }

    private void getSharedPreferences(){
        //initialize shared preferences

        sCurrentCellarIndex=getPreferences(MODE_PRIVATE).getInt(CURRENT_CELLAR_INDEX,0);
        //check current cellar index is relevant (in case of unexpected application fail)
        if (sCurrentCellarIndex>=Cellar.getCellarPool().size()) sCurrentCellarIndex=0;

        sCurrentTypeFilter=getPreferences(MODE_PRIVATE).getString(CURRENT_TYPE_FILTER,TYPE_FILTER_ALL);
        sSortOption=getPreferences(MODE_PRIVATE).getInt(CURRENT_SORT_OPTION,0);
    }

//    **********************************************************************************************
//    Working subs
//    **********************************************************************************************

    private void loadDatas(){
        //load all the datas

        File saveDir=getFilesDir();
        File saveURI=CellarStorageUtils.createOrGetFile(saveDir,getResources().getString(R.string.database_folder_name),getResources().getString(R.string.database_file_name));

        CellarStorageUtils.loadDataBase(getApplicationContext(),saveURI);
    }

    private void saveDatas(){
        //save all the datas

        File saveDir=getFilesDir();
        File saveFileName=CellarStorageUtils.createOrGetFile(saveDir,getResources().getString(R.string.database_folder_name),getResources().getString(R.string.database_file_name));

        CellarStorageUtils.saveDataBase(saveFileName);
    }

    private void importDatabase(Uri pathNameUri) {
        //import database from file chosen by user

        //delete older files
        CellarStorageUtils.deleteRecursive(new File(getFilesDir(),
                getResources().getString(R.string.database_folder_name)));
        CellarStorageUtils.unpackZip(getApplicationContext(),
                getFilesDir().getPath(),
                pathNameUri);
        loadDatas();
        sCurrentCellarIndex=0;
        setDrawerCellarTitle();
    }

    private void showCellarChoiceDialogFragment(String dialogTitle,String menuTag){
        //prompt to chose current cellar

        sMenuTag=menuTag;

        FragmentManager fm=getSupportFragmentManager();
        CellarChoiceDialogFragment dialogFragment=CellarChoiceDialogFragment.newInstance(dialogTitle);
        dialogFragment.show(fm,"fragment_cellar_choice");
    }

    private void showEditDialogFragment(String dialogTitle, String invite, String preFilledInput,String menuTag){
//        display edit dialog

        sMenuTag=menuTag;

        FragmentManager fm=getSupportFragmentManager();
        EditDialogFragment edf=EditDialogFragment.newInstance(dialogTitle,invite,preFilledInput);
        edf.show(fm,"fragment_edit_dialog");
    }

    private void showSortOptionsEditor(){
        //user can create their own sort options
    }

    private void showStats(){
        //show stats

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        String message=getString(R.string.main_activity_global_stats)+"\n\n";
        message=message+Cellar.getNumberOfCellars()+getString(R.string.main_activity_cellars)+"\n";
        message=message+Bottle.getNumberOfReferences()+getString(R.string.main_activity_references)+"\n";
        message=message+Cellar.totalStock()+getString(R.string.main_activity_stocked_bottles)+"\n\n\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getCellarName()+" :\n\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().size()+getString(R.string.main_activity_ref_in_stock)+"\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getStock()+getString(R.string.main_activity_stocked_bottles_among_them)+"\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getStock(RED_WINE)+getString(R.string.main_activity_stock_red)+"\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getStock(WHITE_WINE)+getString(R.string.main_activity_stock_white)+"\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getStock(PINK_WINE)+getString(R.string.main_activity_stock_pink)+"\n";

        builder.setTitle(R.string.main_activity_stats)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    private void cleanUpDatabase(){
        //remove all the unused items from database
        //starting with cells, then bottles
        //this sub is meant to be used only in development

        for (int i=Cell.getNumberOfCells()-1;i>=0;i--){
            if (Cell.getCellPool().get(i).findUseCaseCellar(0)==null) Cell.getCellPool().get(i).removeCell();
        }

        for (int i=Bottle.getNumberOfReferences()-1;i>=0;i--){
            if (Bottle.getBottleCatalog().get(i).findUseCaseCell(0)==null) Bottle.getBottleCatalog().get(i).removeBottleFromCatalog();
        }
    }

    private void prepareFirstUse(){
        //manage the first use

        //create a first cellar
        ArrayList<Cell> cellArrayList=new ArrayList<>();
        Cellar cellar=new Cellar(getString(R.string.main_activity_my_cellar),cellArrayList,true);
    }

//    **********************************************************************************************
//    Send intents
//    **********************************************************************************************

    private void launchEditCellarActivity(int cellPosition){
        //launch the cellar editor

        Intent intent=new Intent(MainActivity.this,EditCellarActivity.class);
        intent.putExtra(BUNDLE_EXTRA_CURRENT_CELLAR_INDEX,sCurrentCellarIndex);
        intent.putExtra(BUNDLE_EXTRA_CELL_POSITION,cellPosition);
        this.startActivity(intent);
    }

    private void sendExportDataBaseIntent(){
        //export the whole database to a file chosen by user

        Intent intent=new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("*/*");
        startActivityForResult(intent,REQUEST_URI_CREATE);
    }

    private void sendExportCsvIntent(){
        //export the whole database to a file chosen by user in CSV format

        Intent intent=new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/*");
        startActivityForResult(intent,REQUEST_CSV_URI_CREATE);
    }


    private void sendImportDataBaseIntent(){
        //import the whole database from file chose by user

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,REQUEST_URI_LOAD);
    }

//    **********************************************************************************************
//    Check permissions
//    **********************************************************************************************

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] permissionString={Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
            ||checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
                requestPermissions(permissionString, REQUEST_PERMISSION);
        }
    }
}
