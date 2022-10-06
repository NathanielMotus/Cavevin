package com.nathaniel.motus.cavevin.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.nathaniel.motus.cavevin.R

class WineView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    constructor(context: Context) : this(context, null)

    var wineColor: String? = "red"
        set(value) {
            field = value
            setColor()
            invalidate()
        }

    var wineStillness: String? = "still"
        set(value) {
            field = value
            setImage()
            invalidate()
        }

    init {
        if (attrs != null) {
            wineColor = context.obtainStyledAttributes(attrs, R.styleable.WineView)
                .getString(R.styleable.WineView_wineColor)
            wineStillness = context.obtainStyledAttributes(attrs, R.styleable.WineView)
                .getString(R.styleable.WineView_wineStillness)
        }
    }

    private fun setImage() {
        when (wineStillness) {
            "sparkling" -> setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_baseline_wine_bar_full_sparkling_48
                )
            )
            else -> setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_baseline_wine_bar_full_48
                )
            )
        }
    }

    private fun setColor() {
        imageTintList = when (wineColor) {
            "white" -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.whiteWine))
            "pink" -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.pinkWine))
            else -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.redWine))
        }
    }
}