package com.example.fintrack_bangkitcapstone2024.response.dataResonse

import com.google.gson.annotations.SerializedName

data class ResponseWeekly(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: DataWeekly,

	@field:SerializedName("status")
	val status: String
)