package com.example.fintrack_bangkitcapstone2024.request

data class RequestRegister(
	val password: String,
	val name: String,
	val email: String
)

data class RequestUpdate(
	val password: String,
	val name: String,
)