package com.example.mercadogil.utils

import java.text.NumberFormat
import java.util.*

object CurrencyUtils {

    fun convertToCurrency(text: String): Double {
        val cleanString = text.replace("[^\\d]".toRegex(), "")
        return if (cleanString.isNotEmpty()) {
            cleanString.toDouble() / 100
        } else {
            0.0
        }
    }

    fun formatValueAsCurrency(value: Double): String {
        val userLocale = Locale.getDefault()
        val numberFormat = NumberFormat.getCurrencyInstance(userLocale)
        return numberFormat.format(value)
    }
}