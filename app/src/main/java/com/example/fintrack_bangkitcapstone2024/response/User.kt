package com.example.fintrack_bangkitcapstone2024.response

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)