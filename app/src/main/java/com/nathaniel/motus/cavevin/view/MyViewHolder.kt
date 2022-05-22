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
import com.nathaniel.motus.cavevin.view.CellarChoiceDialogFragment

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var appellation: TextView
    var domain: TextView
    var cuvee: TextView
    var type: TextView
    var vintage: TextView
    var bottleName: TextView
    var capacity: TextView
    var quantity: TextView
    var originTitle: TextView
    var origin: TextView
    var bottleCommentTitle: TextView
    var bottleComment: TextView
    var cellarCommentTitle: TextView
    var cellarComment: TextView
    var buttonPlus: Button
    var buttonMinus: Button
    var buttonCollapse: ImageView
    var buttonEdit: ImageView
    var photoImage: ImageView

    init {
        appellation = view.findViewById(R.id.recycler_cellar_row_appellation_text)
        domain = view.findViewById(R.id.recycler_cellar_row_domain_text)
        cuvee = view.findViewById(R.id.recycler_cellar_row_cuvee_text)
        type = view.findViewById(R.id.recycler_cellar_row_type_text)
        vintage = view.findViewById(R.id.recycler_cellar_row_vintage_text)
        bottleName = view.findViewById(R.id.recycler_cellar_row_bottle_name_text)
        capacity = view.findViewById(R.id.recycler_cellar_row_capacity_text)
        quantity = view.findViewById(R.id.recycler_cellar_row_quantity_text)
        buttonPlus = view.findViewById(R.id.recycler_cellar_row_plus_button)
        buttonMinus = view.findViewById(R.id.recycler_cellar_row_minus_button)
        origin = view.findViewById(R.id.recycler_cellar_row_origin_text)
        originTitle = view.findViewById(R.id.recycler_cellar_row_origin_title_text)
        bottleCommentTitle = view.findViewById(R.id.recycler_cellar_row_bottle_comment_title_text)
        bottleComment = view.findViewById(R.id.recycler_cellar_row_bottle_comment_text)
        cellarCommentTitle = view.findViewById(R.id.recycler_cellar_row_cellar_comment_title_text)
        cellarComment = view.findViewById(R.id.recycler_cellar_row_cellar_comment_text)
        buttonCollapse = view.findViewById(R.id.recycler_cellar_row_collapse_image)
        buttonEdit = view.findViewById(R.id.recycler_cellar_row_edit_image)
        photoImage = view.findViewById(R.id.recycler_cellar_row_photo_image)
    }
}