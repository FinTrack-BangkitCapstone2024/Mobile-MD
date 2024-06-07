package com.example.fintrack_bangkitcapstone2024.request

data class RequestFinancials(
	val jumlah: String,
	val description: String,
	val tanggal: String,
	val tipe: String,
	val usahaId: String
)