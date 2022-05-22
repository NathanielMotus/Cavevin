package com.nathaniel.motus.cavevin.controller

import com.nathaniel.motus.cavevin.controller.CellarInputUtils.replaceForbiddenCharacters
import androidx.appcompat.app.AppCompatActivity
import com.nathaniel.motus.cavevin.view.MyRecyclerViewAdapter.onItemClickedListener
import com.nathaniel.motus.cavevin.view.BottomBarFragment.onBottomBarClickedListener
import com.google.android.material.navigation.NavigationView
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment.onCellarChoiceFragmentClickListener
import com.nathaniel.motus.cavevin.view.EditDialogFragment.onEditDialogClickListener
import com.nathaniel.motus.cavevin.view.CellarFragment
import com.nathaniel.motus.cavevin.view.BottomBarFragment
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.TextView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.os.Bundle
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.model.Cellar
import com.nathaniel.motus.cavevin.controller.MainActivity
import com.nathaniel.motus.cavevin.model.CellComparator
import androidx.annotation.RequiresApi
import android.os.Build
import android.content.Intent
import android.app.Activity
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils
import android.content.SharedPreferences
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment
import com.nathaniel.motus.cavevin.view.EditDialogFragment
import com.nathaniel.motus.cavevin.model.Bottle
import android.content.DialogInterface
import com.nathaniel.motus.cavevin.controller.EditCellarActivity
import android.content.ContentProvider
import kotlin.Throws
import android.os.ParcelFileDescriptor
import android.content.ContentValues
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.view.WindowManager
import com.nathaniel.motus.cavevin.model.JsonObject
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import com.nathaniel.motus.cavevin.controller.CellarInputUtils
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException

class PhotoProvider : ContentProvider() {
    @Throws(FileNotFoundException::class)
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val privateFile = File(context!!.filesDir, uri.path)
        return ParcelFileDescriptor.open(privateFile, ParcelFileDescriptor.MODE_READ_ONLY)
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}