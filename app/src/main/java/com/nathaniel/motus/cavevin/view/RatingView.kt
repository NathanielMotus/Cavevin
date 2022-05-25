package com.nathaniel.motus.cavevin.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.nathaniel.motus.cavevin.R

class RatingView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    constructor(context: Context) : this(context, null)

    private val imageViews: MutableList<ImageView> = mutableListOf()
    var rating = 0
        set(value) {
            field = value
            updateImageViews()
            invalidate()
            requestLayout()
        }
    var displayMode = VIEW_MODE
        set(value) {
            field = value
            updateImageViews()
            invalidate()
            requestLayout()
        }

    init {
        createImageViews()
        addImageViews()
        context.obtainStyledAttributes(attrs, R.styleable.RatingView).apply {
            rating = getInt(R.styleable.RatingView_rating, 0)
            displayMode = getInt(R.styleable.RatingView_displayMode, 0)
            recycle()
        }

        orientation = HORIZONTAL
        updateImageViews()
    }

    private fun createImageViews() {
        for (i in 0..6) {
            imageViews.add(ImageView(context))
            imageViews[i].adjustViewBounds = true
        }

        setImage(
            0,
            AppCompatResources.getDrawable(context,R.drawable.outline_clear_black_24dp)!!,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )

        setImage(
            6,
            AppCompatResources.getDrawable(context,R.drawable.outline_edit_black_24dp)!!,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
    }

    private fun setImage(index: Int, drawable: Drawable, color: Int) {
        imageViews[index].apply {
            imageTintList = ColorStateList.valueOf(color)
            setImageDrawable(drawable)
        }
    }

    private fun addImageViews() {
        imageViews.forEach {
            addView(
                it,
                LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            )
        }
    }

    private fun updateImageViews() {
        imageViews[0].visibility = GONE
        imageViews[6].visibility = GONE

        when (displayMode) {
            EDITABLE_MODE -> imageViews[6].visibility = VISIBLE
            EDIT_MODE -> imageViews[0].visibility = VISIBLE
        }

        for (i in 1..rating)
            setImage(
                i,
                AppCompatResources.getDrawable(context,R.drawable.outline_star_black_24dp)!!,
                ContextCompat.getColor(context, android.R.color.holo_orange_light)
            )

        for (i in rating+1..5)
            setImage(
                i,
                AppCompatResources.getDrawable(context,R.drawable.outline_star_outline_black_24dp)!!,
                ContextCompat.getColor(context,android.R.color.holo_orange_light)
            )
    }

    companion object {
        const val VIEW_MODE = 0
        const val EDITABLE_MODE = 1
        const val EDIT_MODE = 2
    }
}