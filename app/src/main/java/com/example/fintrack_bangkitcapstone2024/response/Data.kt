package com.example.fintrack_bangkitcapstone2024.response

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("token")
	val token: Token
)