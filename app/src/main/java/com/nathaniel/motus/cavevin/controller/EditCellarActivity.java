package com.nathaniel.motus.cavevin.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.exifinterface.media.ExifInterface;

import com.nathaniel.motus.cavevin.R;
import com.nathaniel.motus.cavevin.model.Bottle;
import com.nathaniel.motus.cavevin.model.Cell;
import com.nathaniel.motus.cavevin.model.Cellar;

import java.io.File;
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
    Button mStorageButton;
    Button mCameraButton;
    Button mDeletePhotoButton;
    Button mOKButton;
    Button mDeleteButton;
    ImageView mPhotoImage;

//    **********************************************************************************************
//    Intent request tags
//    **********************************************************************************************
    private static final int REQUEST_PHOTO_PATHNAME=200;
    private static final int REQUEST_CAMERA_USE=201;

    //To always know the cellarindex
    private static int sCurrentCellarIndex=0;
    private static int sCellPosition=0;

    //To know whether a photo was chosen
    private static final String PHOTO_IS_NEW="photo is new";
    private static final String PHOTO_HAS_NOT_CHANGED="photo has not changed";
    private static final String PHOTO_WAS_DELETED="photo was deleted";
    private static String sPhotoHasChanged=PHOTO_HAS_NOT_CHANGED;

    //the file in which the photo taken will be saved
    //as to be a global field as it cannot be passed back through intent
    private Uri mPhotoTakenUri;
    private String mPhotoTakenFolderName="DCIM";
    private String mPhotoTakenName="temporary_pic.jpg";
    private String mPhotoThumbnailSuffix="_thumb";


//    **********************************************************************************************
//    EditCellarActivity events
//    **********************************************************************************************

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
        mStorageButton=findViewById(R.id.activity_edit_cellar_storage_button);
        mCameraButton=findViewById(R.id.activity_edit_cellar_camera_button);
        mDeletePhotoButton=findViewById(R.id.activity_edit_cellar_delete_photo_button);
        mPhotoImage=findViewById(R.id.activity_edit_cellar_photo_image);


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
                deleteBottlePhoto();
                deleteCellarEntry();
            }
        });

        mStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGetPhotoPathNameIntent();
            }
        });

        mDeletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBottlePhoto();
            }
        });

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTakePhotoIntent();
            }
        });

        mPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sPhotoHasChanged.compareTo(PHOTO_HAS_NOT_CHANGED) == 0) {
                    String photoName=Cellar.getCellarPool().get(sCurrentCellarIndex)
                            .getCellList().get(sCellPosition)
                            .getBottle().getPhotoName();
                    if (photoName.compareTo("") != 0) {
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(CellarPictureUtils.getUriFromFileProvider(getApplicationContext(),photoName),"image/*");
                        startActivity(intent);
                    }
                }

                if (sPhotoHasChanged.compareTo(PHOTO_IS_NEW) == 0) {
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(CellarStorageUtils.createOrGetFile(Environment.getExternalStorageDirectory(),mPhotoTakenFolderName,mPhotoTakenName)),"image/*");
                    startActivity(intent);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //pathname request
        if (requestCode==REQUEST_PHOTO_PATHNAME && resultCode==RESULT_OK){
            Uri uri=data.getData();
            Bitmap bitmap= CellarStorageUtils.getBitmapFromUri(getApplicationContext(),uri);

            //save photo to temporary_pic to get the exif
            CellarStorageUtils.saveBitmapToInternalStorage(Environment.getExternalStorageDirectory(),mPhotoTakenFolderName,mPhotoTakenName,bitmap);

            mPhotoImage.setImageBitmap(CellarStorageUtils.getBitmapFromInternalStorage(Environment.getExternalStorageDirectory(),mPhotoTakenFolderName,mPhotoTakenName));
            sPhotoHasChanged=PHOTO_IS_NEW;

        }

        //Camera use request
        if (requestCode==REQUEST_CAMERA_USE && resultCode==RESULT_OK){
            mPhotoImage.setImageBitmap(CellarStorageUtils.getBitmapFromInternalStorage(Environment.getExternalStorageDirectory(),mPhotoTakenFolderName,mPhotoTakenName));
            sPhotoHasChanged=PHOTO_IS_NEW;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //delete temporary files
        CellarStorageUtils.deleteFileFromInternalStorage(Environment.getExternalStorageDirectory(),mPhotoTakenFolderName,mPhotoTakenName);

    }

//    **********************************************************************************************
//    Configuration subs
//    **********************************************************************************************

    private void configureToolBar(){
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initializeFields(){
        //initialize cellar editor fields

        updateAutoCompletionTextViewAdapter();

        sPhotoHasChanged=PHOTO_HAS_NOT_CHANGED;

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
            mPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.photo_frame));
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
            if(bottle.getPhotoName().compareTo("")!=0)
                mPhotoImage.setImageBitmap(CellarStorageUtils.getBitmapFromInternalStorage(getFilesDir(),
                        getResources().getString(R.string.photo_folder_name),
                        bottle.getPhotoName()+mPhotoThumbnailSuffix));
            else
                mPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.photo_frame));
        }
    }



//    **********************************************************************************************
//    Working subs
//    **********************************************************************************************

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

        //handle the new photo
        String photoName="";
        Log.i(TAG,sPhotoHasChanged);
        if(sPhotoHasChanged.compareTo(PHOTO_IS_NEW)==0){

            Bitmap bitmap=((BitmapDrawable)mPhotoImage.getDrawable()).getBitmap();
            photoName=CellarStorageUtils.saveBottleImageToInternalStorage(getFilesDir(),getResources().getString(R.string.photo_folder_name),bitmap);

            //create a thumbnail
            Bitmap thumbnail=CellarStorageUtils.decodeSampledBitmapFromFile(getFilesDir(),getResources().getString(R.string.photo_folder_name),photoName,
                    (int)getResources().getDimension(R.dimen.recyclerview_cellar_row_photo_width),
                    (int)getResources().getDimension(R.dimen.recyclerview_cellar_row_photo_height));
            CellarStorageUtils.saveBitmapToInternalStorage(getFilesDir(),getResources().getString(R.string.photo_folder_name),photoName+mPhotoThumbnailSuffix,
                    thumbnail);
        }

        //Create new objects if it is a creation
        if (sCellPosition==-1) {
            Bottle bottle = new Bottle(appellation, domain, cuvee, type, vintage, bottleName, capacity, bottleComment,photoName, true);
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
            if (sPhotoHasChanged.compareTo(PHOTO_HAS_NOT_CHANGED)!=0) bottle.setPhotoName(photoName);
            cell.setOrigin(origin);
            cell.setStock(stock);
            cell.setCellComment(cellarComment);

            Toast.makeText(this,"Entrée modifiée",Toast.LENGTH_SHORT).show();
            finish();
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

    private void deleteBottlePhoto(){
        //delete the photo when delete photo is clicked

        CellarStorageUtils.deleteFileFromInternalStorage(getFilesDir(),
                getResources().getString(R.string.photo_folder_name),
                Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().get(sCellPosition).getBottle().getPhotoName());
        CellarStorageUtils.deleteFileFromInternalStorage(getFilesDir(),
                getResources().getString(R.string.photo_folder_name),
                Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().get(sCellPosition).getBottle().getPhotoName()+mPhotoThumbnailSuffix);

        mPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.photo_frame));
        Cellar.getCellarPool().get(sCurrentCellarIndex).getCellList().get(sCellPosition).getBottle().setPhotoName("");
        sPhotoHasChanged=PHOTO_WAS_DELETED;
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

//    **********************************************************************************************
//    Sending intents
//    **********************************************************************************************

    private void sendGetPhotoPathNameIntent(){
        //get the pathname of a photo chosen by user

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_PHOTO_PATHNAME);
    }

    private void sendTakePhotoIntent() {
        //ask for taking a photo and put it in predefined Uri
        //that Uri has to be deleted after usage

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo=new File(Environment.getExternalStorageDirectory(),mPhotoTakenFolderName+"/"+mPhotoTakenName);
        mPhotoTakenUri=Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mPhotoTakenUri);
        startActivityForResult(intent,REQUEST_CAMERA_USE);
    }



}
