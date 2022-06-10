package com.nathaniel.motus.cavevin.view

import com.nathaniel.motus.cavevin.controller.CellarPictureUtils.getDisplayWidthPx
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils.getBitmapFromInternalStorage
import androidx.recyclerview.widget.RecyclerView
import com.nathaniel.motus.cavevin.R
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nathaniel.motus.cavevin.model.Cellar
import com.nathaniel.motus.cavevin.controller.MainActivity
import android.app.Activity
import android.content.Context
import android.view.View
import com.nathaniel.motus.cavevin.R.layout
import com.nathaniel.motus.cavevin.R.dimen
import com.nathaniel.motus.cavevin.R.string
import com.nathaniel.motus.cavevin.R.drawable
import com.nathaniel.motus.cavevin.model.Cell
import java.lang.ClassCastException

class MyRecyclerViewAdapter(
    private val mCellar: Cellar,
    private val mContext: Context,
    private val mActivity: Activity?
) : RecyclerView.Adapter<MyViewHolder>() {
    private var mCallback: onItemClickedListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(layout.recyclerview_cellar_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

//                Get the current cellar position before callback (position is filtered cellar position)
        val actualPosition: Int =
            Cellar.cellarPool[MainActivity.currentCellarIndex].cellList.indexOf(
                mCellar.cellList[position]
            )

        //calculate width of multilines textviews
        val typeWidthPx =
            mActivity!!.resources.getDimension(dimen.recyclerview_cellar_row_type_width)
                .toInt()
        val photoImageWidthPx =
            mActivity.resources.getDimension(dimen.recyclerview_cellar_row_photo_width)
                .toInt()
        val vintageWidthPx =
            mActivity.resources.getDimension(dimen.recyclerview_cellar_row_vintage_width)
                .toInt()
        val buttonsWidthPx =
            mActivity.resources.getDimension(dimen.recyclerview_cellar_row_buttons_width)
                .toInt()
        val appellationWidth =
            getDisplayWidthPx(mContext) - typeWidthPx - photoImageWidthPx - vintageWidthPx - buttonsWidthPx
        val domainWidth =
            getDisplayWidthPx(mContext) - typeWidthPx - photoImageWidthPx - buttonsWidthPx
        val cuveeWidth =
            getDisplayWidthPx(mContext) - typeWidthPx - photoImageWidthPx - buttonsWidthPx
        holder.appellation.layoutParams.width = appellationWidth
        holder.domain.layoutParams.width = domainWidth
        holder.cuvee.layoutParams.width = cuveeWidth
        val cell = mCellar.cellList[position]!!
        val bottle = cell.bottle
        val color = bottle.type
        when (color) {
            "1" -> holder.type.setBackgroundColor(mContext.resources.getColor(R.color.whiteWine))
            "2" -> holder.type.setBackgroundColor(mContext.resources.getColor(R.color.pinkWine))
            else -> holder.type.setBackgroundColor(mContext.resources.getColor(R.color.redWine))
        }
        holder.appellation.text = bottle.appellation
        holder.domain.text = bottle.domain
        holder.cuvee.text = bottle.cuvee
        holder.vintage.text = bottle.vintage
        holder.bottleName.text = bottle.bottleName
        holder.capacity.text = java.lang.Double.toString(bottle.capacity) + " L"
        holder.quantity.text = Integer.toString(cell.stock)
        holder.origin.text = cell.origin
        holder.bottleComment.text = bottle.bottleComment
        holder.cellarComment.text = cell.cellComment

        //load photo image
        val photoName = bottle.photoName
        if (photoName.compareTo("") != 0) holder.photoImage.setImageBitmap(
            getBitmapFromInternalStorage(
                mContext.filesDir,
                mContext.resources.getString(string.photo_folder_name),
                photoName + mContext.resources.getString(string.photo_thumbnail_suffix)
            )
        ) else holder.photoImage.setImageDrawable(mContext.resources.getDrawable(drawable.photo_frame))

        //Expand or collapse, according to flag isExpanded
        if (mCellar.cellList[position]!!.isExpanded) expandCard(holder) else collapseCard(holder)
        createCallBackToParentActivity()

        //Click listeners
        holder.buttonEdit.setOnClickListener { v -> mCallback!!.onItemClicked(v, actualPosition) }
        holder.buttonPlus.setOnClickListener {
            val cell: Cell =
                Cellar.cellarPool[MainActivity.currentCellarIndex].cellList[actualPosition]!!
            cell.stock = cell.stock + 1
            holder.quantity.text = Integer.toString(cell.stock)
        }
        holder.buttonMinus.setOnClickListener {
            val cell: Cell =
                Cellar.cellarPool[MainActivity.currentCellarIndex].cellList[actualPosition]!!
            if (cell.stock > 0) cell.stock = cell.stock - 1
            holder.quantity.text = Integer.toString(cell.stock)
        }
        holder.photoImage.setOnClickListener { v -> mCallback!!.onItemClicked(v, actualPosition) }
        holder.buttonCollapse.setOnClickListener {
            if (mCellar.cellList[position]!!.isExpanded) {
                collapseCard(holder)
                mCellar.cellList[position]!!.isExpanded = false
            } else {
                expandCard(holder)
                mCellar.cellList[position]!!.isExpanded = true
            }
        }
    }

    override fun getItemCount(): Int {
        return mCellar.cellList.size
    }

    //    **********************************************************************************************
    //    Callback subs
    //    **********************************************************************************************
    interface onItemClickedListener {
        fun onItemClicked(view: View?, position: Int)
    }

    private fun createCallBackToParentActivity() {
        mCallback = try {
            mActivity as onItemClickedListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("$e must implement onItemClickedListener")
        }
    }

    //    **********************************************************************************************
    //    Display subs
    //    **********************************************************************************************
    private fun collapseCard(holder: MyViewHolder) {
        //Collapse a card
        holder.originTitle.visibility = View.GONE
        holder.origin.visibility = View.GONE
        holder.bottleCommentTitle.visibility = View.GONE
        holder.bottleComment.visibility = View.GONE
        holder.cellarCommentTitle.visibility = View.GONE
        holder.cellarComment.visibility = View.GONE
        holder.buttonEdit.visibility = View.GONE
        holder.buttonCollapse.setImageResource(drawable.baseline_keyboard_arrow_down_white_18dp)
    }

    private fun expandCard(holder: MyViewHolder) {
        //Expand a card
        holder.originTitle.visibility = View.VISIBLE
        holder.origin.visibility = View.VISIBLE
        holder.bottleCommentTitle.visibility = View.VISIBLE
        holder.bottleComment.visibility = View.VISIBLE
        holder.cellarCommentTitle.visibility = View.VISIBLE
        holder.cellarComment.visibility = View.VISIBLE
        holder.buttonEdit.visibility = View.VISIBLE
        holder.buttonCollapse.setImageResource(drawable.baseline_keyboard_arrow_up_white_18dp)
    }
}