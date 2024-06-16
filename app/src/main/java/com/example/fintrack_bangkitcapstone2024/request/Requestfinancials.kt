package com.example.fintrack_bangkitcapstone2024.request

data class RequestFinancials(
	val title: String,
	val description: String,
	val jumlah: Int,
	val tanggal: String,
	val tipe: String,
	val usaha_id: String
)