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
import android.app.Activity
import android.content.*
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment
import com.nathaniel.motus.cavevin.view.EditDialogFragment
import com.nathaniel.motus.cavevin.model.Bottle
import com.nathaniel.motus.cavevin.controller.EditCellarActivity
import kotlin.Throws
import android.os.ParcelFileDescriptor
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Point
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

object CellarPictureUtils {
    //    **********************************************************************************************
    //    Working subs
    //    **********************************************************************************************
    fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight
                && halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(
        res: Resources?,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    @JvmStatic
    fun getDisplayWidthPx(context: Context): Int {
        //return diplay width in px
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        return size.x
    }

    fun rotate(bitmap: Bitmap?, degree: Int): Bitmap {
        val w = bitmap!!.width
        val h = bitmap.height
        val mtx = Matrix()
        mtx.setRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
    }

    fun getUriFromFileProvider(context: Context, photoName: String): Uri {
        //return a Uri to share a picture to other app
        val photoPathName = "content://" + context.packageName + "/" +
                context.resources.getString(R.string.photo_folder_name) + "/" +
                photoName
        return Uri.parse(photoPathName)
    }
}