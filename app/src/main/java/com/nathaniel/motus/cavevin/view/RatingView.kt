package com.nathaniel.motus.cavevin.view

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
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
            if (displayMode == EDITABLE_MODE)
                setMainListener()
            if (displayMode == EDIT_MODE) {
                setClearListener()
                setRatingListeners()
            }

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
            AppCompatResources.getDrawable(context, R.drawable.ic_baseline_clear_48)!!,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )

        setImage(
            6,
            AppCompatResources.getDrawable(context, R.drawable.ic_baseline_edit_48)!!,
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
                AppCompatResources.getDrawable(context, R.drawable.ic_baseline_star_rate_48)!!,
                ContextCompat.getColor(context, android.R.color.holo_orange_light)
            )

        for (i in rating + 1..5)
            setImage(
                i,
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_baseline_star_outline_48
                )!!,
                ContextCompat.getColor(context, android.R.color.holo_orange_light)
            )
    }

    private fun setMainListener() {
        var nestedView: RatingView? = null
        this.setOnClickListener {
            it as RatingView
            nestedView = RatingView(it.context).apply {
                displayMode = EDIT_MODE
                rating = it.rating
            }
            AlertDialog.Builder(it.context)
                .setView(nestedView)
                .setNeutralButton("Cancel") { _, _ -> }
                .setPositiveButton("OK") { _, _ ->
                    it.rating = nestedView!!.rating
                }
                .show()
        }
    }

    private fun setClearListener() {
        this.imageViews[0].setOnClickListener {
            this.rating = 0
        }
    }

    private fun setRatingListeners() {
        for (i in 1..5)
            this.imageViews[i].setOnClickListener {
                this.rating = i
            }
    }


    companion object {
        const val VIEW_MODE = 0
        const val EDITABLE_MODE = 1
        const val EDIT_MODE = 2
    }
}