package com.nathaniel.motus.cavevin.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
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

    private val addButton = ImageButton(context).apply {
        setImageResource(R.drawable.outline_add_black_24dp)
        setOnClickListener {
            if (!isLocked) {
                stock++
                notifyObservers()
            }
        }
    }

    private val removeButton = ImageButton(context).apply {
        setImageResource(R.drawable.outline_remove_black_24dp)
        setOnClickListener {
            if (stock > 0 && !isLocked) {
                stock--
                notifyObservers()
            }
        }
    }

    private val stockText = TextView(context).apply {
        text = stock.toString()
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