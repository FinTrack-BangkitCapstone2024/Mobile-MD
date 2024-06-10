package com.example.fintrack_bangkitcapstone2024.Api


import com.example.fintrack_bangkitcapstone2024.request.RequestFinancials
import com.example.fintrack_bangkitcapstone2024.request.RequestLogin
import com.example.fintrack_bangkitcapstone2024.request.RequestRegister
import com.example.fintrack_bangkitcapstone2024.request.RequestUpdate
import com.example.fintrack_bangkitcapstone2024.request.RequestUsaha
import com.example.fintrack_bangkitcapstone2024.response.Financial.ResponseFinancialData
import com.example.fintrack_bangkitcapstone2024.response.ResponseLogin
import com.example.fintrack_bangkitcapstone2024.response.ResponseRegister
import com.example.fintrack_bangkitcapstone2024.response.Usaha.ResponseUsaha
import com.example.fintrack_bangkitcapstone2024.response.dataResonse.ResponseWeekly
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @POST("auth/signUpWithEmail") // replace with your actual endpoint
    fun registerData(@Body registerUser: RequestRegister): Call<ResponseRegister>

    @POST("auth/signInWithEmail")
    fun loginUser(@Body requestLogin: RequestLogin): Call<ResponseLogin>

    @POST("usaha/financial")
    fun addFinancialData(@Body requestRegister: RequestFinancials): Call<ResponseRegister>

    @POST("usaha/")
    fun getUsaha(@Body requestRegister: RequestUsaha): Call<ResponseUsaha>

    @GET("usaha/{usahaId}/financial")
    fun getFinancialDataFromUsaha(
        @Path("usahaId") usahaId: String
    ): Call<ResponseFinancialData>

    @PUT("users/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Body requestUpdateUser: RequestUpdate
    ): Call<ResponseRegister>


    @GET("usaha/{idUsaha}/weekly")
    fun getWeeklyData(@Path("idUsaha") idUsaha: String): Call<ResponseWeekly>

}