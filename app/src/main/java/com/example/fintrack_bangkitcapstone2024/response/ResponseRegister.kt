package com.example.fintrack_bangkitcapstone2024.response

import com.google.gson.annotations.SerializedName

data class ResponseRegister(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)