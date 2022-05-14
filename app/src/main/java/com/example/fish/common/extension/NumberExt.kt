package com.example.fish.common.extension

import android.os.Build
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

fun Double.formatCurrency(): String {
    val formatter = DecimalFormat.getCurrencyInstance(getIndonesianLocale()) as DecimalFormat
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
        formatter.decimalFormatSymbols = DecimalFormatSymbols().apply {
            currencySymbol = "IDR".plus(" ")
            monetaryDecimalSeparator = ','
            groupingSeparator = '.'
        }
        formatter.format(this).replace(",00", "")
    } else {
        formatter.decimalFormatSymbols = DecimalFormatSymbols().apply {
            currencySymbol = "IDR".plus(" ")
            monetaryDecimalSeparator = ','
            groupingSeparator = '.'
        }
        formatter.format(this)
    }
}

fun getIndonesianLocale() : Locale {
    return Locale("in", "ID")
}

