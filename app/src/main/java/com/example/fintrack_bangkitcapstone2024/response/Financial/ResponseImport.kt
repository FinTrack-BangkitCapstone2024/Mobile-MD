package com.example.fintrack_bangkitcapstone2024.response.Financial

import com.google.gson.annotations.SerializedName

data class ResponseImport(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: DataImport,

	@field:SerializedName("status")
	val status: String
)

data class DataImport(

	@field:SerializedName("data_id")
	val dataId: List<String>,

	@field:SerializedName("total_fianncial_data_created")
	val totalFianncialDataCreated: Int,

	@field:SerializedName("message")
	val message: String
)
