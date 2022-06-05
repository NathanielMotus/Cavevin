package com.nathaniel.motus.cavevin.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.nathaniel.motus.cavevin.R

class WineView(context:Context,attrs:AttributeSet?):androidx.appcompat.widget.AppCompatImageView(context,attrs) {
    constructor(context: Context):this(context,null)

    var wineColor:String?="red"
        set(value) {
            field = value
            setColor()
            invalidate()
        }

    var wineStillness:String?="still"
        set(value) {
            field = value
            setImage()
            invalidate()
        }

    private fun setImage(){
        when(wineStillness){
            "sparkling"->setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_wine_bar_full_sparkling_48))
            else->setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_wine_bar_full_48))
        }
    }

    private fun setColor(){
        when(wineColor){
            "white"->imageTintList= ColorStateList.valueOf(ContextCompat.getColor(context,R.color.whiteWine))
            "pink"->imageTintList= ColorStateList.valueOf(ContextCompat.getColor(context,R.color.pinkWine))
            else->imageTintList= ColorStateList.valueOf(ContextCompat.getColor(context,R.color.redWine))
        }
    }
}