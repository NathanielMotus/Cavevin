package com.nathaniel.motus.cavevin.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButton.*
import com.google.android.material.textview.MaterialTextView
import com.nathaniel.motus.cavevin.R

class StockView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    constructor(context: Context) : this(context, null)

    var observers: MutableList<Observer> = mutableListOf()
    fun observe(observer: Observer) {
        observers.add(observer)
    }

    private fun notifyObservers() {
        observers.forEach {
            it.notifyNewStock(stock)
        }
    }

    var stock = 2
        set(value) {
            field = value
            stockText.text = stock.toString()
            invalidate()
            requestLayout()
        }

    var isLocked = false

    private val addButton =
        MaterialButton(context, null, R.style.Widget_MaterialComponents_Button).apply {
            setIconResource(R.drawable.outline_add_black_24dp)
            iconPadding = 0
            iconGravity = ICON_GRAVITY_TEXT_START
            gravity = Gravity.CENTER
            setOnClickListener {
                if (!isLocked) {
                    stock++
                    notifyObservers()
                }
            }
        }

    private val removeButton =
        MaterialButton(context, null, R.style.Widget_MaterialComponents_Button).apply {
            setIconResource(R.drawable.outline_remove_black_24dp)
            iconPadding = 0
            iconGravity = ICON_GRAVITY_TEXT_START
            gravity = Gravity.CENTER
            setOnClickListener {
                if (stock > 0 && !isLocked) {
                    stock--
                    notifyObservers()
                }
            }
        }

    private val stockText = MaterialTextView(
        context,
        null,
        R.style.TextAppearance_Material3_TitleLarge
    ).apply {
        text = stock.toString()
        textSize=20f
        gravity = Gravity.CENTER
    }

    init {
        orientation = VERTICAL
        addView(addButton)
        addView(stockText)
        addView(removeButton)
    }

    interface Observer {
        fun notifyNewStock(stock: Int)
    }

}