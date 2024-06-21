package com.example.fintrack_bangkitcapstone2024.response.Financial

import com.google.gson.annotations.SerializedName

data class ResponseForcasting(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataForcasting? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataForcasting(
	@field:SerializedName("pemasukan")
	val pemasukan: List<Float>,

	@field:SerializedName("pengeluaran")
	val pengeluaran: List<Float>
)
