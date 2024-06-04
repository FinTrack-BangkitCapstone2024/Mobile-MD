package com.example.fintrack_bangkitcapstone2024.response

import com.google.gson.annotations.SerializedName

data class Token(

	@field:SerializedName("expirationTime")
	val expirationTime: Long,

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("refreshToken")
	val refreshToken: String
)