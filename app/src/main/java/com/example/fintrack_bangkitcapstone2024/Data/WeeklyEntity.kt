package com.example.fintrack_bangkitcapstone2024.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeeklyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pengeluaran: List<Int>,
    val masukan: List<Int>,
    val day: List<String>
)
