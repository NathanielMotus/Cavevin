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
import android.view.View
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