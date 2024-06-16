package com.example.fintrack_bangkitcapstone2024.response.Financial

data class  ResponseFinancialData(
    val code: Int,
    val status: String,
    val data: List<FinancialData>
)

data class FinancialData(
    val id: String,
    val jumlah: Int,
    val description: String,
    val tipe: String,
    val tanggal: String,
    val title: String,
    val usaha_id: String
)