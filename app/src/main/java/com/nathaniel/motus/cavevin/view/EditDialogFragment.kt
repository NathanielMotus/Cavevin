package com.nathaniel.motus.cavevin.view

import com.nathaniel.motus.cavevin.controller.CellarInputUtils.replaceForbiddenCharacters
import android.widget.TextView
import com.nathaniel.motus.cavevin.R
import android.os.Bundle
import android.widget.EditText
import android.app.AlertDialog
import android.app.Dialog
import android.view.View
import com.nathaniel.motus.cavevin.R.layout
import androidx.fragment.app.DialogFragment

/**
 * A simple [Fragment] subclass.
 */
class EditDialogFragment : DialogFragment() {
    //abstract class that shows a dialog with an EditText
    private lateinit var mInputTextEdit: EditText
    private lateinit var mInviteText: TextView
    private var mCallBack: onEditDialogClickListener? = null
    private var mDialogTitle: String? = null
    private var mInvite: String? = null
    private var mPreFilledInput: String? = null

    interface onEditDialogClickListener {
        //        Interface needed for callback
        fun onEditDialogClick(v: View?, inputText: String?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val adb = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(layout.fragment_edit_dialog, null)
        mInputTextEdit = view.findViewById(R.id.fragment_edit_dialog_edit)
        mInviteText = view.findViewById(R.id.fragment_edit_dialog_text)
        mInputTextEdit.setText(mPreFilledInput)
        mInviteText.text = mInvite
        adb.setView(view)
        adb.setTitle(mDialogTitle)
        adb.setPositiveButton("OK") { dialog, which ->
            createCallBackToParentActivity()
            val inputText =
                replaceForbiddenCharacters(view.context, mInputTextEdit.getText().toString())
            mCallBack!!.onEditDialogClick(view, inputText)
        }
        adb.setNegativeButton("CANCEL") { dialog, which -> dialog?.dismiss() }
        return adb.create()
    }

    private fun createCallBackToParentActivity() {
        mCallBack = activity as onEditDialogClickListener?
    }

    companion object {
        fun newInstance(
            dialogTitle: String?,
            invite: String?,
            preFilledInput: String?
        ): EditDialogFragment {
//        Create a new instance
            val edf = EditDialogFragment()
            edf.mDialogTitle = dialogTitle
            edf.mInvite = invite
            edf.mPreFilledInput = preFilledInput
            return edf
        }
    }
}