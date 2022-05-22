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
import androidx.fragment.app.Fragment
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment

/**
 * A simple [Fragment] subclass.
 */
class CellarFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MyRecyclerViewAdapter? = null

    //    **********************************************************************************************
    //    Fragment events
    //    **********************************************************************************************
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val result = inflater.inflate(layout.fragment_cellar, container, false)

        //Instantiate recyclerview
        mRecyclerView = result.findViewById(R.id.fragment_cellar_recycler_view)
        return result
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //update the recyclerview
        //only possible in onActivityCreated as it uses getActivity
        if (Cellar.cellarPool.size != 0) updateCellarRecyclerView(
            Cellar.cellarPool[MainActivity.currentCellarIndex]
                .typeFiltered(MainActivity.currentTypeFilter)
        )
    }

    override fun onResume() {
        super.onResume()

        //update the recyclerview when coming back from cellar edit
        if (Cellar.cellarPool.size != 0) updateCellarRecyclerView(
            Cellar.cellarPool[MainActivity.currentCellarIndex]
                .typeFiltered(MainActivity.currentTypeFilter)
        )

        //Get back recyclerview state
        if (mSavedRecyclerState != null) {
            mRecyclerView!!.layoutManager!!
                .onRestoreInstanceState(mSavedRecyclerState!!.getParcelable(KEY_RECYCLER_STATE))
        }
    }

    override fun onPause() {
        super.onPause()

        //Save recyclerview state
        mSavedRecyclerState = Bundle()
        mSavedRecyclerState!!.putParcelable(
            KEY_RECYCLER_STATE, mRecyclerView!!.layoutManager!!
                .onSaveInstanceState()
        )
    }

    //    **********************************************************************************************
    //    Working subs
    //    **********************************************************************************************
    fun updateCellarRecyclerView(cellar: Cellar) {
        //update cellar RecyclerView
        mAdapter = MyRecyclerViewAdapter(cellar, this.requireActivity().applicationContext, this.activity)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            this.requireActivity().applicationContext
        )
        mRecyclerView!!.layoutManager = mLayoutManager
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
        mRecyclerView!!.adapter = mAdapter
    }

    companion object {
        //    **********************************************************************************************
        //    Valeurs de debug
        //    **********************************************************************************************
        private const val TAG = "CellarFragment"

        //Remember recyclerview state
        private const val KEY_RECYCLER_STATE = "key_recycler_view"
        private var mSavedRecyclerState: Bundle? = null
    }
}