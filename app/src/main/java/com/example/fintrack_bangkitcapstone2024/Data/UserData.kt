package com.example.fintrack_bangkitcapstone2024.Data

data class UserDataLogin(
    val userId: String,
    val token: String,
    val name: String,
    val loginSession: Boolean
)