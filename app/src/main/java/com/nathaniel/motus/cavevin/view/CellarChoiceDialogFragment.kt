package com.nathaniel.motus.cavevin.view

import com.nathaniel.motus.cavevin.controller.CellarInputUtils.replaceForbiddenCharacters
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils.getDisplayWidthPx
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils.getBitmapFromInternalStorage
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.view.MyRecyclerViewAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.nathaniel.motus.cavevin.model.Cellar
import com.nathaniel.motus.cavevin.controller.MainActivity
import com.nathaniel.motus.cavevin.view.CellarFragment
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.nathaniel.motus.cavevin.view.BottomBarFragment.onBottomBarClickedListener
import com.nathaniel.motus.cavevin.view.BottomBarFragment
import android.widget.EditText
import com.nathaniel.motus.cavevin.view.EditDialogFragment.onEditDialogClickListener
import android.content.DialogInterface
import com.nathaniel.motus.cavevin.controller.CellarInputUtils
import com.nathaniel.motus.cavevin.view.EditDialogFragment
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import com.nathaniel.motus.cavevin.view.MyViewHolder
import com.nathaniel.motus.cavevin.view.MyRecyclerViewAdapter.onItemClickedListener
import com.nathaniel.motus.cavevin.R.layout
import com.nathaniel.motus.cavevin.R.dimen
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils
import com.nathaniel.motus.cavevin.model.Bottle
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.R.string
import com.nathaniel.motus.cavevin.R.drawable
import android.widget.Spinner
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment.onCellarChoiceFragmentClickListener
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment

/**
 * A simple [Fragment] subclass.
 */
class CellarChoiceDialogFragment : DialogFragment() {
    private lateinit var mCellarChoiceSpinner: Spinner
    private var mCallBack: onCellarChoiceFragmentClickListener? = null
    private var mDialogTitle: String? = null

    interface onCellarChoiceFragmentClickListener {
        fun onCellarChoiceFragmentClick(position: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val adb = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(layout.fragment_cellar_choice_dialog, null)
        mCellarChoiceSpinner = view.findViewById(R.id.fragment_cellar_choice_dialog_spinner)
        initializeCellarChoiceSpinner()
        adb.setView(view)
        adb.setTitle(mDialogTitle)
        adb.setPositiveButton("OK") { dialog, which ->
            val position = mCellarChoiceSpinner.getSelectedItemPosition()
            mCallBack!!.onCellarChoiceFragmentClick(position)
        }
        adb.setNegativeButton("Cancel") { dialog, which -> dialog?.dismiss() }
        return adb.create()
    }

    private fun initializeCellarChoiceSpinner() {
        val cellarNameListAdapter = ArrayAdapter<String>(
            activity!!.applicationContext, android.R.layout.simple_spinner_item
        )
        var i = 0
        while (i < Cellar.cellarPool.size) {
            cellarNameListAdapter.add(Cellar.cellarPool[i].cellarName)
            i++
        }
        cellarNameListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCellarChoiceSpinner!!.adapter = cellarNameListAdapter
        mCellarChoiceSpinner!!.setSelection(MainActivity.currentCellarIndex)
        createCallBackToParentActivity()
    }

    private fun createCallBackToParentActivity() {
        mCallBack = activity as onCellarChoiceFragmentClickListener?
    }

    companion object {
        fun newInstance(dialogTitle: String?): CellarChoiceDialogFragment {
            val dialogFragment = CellarChoiceDialogFragment()
            dialogFragment.mDialogTitle = dialogTitle
            return dialogFragment
        }
    }
}