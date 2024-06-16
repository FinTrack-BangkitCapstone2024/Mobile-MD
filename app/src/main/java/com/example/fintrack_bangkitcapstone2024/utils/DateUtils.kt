package com.example.fintrack_bangkitcapstone2024.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return if (date != null) outputFormat.format(date) else inputDate
    }
}