package com.example.fintrack_bangkitcapstone2024.utils

import java.text.NumberFormat
import java.util.Locale

 object CurrencyUtil {
     fun Double.toRupiah(): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(this).toString()
    }

     fun Int.toRupiah(): String {
        return this.toDouble().toRupiah()
    }
}