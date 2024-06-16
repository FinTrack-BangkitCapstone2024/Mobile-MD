package com.example.fintrack_bangkitcapstone2024.response.Financial

import com.google.gson.annotations.SerializedName

data class ResponseAddFInancial(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("id")
	val id: String? = null
)
