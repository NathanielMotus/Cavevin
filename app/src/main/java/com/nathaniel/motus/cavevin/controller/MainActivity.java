package com.nathaniel.motus.cavevin.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.model.Bottle;
import com.nathaniel.motus.cavevin.model.Cell;
import com.nathaniel.motus.cavevin.model.CellComparator;
import com.nathaniel.motus.cavevin.model.Cellar;
import com.nathaniel.motus.cavevin.model.CellarStorageUtils;
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
    private static String sCurrentTypeFilter="all";
    private static int sSortOption=0;
    private static final int REQUEST_URI_CREATE=1;
    private static final int REQUEST_URI_LOAD=2;
    private static final String sFolderName="Cavevin";
    private static final String sDataBaseFileName="Database.txt";

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

        if (Cellar.getNumberOfCellars()>0) Collections.sort(Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList(),new CellComparator());

        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        configureBottomBar();
        configureSortOptions();

        setDrawerCellarTitle();
        configureAndShowCellarFragment();
    }

    @Override
    protected void onDestroy() {
        //Save the datas when app is left
        super.onDestroy();

        saveDatas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Export database
        if (requestCode==REQUEST_URI_CREATE && resultCode==RESULT_OK) {
            Uri pathNameUri=data.getData();
            CellarStorageUtils.writeDatabaseToExportFile(this,pathNameUri);
        }

        //Import database
        if (requestCode==REQUEST_URI_LOAD && resultCode==RESULT_OK){
            Uri pathNameUri=data.getData();
            CellarStorageUtils.readDataBaseFromImportFile(this,pathNameUri);
            sCurrentCellarIndex=0;
            setDrawerCellarTitle();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    showCellarChoiceDialogFragment("Supprimer une cave",MENU_ITEM_DELETE_CELLAR);
                }else Toast.makeText(this,"Il doit rester au moins une cave",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_activity_main_export:
                sendExportDataBaseIntent();
                return true;

            case R.id.menu_activity_main_import:
                sendImportDataBaseIntent();
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
                showCellarChoiceDialogFragment("Choisir une cave",MENU_ITEM_CHOOSE_CELLAR);
                break;
            case R.id.activity_main_drawer_create_cellar:
                showEditDialogFragment("Créer une cave","Nom de la cave","",MENU_ITEM_CREATE_CELLAR);
                break;
            case R.id.activity_main_drawer_rename_cellar:
                showEditDialogFragment("Renommer la cave","Nouveau nom",Cellar.getCellarPool().get(sCurrentCellarIndex).getCellarName(),MENU_ITEM_RENAME_CELLAR);
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

//    **********************************************************************************************
//    Callbacks
//    **********************************************************************************************

    @Override
    public void onItemClicked(View view,int position) {
        launchEditCellarActivity(position);
    }

    @Override
    public void onBottomBarItemClicked(View v, String buttonTag) {
        switch (buttonTag){
            case "red":
                sCurrentTypeFilter="red";
                break;
            case "white":
                sCurrentTypeFilter="white";
                break;
            case "pink":
                sCurrentTypeFilter="pink";
                break;
            default:
                sCurrentTypeFilter="all";
        }
        cellarFragment.updateCellarRecyclerView(Cellar.getCellarPool().get(sCurrentCellarIndex).typeFiltered(sCurrentTypeFilter));
        SharedPreferences preferences=getPreferences(MODE_PRIVATE);
        preferences.edit().putString(CURRENT_TYPE_FILTER,sCurrentTypeFilter).apply();
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

        sCurrentTypeFilter=getPreferences(MODE_PRIVATE).getString(CURRENT_TYPE_FILTER,"red");
        sSortOption=getPreferences(MODE_PRIVATE).getInt(CURRENT_SORT_OPTION,0);
    }

//    **********************************************************************************************
//    Working subs
//    **********************************************************************************************

    private void launchEditCellarActivity(int cellPosition){
        //launch the cellar editor

        Intent intent=new Intent(MainActivity.this,EditCellarActivity.class);
        intent.putExtra(BUNDLE_EXTRA_CURRENT_CELLAR_INDEX,sCurrentCellarIndex);
        intent.putExtra(BUNDLE_EXTRA_CELL_POSITION,cellPosition);
        this.startActivity(intent);
    }

    private void loadDatas(){
        //load all the datas

        File saveDir=getFilesDir();
        File saveURI=CellarStorageUtils.createOrGetFile(saveDir,sFolderName,sDataBaseFileName);

        CellarStorageUtils.loadDataBase(saveURI);
    }

    private void saveDatas(){
        //save all the datas

        File saveDir=getFilesDir();
        File saveURI=CellarStorageUtils.createOrGetFile(saveDir,sFolderName,sDataBaseFileName);

        CellarStorageUtils.saveDataBase(saveURI);
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

    private void sendExportDataBaseIntent(){
        //export the whole database to a file chosen by user

        Intent intent=new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/*");
        startActivityForResult(intent,REQUEST_URI_CREATE);
    }

    private void sendImportDataBaseIntent(){
        //import the whole database from file chose by user

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        startActivityForResult(intent,REQUEST_URI_LOAD);
    }

    private void showStats(){
        //show stats

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        String message="Stats globales:\n\n";
        message=message+Cellar.getNumberOfCellars()+" cave(s)\n";
        message=message+Bottle.getNumberOfReferences()+" référence(s)\n";
        message=message+Cellar.totalStock()+" bouteille(s) stockée(s)\n\n\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getCellarName()+" :\n\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().size()+" référence(s) en stock\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getStock()+" bouteille(s) stockée(s), dont :\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getStock(RED_WINE)+" de rouge\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getStock(WHITE_WINE)+" de blanc\n";
        message=message+Cellar.getCellarPool().get(sCurrentCellarIndex).getStock(PINK_WINE)+" de rosé\n";

        builder.setTitle("Stats")
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
        Cellar cellar=new Cellar("Ma cave",cellArrayList,true);
    }

}