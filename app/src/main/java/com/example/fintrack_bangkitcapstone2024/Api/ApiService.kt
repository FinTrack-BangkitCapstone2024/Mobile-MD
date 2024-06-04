package com.example.fintrack_bangkitcapstone2024.Api


import com.example.fintrack_bangkitcapstone2024.request.RequestLogin
import com.example.fintrack_bangkitcapstone2024.request.RequestRegister
import com.example.fintrack_bangkitcapstone2024.response.ResponseLogin
import com.example.fintrack_bangkitcapstone2024.response.ResponseRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("auth/signUpWithEmail") // replace with your actual endpoint
    fun registerData(@Body registerUser: RequestRegister): Call<ResponseRegister>

    @POST("auth/signInWithEmail")
    fun loginUser(@Body requestLogin: RequestLogin): Call<ResponseLogin>

}