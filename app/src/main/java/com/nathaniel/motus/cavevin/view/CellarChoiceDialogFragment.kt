package com.nathaniel.motus.cavevin.view

import com.nathaniel.motus.cavevin.R
import android.os.Bundle
import com.nathaniel.motus.cavevin.model.Cellar
import com.nathaniel.motus.cavevin.controller.MainActivity
import android.app.AlertDialog
import android.app.Dialog
import com.nathaniel.motus.cavevin.R.layout
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment

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