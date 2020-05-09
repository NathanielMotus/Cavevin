package com.nathaniel.motus.cavevin.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.model.Bottle;
import com.nathaniel.motus.cavevin.model.Cell;
import com.nathaniel.motus.cavevin.model.Cellar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EditCellarActivity extends AppCompatActivity {

    //Debug values
    private static final String TAG="EditCellarActivity";

    //List of bottle names
    private static ArrayList<String> bottleNameList= new ArrayList<>(Arrays.asList("Bouteille","Piccolo","Quart","Demi","Pot","Magnum","Marie-Jeanne","Réhoboam","Jéroboam","Mathusalem","Salmanazar","Balthazar","Nabuchodonosor","Melchior"));

    //declaration of the views
    TextView mTitleText;
    AutoCompleteTextView mAppellationACTV;
    AutoCompleteTextView mDomainACTV;
    AutoCompleteTextView mCuveeACTV;
    RadioButton mRedWineRadio;
    RadioButton mWhiteWineRadio;
    RadioButton mPinkWineRadio;
    EditText mVintageEdit;
    Spinner mCapacitySpinner;
    EditText mBottleCommentEdit;
    EditText mOriginEdit;
    EditText mStockEdit;
    EditText mCellarCommentEdit;
    Button mOKButton;
    Button mDeleteButton;

    //To always know the cellarindex
    private static int sCurrentCellarIndex=0;
    private static int sCellPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cellar);

        //Get the current cellar index
        Intent intent=getIntent();
        sCurrentCellarIndex=intent.getIntExtra(MainActivity.BUNDLE_EXTRA_CURRENT_CELLAR_INDEX,0);
        sCellPosition=intent.getIntExtra(MainActivity.BUNDLE_EXTRA_CELL_POSITION,-1);

        //Instantiate the views
        mTitleText=findViewById(R.id.activity_edit_cellar_title_text);
        mAppellationACTV=findViewById(R.id.activity_edit_cellar_appellation_ACTV);
        mDomainACTV=findViewById(R.id.activity_edit_cellar_domain_ACTV);
        mCuveeACTV=findViewById(R.id.activity_edit_cellar_cuvee_ACTV);
        mRedWineRadio=findViewById(R.id.activity_edit_cellar_red_radio);
        mWhiteWineRadio=findViewById(R.id.activity_edit_cellar_white_radio);
        mPinkWineRadio=findViewById(R.id.activity_edit_cellar_pink_radio);
        mVintageEdit=findViewById(R.id.activity_edit_cellar_vintage_edit);
        mCapacitySpinner=findViewById(R.id.activity_edit_cellar_capacity_spinner);
        mBottleCommentEdit=findViewById(R.id.activity_edit_cellar_bottle_comment_edit);
        mOriginEdit=findViewById(R.id.activity_edit_cellar_origin_edit);
        mStockEdit=findViewById(R.id.activity_edit_cellar_stock_edit);
        mCellarCommentEdit=findViewById(R.id.activity_edit_cellar_cellar_comment_edit);
        mOKButton=findViewById(R.id.activity_edit_cellar_ok_button);
        mDeleteButton=findViewById(R.id.activity_edit_cellar_delete_button);

        //if not creation "delete" is not visible
        if (sCellPosition==-1) mDeleteButton.setVisibility(View.GONE);

        //Put on clicklistener
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEntry();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCellarEntry();
//                finish();
            }
        });


        initializeFields();

        configureToolBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void configureToolBar(){
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void createEntry(){
        //collect all the datas submitted and create a new bottle in the current cellar

        //Collect the datas
        String appellation=CellarInputUtils.replaceForbiddenCharacters(this,mAppellationACTV.getText().toString());
        String domain=CellarInputUtils.replaceForbiddenCharacters(this,mDomainACTV.getText().toString());
        String cuvee=CellarInputUtils.replaceForbiddenCharacters(this,mCuveeACTV.getText().toString());
        String type="";
        if (mRedWineRadio.isChecked()) type="0";
        if (mWhiteWineRadio.isChecked()) type="1";
        if (mPinkWineRadio.isChecked()) type="2";
        String vintage=CellarInputUtils.replaceForbiddenCharacters(this,mVintageEdit.getText().toString());
        int capacityIndex=mCapacitySpinner.getSelectedItemPosition();
        String bottleName="Bouteille";
        Float capacity=0.75f;
        switch (capacityIndex) {
            case 0: {
                bottleName = "Bouteille";
                capacity = 0.75f;
                break;
            }
            case 1: {
                bottleName = "Piccolo";
                capacity = 0.2f;
                break;
            }
            case 2: {
                bottleName = "Quart";
                capacity = 0.25f;
                break;
            }
            case 3: {
                bottleName = "Demi";
                capacity = 0.375f;
                break;
            }
            case 4: {
                bottleName = "Pot";
                capacity = 0.46f;
                break;
            }
            case 5: {
                bottleName = "Magnum";
                capacity = 1.5f;
                break;
            }
            case 6: {
                bottleName = "Marie-Jeanne";
                capacity = 3f;
                break;
            }
            case 7: {
                bottleName = "Réhoboam";
                capacity = 4.5f;
                break;
            }
            case 8: {
                bottleName = "Jéroboam";
                capacity = 5f;
                break;
            }
            case 9: {
                bottleName = "Mathusalem";
                capacity = 6f;
                break;
            }
            case 10: {
                bottleName = "Salmanazar";
                capacity = 9f;
                break;
            }
            case 11: {
                bottleName = "Balthazar";
                capacity = 12f;
                break;
            }
            case 12: {
                bottleName = "Nabuchodonosor";
                capacity = 15f;
                break;
            }
            case 13: {
                bottleName = "Melchior";
                capacity = 18f;
            }
        }
        String bottleComment=CellarInputUtils.replaceForbiddenCharacters(this,mBottleCommentEdit.getText().toString());
        String origin=CellarInputUtils.replaceForbiddenCharacters(this,mOriginEdit.getText().toString());
        int stock=Integer.parseInt(mStockEdit.getText().toString());
        String cellarComment=CellarInputUtils.replaceForbiddenCharacters(this,mCellarCommentEdit.getText().toString());

        //Create new objects if it is a creation
        if (sCellPosition==-1) {
            Bottle bottle = new Bottle(appellation, domain, cuvee, type, vintage, bottleName, capacity, bottleComment, true);
            Cell cell = new Cell(bottle, origin, stock, cellarComment, true);
            Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().add(cell);

            initializeFields();
            Toast.makeText(this,"Entrée créée",Toast.LENGTH_SHORT).show();
            mAppellationACTV.requestFocus();
        } else {
            //modify the existing entry
            Cell cell = Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().get(sCellPosition);
            Bottle bottle = cell.getBottle();
            bottle.setAppellation(appellation);
            bottle.setDomain(domain);
            bottle.setCuvee(cuvee);
            bottle.setType(type);
            bottle.setVintage(vintage);
            bottle.setBottleName(bottleName);
            bottle.setCapacity(capacity);
            bottle.setBottleComment(bottleComment);
            cell.setOrigin(origin);
            cell.setStock(stock);
            cell.setCellComment(cellarComment);

            Toast.makeText(this,"Entrée modifiée",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeFields(){
        //initialize cellar editor fields

        updateAutoCompletionTextViewAdapter();

        if (sCellPosition==-1) {
            mTitleText.setText("Créer une entrée");
            mAppellationACTV.setText("");
            mDomainACTV.setText("");
            mCuveeACTV.setText("");
            mRedWineRadio.setChecked(true);
            mVintageEdit.setText("");
            mCapacitySpinner.setSelection(0);
            mBottleCommentEdit.setText("");
            mOriginEdit.setText("");
            mStockEdit.setText("1");
            mCellarCommentEdit.setText("");
        } else {
            mTitleText.setText("Modifier une entrée");
            Cell cell=Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().get(sCellPosition);
            Bottle bottle=cell.getBottle();
            mAppellationACTV.setText(bottle.getAppellation());
            mDomainACTV.setText(bottle.getDomain());
            mCuveeACTV.setText(bottle.getCuvee());
            switch (bottle.getType()){
                case "0":
                    mRedWineRadio.setChecked(true);
                    break;
                case "1":
                    mWhiteWineRadio.setChecked(true);
                    break;
                case "2":
                    mPinkWineRadio.setChecked(true);
                    break;
                default:
                    mRedWineRadio.setChecked(true);
            }
            mVintageEdit.setText(bottle.getVintage());
            int i=0;
            String bottleName=bottle.getBottleName();
            while (bottleNameList.get(i).compareTo(bottleName)!=0) i++;
            mCapacitySpinner.setSelection(i);
            mBottleCommentEdit.setText(bottle.getBottleComment());
            mOriginEdit.setText(cell.getOrigin());
            mStockEdit.setText(Integer.toString(cell.getStock()));
            mCellarCommentEdit.setText(cell.getCellComment());
        }
    }

    private void deleteCellarEntry(){
        //delete entry when delete is clicked

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Attention !")
                .setMessage("Etes-vous sûr de vouloir supprimer cette entrée ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().get(sCellPosition).removeCell();
                        Toast.makeText(getApplicationContext(),"Entrée supprimée",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();

    }

//    **********************************************************************************************
//    Autocompletion subs
//    **********************************************************************************************

    private ArrayList<String> getAppellationAutoCompletionList(){
        //return list of already submitted appellations

        ArrayList<String> currentStringList=new ArrayList<>();
        String currentString;
        for (int i=0;i<Bottle.getNumberOfReferences();i++){
            currentString=Bottle.getBottleCatalog().get(i).getAppellation();
            if (currentStringList.indexOf(currentString)<0) currentStringList.add(currentString);
        }
        Collections.sort(currentStringList);
        return currentStringList;
    }

    private ArrayList<String> getDomainAutoCompletionList(){
        //return list of already submitted appellations

        ArrayList<String> currentStringList=new ArrayList<>();
        String currentString;
        for (int i=0;i<Bottle.getNumberOfReferences();i++){
            currentString=Bottle.getBottleCatalog().get(i).getDomain();
            if (currentStringList.indexOf(currentString)<0) currentStringList.add(currentString);
        }
        Collections.sort(currentStringList);
        return currentStringList;
    }

    private ArrayList<String> getCuveeAutoCompletionList(){
        //return list of already submitted appellations

        ArrayList<String> currentStringList=new ArrayList<>();
        String currentString;
        for (int i=0;i<Bottle.getNumberOfReferences();i++){
            currentString=Bottle.getBottleCatalog().get(i).getCuvee();
            if (currentStringList.indexOf(currentString)<0) currentStringList.add(currentString);
        }
        Collections.sort(currentStringList);
        return currentStringList;
    }

    private void updateAutoCompletionTextViewAdapter(){
        //put on autocomplete lists

        ArrayAdapter<String> appellationAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,getAppellationAutoCompletionList());
        mAppellationACTV.setAdapter(appellationAdapter);

        ArrayAdapter<String> domainAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,getDomainAutoCompletionList());
        mDomainACTV.setAdapter(domainAdapter);

        ArrayAdapter<String> cuveeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,getCuveeAutoCompletionList());
        mCuveeACTV.setAdapter(cuveeAdapter);
    }



}
