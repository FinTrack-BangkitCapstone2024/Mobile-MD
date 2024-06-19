package com.example.fintrack_bangkitcapstone2024.response.ResonseReport

import com.google.gson.annotations.SerializedName

data class ResponseDataAnalysis(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class Weekly(

	@field:SerializedName("pengeluaran")
	val pengeluaran: List<Int>,

	@field:SerializedName("masukan")
	val masukan: List<Int>,

	@field:SerializedName("day")
	val day: List<String>
)

data class Data(

	@field:SerializedName("monthly")
	val monthly: Monthly,

	@field:SerializedName("yearly")
	val yearly: Yearly,

	@field:SerializedName("weekly")
	val weekly: Weekly
)

data class Yearly(

	@field:SerializedName("bulan")
	val bulan	: List<String>,

	@field:SerializedName("pengeluaran")
	val pengeluaran: List<Int>,

	@field:SerializedName("masukan")
	val masukan: List<Int>
)

data class Monthly(

	@field:SerializedName("pengeluaran")
	val pengeluaran: List<Int>,

	@field:SerializedName("masukan")
	val masukan: List<Int>,

	@field:SerializedName("tanggal")
	val tanggal: List<String>
)