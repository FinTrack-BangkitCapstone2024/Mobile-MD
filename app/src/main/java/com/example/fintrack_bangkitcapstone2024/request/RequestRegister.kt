package com.example.fintrack_bangkitcapstone2024.request

data class RequestRegister(
	val password: String,
	val name: String,
	val email: String
)

data class RequestUpdatePassword(
	val old_password: String,
	val new_password: String
)

data class RequestPasswordUpdate(
	val old_password: String,
	val new_password: String,
)