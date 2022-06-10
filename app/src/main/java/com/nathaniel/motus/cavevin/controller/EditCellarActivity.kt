package com.nathaniel.motus.cavevin.controller

import com.nathaniel.motus.cavevin.controller.CellarInputUtils.replaceForbiddenCharacters
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nathaniel.motus.cavevin.R
import android.os.Build
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.nathaniel.motus.cavevin.model.*
import java.io.File
import java.util.*

class EditCellarActivity : AppCompatActivity() {
    //declaration of the views
    lateinit var mTitleText: TextView
    lateinit var mAppellationACTV: AutoCompleteTextView
    lateinit var mDomainACTV: AutoCompleteTextView
    lateinit var mCuveeACTV: AutoCompleteTextView
    lateinit var mRedWineRadio: RadioButton
    lateinit var mWhiteWineRadio: RadioButton
    lateinit var mPinkWineRadio: RadioButton
    lateinit var mVintageEdit: EditText
    lateinit var mCapacitySpinner: Spinner
    lateinit var mBottleCommentEdit: EditText
    lateinit var mOriginEdit: EditText
    lateinit var mStockEdit: EditText
    lateinit var mCellarCommentEdit: EditText
    lateinit var mStorageButton: Button
    lateinit var mCameraButton: Button
    lateinit var mDeletePhotoButton: Button
    lateinit var mOKButton: Button
    lateinit var mDeleteButton: Button
    lateinit var mPhotoImage: ImageView

    //the file in which the photo taken will be saved
    //as to be a global field as it cannot be passed back through intent
    private var mPhotoTakenUri: Uri? = null
    private val mPhotoTakenName = "temporary_pic.jpg"
    private val mPhotoThumbnailSuffix = "_thumb"

    //    **********************************************************************************************
    //    EditCellarActivity events
    //    **********************************************************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cellar)

        //Create the bottle list
        sBottleNameList.clear()
        Collections.addAll(
            sBottleNameList, resources.getString(R.string.bottle_standard),
            resources.getString(R.string.bottle_piccolo),
            resources.getString(R.string.bottle_quarter),
            resources.getString(R.string.bottle_demi),
            resources.getString(R.string.bottle_pot),
            resources.getString(R.string.bottle_magnum),
            resources.getString(R.string.bottle_marie_jeanne),
            resources.getString(R.string.bottle_rehoboam),
            resources.getString(R.string.bottle_jeroboam),
            resources.getString(R.string.bottle_methuselah),
            resources.getString(R.string.bottle_salmanazar),
            resources.getString(R.string.bottle_balthazar),
            resources.getString(R.string.bottle_nebuchadnezzar),
            resources.getString(R.string.bottle_melchior)
        )

        //Create the capacity list
        sBottleCapacityList.clear()
        Collections.addAll(
            sBottleCapacityList, resources.getString(R.string.capacity_standard),
            resources.getString(R.string.capacity_piccolo),
            resources.getString(R.string.capacity_quarter),
            resources.getString(R.string.capacity_demi),
            resources.getString(R.string.capacity_pot),
            resources.getString(R.string.capacity_magnum),
            resources.getString(R.string.capacity_marie_jeanne),
            resources.getString(R.string.capacity_rehoboam),
            resources.getString(R.string.capacity_jeroboam),
            resources.getString(R.string.capacity_methuselah),
            resources.getString(R.string.capacity_salmanazar),
            resources.getString(R.string.capacity_balthazar),
            resources.getString(R.string.capacity_nebuchadnezzar),
            resources.getString(R.string.capacity_melchior)
        )

        //Create the spinner adapter list
        sSpinnerList.clear()
        for (i in sBottleCapacityList.indices) sSpinnerList.add(
            sBottleNameList[i] + " (" + sBottleCapacityList[i] + " L)"
        )

        //Get the current cellar index
        val intent = intent
        sCurrentCellarIndex =
            intent.getIntExtra(MainActivity.Companion.BUNDLE_EXTRA_CURRENT_CELLAR_INDEX, 0)
        sCellPosition = intent.getIntExtra(MainActivity.Companion.BUNDLE_EXTRA_CELL_POSITION, -1)

        //Instantiate the views
        mTitleText = findViewById(R.id.activity_edit_cellar_title_text)
        mAppellationACTV = findViewById(R.id.activity_edit_cellar_appellation_ACTV)
        mDomainACTV = findViewById(R.id.activity_edit_cellar_domain_ACTV)
        mCuveeACTV = findViewById(R.id.activity_edit_cellar_cuvee_ACTV)
        mRedWineRadio = findViewById(R.id.activity_edit_cellar_red_radio)
        mWhiteWineRadio = findViewById(R.id.activity_edit_cellar_white_radio)
        mPinkWineRadio = findViewById(R.id.activity_edit_cellar_pink_radio)
        mVintageEdit = findViewById(R.id.activity_edit_cellar_vintage_edit)
        mCapacitySpinner = findViewById(R.id.activity_edit_cellar_capacity_spinner)
        mBottleCommentEdit = findViewById(R.id.activity_edit_cellar_bottle_comment_edit)
        mOriginEdit = findViewById(R.id.activity_edit_cellar_origin_edit)
        mStockEdit = findViewById(R.id.activity_edit_cellar_stock_edit)
        mCellarCommentEdit = findViewById(R.id.activity_edit_cellar_cellar_comment_edit)
        mOKButton = findViewById(R.id.activity_edit_cellar_ok_button)
        mDeleteButton = findViewById(R.id.activity_edit_cellar_delete_button)
        mStorageButton = findViewById(R.id.activity_edit_cellar_storage_button)
        mCameraButton = findViewById(R.id.activity_edit_cellar_camera_button)
        mDeletePhotoButton = findViewById(R.id.activity_edit_cellar_delete_photo_button)
        mPhotoImage = findViewById(R.id.activity_edit_cellar_photo_image)


        //localize spinner
        val spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sSpinnerList)
        mCapacitySpinner.setAdapter(spinnerAdapter)

        //if not creation "delete" is not visible
        if (sCellPosition == -1) mDeleteButton.setVisibility(View.GONE)

        //Put on clicklistener
        mOKButton.setOnClickListener(View.OnClickListener { createEntry() })
        mDeleteButton.setOnClickListener(View.OnClickListener {
            deleteBottlePhoto()
            deleteCellarEntry()
        })
        mStorageButton.setOnClickListener(View.OnClickListener { sendGetPhotoPathNameIntent() })
        mDeletePhotoButton.setOnClickListener(View.OnClickListener { deleteBottlePhoto() })
        mCameraButton.setOnClickListener(View.OnClickListener { if (MainActivity.Companion.cameraPermission) sendTakePhotoIntent() })
        mPhotoImage.setOnClickListener(View.OnClickListener {
            if (sPhotoHasChanged.compareTo(PHOTO_HAS_NOT_CHANGED) == 0) {
                val photoName = Cellar.cellarPool[sCurrentCellarIndex]
                    .cellList[sCellPosition]
                    .bottle.photoName
                if (photoName.compareTo("") != 0) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(
                        CellarPictureUtils.getUriFromFileProvider(
                            applicationContext, photoName
                        ), "image/*"
                    )
                    startActivity(intent)
                }
            }
            if (sPhotoHasChanged.compareTo(PHOTO_IS_NEW) == 0) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(
                    CellarPictureUtils.getUriFromFileProvider(
                        applicationContext, mPhotoTakenName
                    ), "image/*"
                )
                startActivity(intent)
            }
        })
        configureToolBar()
        initializeFields()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //pathname request
        if (requestCode == REQUEST_PHOTO_PATHNAME && resultCode == RESULT_OK) {
            val uri = data!!.data
            val bitmap = CellarStorageUtils.getBitmapFromUri(applicationContext, uri)

            //save photo to temporary_pic
            CellarStorageUtils.saveBitmapToInternalStorage(
                filesDir,
                resources.getString(R.string.photo_folder_name), mPhotoTakenName, bitmap
            )
            mPhotoImage!!.setImageBitmap(
                CellarStorageUtils.getBitmapFromInternalStorage(
                    filesDir,
                    resources.getString(R.string.photo_folder_name),
                    mPhotoTakenName
                )
            )
            sPhotoHasChanged = PHOTO_IS_NEW

            //Set focus on image
            mPhotoImage!!.requestFocus()
        }

        //Camera use request
        if (requestCode == REQUEST_CAMERA_USE && resultCode == RESULT_OK) {
            //Case Kitkat
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                mPhotoImage!!.setImageBitmap(
                    CellarStorageUtils.getBitmapFromInternalStorage(
                        getExternalFilesDir("image/*"),
                        resources.getString(R.string.photo_folder_name),
                        mPhotoTakenName
                    )
                )
            } else {
                mPhotoImage!!.setImageBitmap(
                    CellarStorageUtils.getBitmapFromInternalStorage(
                        filesDir,
                        resources.getString(R.string.photo_folder_name),
                        mPhotoTakenName
                    )
                )
            }
            sPhotoHasChanged = PHOTO_IS_NEW

            //Set focus on image
            mPhotoImage!!.requestFocus()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        //delete temporary files

        //Case Kitkat
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            CellarStorageUtils.deleteFileFromInternalStorage(
                getExternalFilesDir("image/*"),
                resources.getString(R.string.photo_folder_name),
                mPhotoTakenName
            )
        } else {
            CellarStorageUtils.deleteFileFromInternalStorage(
                filesDir,
                resources.getString(R.string.photo_folder_name), mPhotoTakenName
            )
        }

        //Save datas
        saveDatas()
    }

    //    **********************************************************************************************
    //    Configuration subs
    //    **********************************************************************************************
    private fun configureToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun initializeFields() {
        //initialize cellar editor fields
        updateAutoCompletionTextViewAdapter()
        sPhotoHasChanged = PHOTO_HAS_NOT_CHANGED
        if (sCellPosition == -1) {
            mTitleText!!.setText(R.string.activity_edit_cellar_new_entry)
            mAppellationACTV!!.setText("")
            mDomainACTV!!.setText("")
            mCuveeACTV!!.setText("")
            mRedWineRadio!!.isChecked = true
            mVintageEdit!!.setText("")
            mCapacitySpinner!!.setSelection(0)
            mBottleCommentEdit!!.setText("")
            mOriginEdit!!.setText("")
            mStockEdit!!.setText("1")
            mCellarCommentEdit!!.setText("")
            mPhotoImage!!.setImageDrawable(resources.getDrawable(R.drawable.photo_frame))
            //set focus on first edittext
            mAppellationACTV!!.requestFocus()
        } else {
            mTitleText!!.setText(R.string.edit_cellar_activity_update_entry)
            val cell = Cellar.cellarPool[sCurrentCellarIndex].cellList[sCellPosition]
            val bottle = cell.bottle
            mAppellationACTV!!.setText(bottle.appellation)
            mDomainACTV!!.setText(bottle.domain)
            mCuveeACTV!!.setText(bottle.cuvee)
            when (bottle.type) {
                "0" -> mRedWineRadio!!.isChecked = true
                "1" -> mWhiteWineRadio!!.isChecked = true
                "2" -> mPinkWineRadio!!.isChecked = true
                else -> mRedWineRadio!!.isChecked = true
            }
            mVintageEdit!!.setText(bottle.vintage)
            var i = 0
            val bottleCapacity = bottle.capacity
            while (sBottleCapacityList[i].compareTo(bottleCapacity.toString() + "") != 0) i++
            mCapacitySpinner!!.setSelection(i)
            mBottleCommentEdit!!.setText(bottle.bottleComment)
            mOriginEdit!!.setText(cell.origin)
            mStockEdit!!.setText(Integer.toString(cell.stock))
            mCellarCommentEdit!!.setText(cell.cellComment)
            if (bottle.photoName.compareTo("") != 0) mPhotoImage!!.setImageBitmap(
                CellarStorageUtils.getBitmapFromInternalStorage(
                    filesDir,
                    resources.getString(R.string.photo_folder_name),
                    bottle.photoName + mPhotoThumbnailSuffix
                )
            ) else mPhotoImage!!.setImageDrawable(resources.getDrawable(R.drawable.photo_frame))
        }
    }

    //    **********************************************************************************************
    //    Working subs
    //    **********************************************************************************************
    private fun createEntry() {
        //collect all the datas submitted and create a new bottle in the current cellar

        //Collect the datas
        val appellation = replaceForbiddenCharacters(this, mAppellationACTV!!.text.toString())
        val domain = replaceForbiddenCharacters(this, mDomainACTV!!.text.toString())
        val cuvee = replaceForbiddenCharacters(this, mCuveeACTV!!.text.toString())
        var type = ""
        if (mRedWineRadio!!.isChecked) type = "0"
        if (mWhiteWineRadio!!.isChecked) type = "1"
        if (mPinkWineRadio!!.isChecked) type = "2"
        val vintage = replaceForbiddenCharacters(this, mVintageEdit!!.text.toString())
        val capacityIndex = mCapacitySpinner!!.selectedItemPosition
        var bottleName = getString(R.string.bottle_standard)
        var capacity = 0.75
        when (capacityIndex) {
            0 -> {
                bottleName = getString(R.string.bottle_standard)
                capacity = 0.75
            }
            1 -> {
                bottleName = getString(R.string.bottle_piccolo)
                capacity = 0.2
            }
            2 -> {
                bottleName = getString(R.string.bottle_quarter)
                capacity = 0.25
            }
            3 -> {
                bottleName = getString(R.string.bottle_demi)
                capacity = 0.375
            }
            4 -> {
                bottleName = getString(R.string.bottle_pot)
                capacity = 0.46
            }
            5 -> {
                bottleName = getString(R.string.bottle_magnum)
                capacity = 1.5
            }
            6 -> {
                bottleName = getString(R.string.bottle_marie_jeanne)
                capacity = 3.0
            }
            7 -> {
                bottleName = getString(R.string.bottle_rehoboam)
                capacity = 4.5
            }
            8 -> {
                bottleName = getString(R.string.bottle_jeroboam)
                capacity = 5.0
            }
            9 -> {
                bottleName = getString(R.string.bottle_methuselah)
                capacity = 6.0
            }
            10 -> {
                bottleName = getString(R.string.bottle_salmanazar)
                capacity = 9.0
            }
            11 -> {
                bottleName = getString(R.string.bottle_balthazar)
                capacity = 12.0
            }
            12 -> {
                bottleName = getString(R.string.bottle_nebuchadnezzar)
                capacity = 15.0
            }
            13 -> {
                bottleName = getString(R.string.bottle_melchior)
                capacity = 18.0
            }
        }
        val bottleComment = replaceForbiddenCharacters(this, mBottleCommentEdit!!.text.toString())
        val origin = replaceForbiddenCharacters(this, mOriginEdit!!.text.toString())
        val stock = mStockEdit!!.text.toString().toInt()
        val cellarComment = replaceForbiddenCharacters(this, mCellarCommentEdit!!.text.toString())

        //handle the new photo
        lateinit var photoName: String
        Log.i(TAG, sPhotoHasChanged)
        if (sPhotoHasChanged.compareTo(PHOTO_IS_NEW) == 0) {
            val bitmap = (mPhotoImage!!.drawable as BitmapDrawable).bitmap
            photoName = CellarStorageUtils.saveBottleImageToInternalStorage(
                filesDir,
                resources.getString(R.string.photo_folder_name),
                bitmap
            )

            //create a thumbnail
            val thumbnail = CellarStorageUtils.decodeSampledBitmapFromFile(
                filesDir, resources.getString(R.string.photo_folder_name), photoName,
                resources.getDimension(R.dimen.recyclerview_cellar_row_photo_width).toInt(),
                resources.getDimension(R.dimen.recyclerview_cellar_row_photo_height).toInt()
            )
            CellarStorageUtils.saveBitmapToInternalStorage(
                filesDir,
                resources.getString(R.string.photo_folder_name),
                photoName + mPhotoThumbnailSuffix,
                thumbnail
            )
        }

        //Create new objects if it is a creation
        if (sCellPosition == -1) {
            val bottle = Bottle(
                appellation,
                domain,
                cuvee,
                type,
                vintage,
                bottleName,
                capacity,
                bottleComment,
                photoName,
                true
            )
            val cell = Cell(bottle, origin, stock, cellarComment, true)
            Cellar.cellarPool[sCurrentCellarIndex].cellList.add(cell)
            initializeFields()
            Toast.makeText(this, R.string.edit_cellar_activity_entry_saved, Toast.LENGTH_SHORT)
                .show()
            mAppellationACTV!!.requestFocus()
        } else {
            //modify the existing entry
            val cell = Cellar.cellarPool[sCurrentCellarIndex].cellList[sCellPosition]
            val bottle = cell.bottle
            bottle.appellation = appellation
            bottle.domain = domain
            bottle.cuvee = cuvee
            bottle.type = type
            bottle.vintage = vintage
            bottle.bottleName = bottleName
            bottle.capacity = capacity
            bottle.bottleComment = bottleComment
            if (sPhotoHasChanged.compareTo(PHOTO_HAS_NOT_CHANGED) != 0) bottle.photoName = photoName
            cell.origin = origin
            cell.stock = stock
            cell.cellComment = cellarComment
            Toast.makeText(this, R.string.edit_cellar_activity_entry_updated, Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private fun deleteCellarEntry() {
        //delete entry when delete is clicked
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.edit_cellar_activity_warning)
            .setMessage(R.string.edit_cellar_activity_sure_delete_entry)
            .setPositiveButton("OK") { dialog, which ->
                Cellar.cellarPool[sCurrentCellarIndex].cellList[sCellPosition].removeCell()
                Toast.makeText(
                    applicationContext,
                    R.string.edit_cellar_activity_entry_deleted,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            .setNegativeButton("CANCEL") { dialog, which -> finish() }
            .create()
            .show()
    }

    private fun deleteBottlePhoto() {
        //delete the photo when delete photo is clicked
        CellarStorageUtils.deleteFileFromInternalStorage(
            filesDir,
            resources.getString(R.string.photo_folder_name),
            Cellar.cellarPool[sCurrentCellarIndex].cellList[sCellPosition].bottle.photoName
        )
        CellarStorageUtils.deleteFileFromInternalStorage(
            filesDir,
            resources.getString(R.string.photo_folder_name),
            Cellar.cellarPool[sCurrentCellarIndex].cellList[sCellPosition].bottle.photoName + mPhotoThumbnailSuffix
        )
        mPhotoImage!!.setImageDrawable(resources.getDrawable(R.drawable.photo_frame))
        Cellar.cellarPool[sCurrentCellarIndex].cellList[sCellPosition].bottle.photoName = ""
        sPhotoHasChanged = PHOTO_WAS_DELETED
    }//return list of already submitted appellations

    //    **********************************************************************************************
    //    Autocompletion subs
    //    **********************************************************************************************
    private val appellationAutoCompletionList: ArrayList<String>
        private get() {
            //return list of already submitted appellations
            val currentStringList = ArrayList<String>()
            var currentString: String
            for (i in 0 until Bottle.numberOfReferences) {
                currentString = Bottle.bottleCatalog[i].appellation
                if (currentStringList.indexOf(currentString) < 0) currentStringList.add(
                    currentString
                )
            }
            Collections.sort(currentStringList)
            return currentStringList
        }

    //return list of already submitted appellations
    private val domainAutoCompletionList: ArrayList<String>
        private get() {
            //return list of already submitted appellations
            val currentStringList = ArrayList<String>()
            var currentString: String
            for (i in 0 until Bottle.numberOfReferences) {
                currentString = Bottle.bottleCatalog[i].domain
                if (currentStringList.indexOf(currentString) < 0) currentStringList.add(
                    currentString
                )
            }
            Collections.sort(currentStringList)
            return currentStringList
        }

    //return list of already submitted appellations
    private val cuveeAutoCompletionList: ArrayList<String>
        private get() {
            //return list of already submitted appellations
            val currentStringList = ArrayList<String>()
            var currentString: String
            for (i in 0 until Bottle.numberOfReferences) {
                currentString = Bottle.bottleCatalog[i].cuvee
                if (currentStringList.indexOf(currentString) < 0) currentStringList.add(
                    currentString
                )
            }
            Collections.sort(currentStringList)
            return currentStringList
        }

    private fun updateAutoCompletionTextViewAdapter() {
        //put on autocomplete lists
        val appellationAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            appellationAutoCompletionList
        )
        mAppellationACTV!!.setAdapter(appellationAdapter)
        val domainAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            domainAutoCompletionList
        )
        mDomainACTV!!.setAdapter(domainAdapter)
        val cuveeAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cuveeAutoCompletionList)
        mCuveeACTV!!.setAdapter(cuveeAdapter)
    }

    //    **********************************************************************************************
    //    Sending intents
    //    **********************************************************************************************
    private fun sendGetPhotoPathNameIntent() {
        //get the pathname of a photo chosen by user
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PHOTO_PATHNAME)
    }

    private fun sendTakePhotoIntent() {
        //ask for taking a photo and put it in predefined Uri
        //that Uri has to be deleted after usage
        val photo: File?
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //Case Kitkat
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            photo = CellarStorageUtils.createOrGetFile(
                getExternalFilesDir("image/*"),
                resources.getString(R.string.photo_folder_name),
                mPhotoTakenName
            )
            mPhotoTakenUri = Uri.fromFile(photo)
        } else {
            photo = CellarStorageUtils.createOrGetFile(
                filesDir,
                resources.getString(R.string.photo_folder_name),
                mPhotoTakenName
            )
            mPhotoTakenUri = FileProvider.getUriForFile(
                applicationContext,
                resources.getString(R.string.file_provider_authority),
                photo
            )
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoTakenUri)
        startActivityForResult(intent, REQUEST_CAMERA_USE)
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

    companion object {
        //Debug values
        private const val TAG = "EditCellarActivity"

        //List of bottle names
        //    private static ArrayList<String> sBottleNameList = new ArrayList<>(Arrays.asList("Standard","Piccolo","Quarter","Demi","Pot","Magnum","Marie-Jeanne","Rehoboam","Jeroboam","Methuselah","Salmanazar","Balthazar","Nebuchadnezzar","Melchior"));
        private val sBottleNameList = ArrayList<String>()
        private val sBottleCapacityList = ArrayList<String>()
        private val sSpinnerList = ArrayList<String>()

        //    **********************************************************************************************
        //    Intent request tags
        //    **********************************************************************************************
        private const val REQUEST_PHOTO_PATHNAME = 200
        private const val REQUEST_CAMERA_USE = 201

        //To always know the cellarindex
        private var sCurrentCellarIndex = 0
        private var sCellPosition = 0

        //To know whether a photo was chosen
        private const val PHOTO_IS_NEW = "photo is new"
        private const val PHOTO_HAS_NOT_CHANGED = "photo has not changed"
        private const val PHOTO_WAS_DELETED = "photo was deleted"
        private var sPhotoHasChanged = PHOTO_HAS_NOT_CHANGED
    }
}