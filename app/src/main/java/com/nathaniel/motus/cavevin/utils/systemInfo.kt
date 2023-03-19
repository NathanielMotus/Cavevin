package com.nathaniel.motus.cavevin.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.icu.util.Currency
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

fun systemLanguage() = Locale.getDefault().toString()

@RequiresApi(Build.VERSION_CODES.N)
fun defaultCurrencyCode():String = Currency.getInstance(Locale.getDefault()).currencyCode

@RequiresApi(Build.VERSION_CODES.N)
fun availableCurrencies(): Set<Currency> = Currency.getAvailableCurrencies()
