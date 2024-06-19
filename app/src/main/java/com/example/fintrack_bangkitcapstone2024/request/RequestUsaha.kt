package com.example.fintrack_bangkitcapstone2024.request

import com.google.gson.annotations.SerializedName

data class RequestUsaha(
	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("jenis")
	val jenis: String
)