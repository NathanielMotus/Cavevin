package com.nathaniel.motus.cavevin.view

import com.nathaniel.motus.cavevin.controller.CellarInputUtils.replaceForbiddenCharacters
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils.getDisplayWidthPx
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils.getBitmapFromInternalStorage
import androidx.recyclerview.widget.RecyclerView
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
import com.nathaniel.motus.cavevin.view.EditDialogFragment.onEditDialogClickListener
import android.content.DialogInterface
import com.nathaniel.motus.cavevin.controller.CellarInputUtils
import com.nathaniel.motus.cavevin.view.EditDialogFragment
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.*
import com.nathaniel.motus.cavevin.view.MyViewHolder
import com.nathaniel.motus.cavevin.view.MyRecyclerViewAdapter.onItemClickedListener
import com.nathaniel.motus.cavevin.R.layout
import com.nathaniel.motus.cavevin.R.dimen
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils
import com.nathaniel.motus.cavevin.model.Bottle
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.R.string
import com.nathaniel.motus.cavevin.R.drawable
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment.onCellarChoiceFragmentClickListener
import androidx.fragment.app.Fragment
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment
import java.lang.ClassCastException

/**
 * A simple [Fragment] subclass.
 */
class BottomBarFragment : Fragment(), View.OnClickListener {

    //Callback
    var mCallBack: onBottomBarClickedListener? = null
    lateinit var mAllWinesButton:ImageView
    lateinit var mRedButton:ImageView
    lateinit var mWhiteButton:ImageView
    lateinit var mPinkButton:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val result = inflater.inflate(layout.fragment_bottom_bar, container, false)

//        Inflate buttons
        mAllWinesButton= result.findViewById(R.id.bottombar_all_wines_button)
        mRedButton = result.findViewById(R.id.bottombar_red_button)
        mWhiteButton = result.findViewById(R.id.bottombar_white_button)
        mPinkButton = result.findViewById(R.id.bottombar_pink_button)

//        Activate the last chosen
        val currentTypeFilter = activity!!.getPreferences(Context.MODE_PRIVATE).getString(
            CURRENT_TYPE_FILTER, "all"
        )
        when (currentTypeFilter) {
            "red" -> mRedButton.setBackgroundColor(result.context.resources.getColor(R.color.colorAccent))
            "white" -> mWhiteButton.setBackgroundColor(result.context.resources.getColor(R.color.colorAccent))
            "pink" -> mPinkButton.setBackgroundColor(result.context.resources.getColor(R.color.colorAccent))
            else -> mAllWinesButton.setBackgroundColor(result.context.resources.getColor(R.color.colorAccent))
        }

//        Put on clicklistener
        mAllWinesButton.setOnClickListener(this)
        mRedButton.setOnClickListener(this)
        mWhiteButton.setOnClickListener(this)
        mPinkButton.setOnClickListener(this)

//        create callback
        createCallBackToParentActivity()
        return result
    }

    override fun onClick(v: View) {
        val buttonTag = v.tag.toString()
        //change the colors
        mAllWinesButton!!.setBackgroundColor(v.context.resources.getColor(R.color.colorPrimary))
        mRedButton!!.setBackgroundColor(v.context.resources.getColor(R.color.colorPrimary))
        mWhiteButton!!.setBackgroundColor(v.context.resources.getColor(R.color.colorPrimary))
        mPinkButton!!.setBackgroundColor(v.context.resources.getColor(R.color.colorPrimary))
        v.setBackgroundColor(v.context.resources.getColor(R.color.colorAccent))
        mCallBack!!.onBottomBarItemClicked(v, buttonTag)
    }

    //    **********************************************************************************************
    //    Callback subs
    //    **********************************************************************************************
    interface onBottomBarClickedListener {
        fun onBottomBarItemClicked(v: View?, buttonTag: String?)
    }

    private fun createCallBackToParentActivity() {
        mCallBack = try {
            activity as onBottomBarClickedListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("$e must implement onItemClickedListener")
        }
    }

    companion object {
        //Shared preferences tags
        private const val CURRENT_CELLAR_INDEX = "Current cellar index"
        private const val CURRENT_TYPE_FILTER = "Current type filter"
        private const val CURRENT_SORT_OPTION = "Current sort options"
    }
}