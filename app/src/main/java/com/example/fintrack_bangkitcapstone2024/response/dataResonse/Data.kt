package com.example.fintrack_bangkitcapstone2024.response.dataResonse

import com.google.gson.annotations.SerializedName

data class DataWeekly(

	@field:SerializedName("pengeluaran")
	val pengeluaran: List<Int>,

	@field:SerializedName("masukan")
	val masukan: List<Int>,

	@field:SerializedName("day")
	val day: List<String>
)