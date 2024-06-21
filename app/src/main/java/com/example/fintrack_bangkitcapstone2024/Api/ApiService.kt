package com.example.fintrack_bangkitcapstone2024.Api

import com.example.fintrack_bangkitcapstone2024.request.RequestFinancials
import com.example.fintrack_bangkitcapstone2024.request.RequestLogin
import com.example.fintrack_bangkitcapstone2024.request.RequestRegister
import com.example.fintrack_bangkitcapstone2024.request.RequestUpdatePassword
import com.example.fintrack_bangkitcapstone2024.request.RequestUsaha
import com.example.fintrack_bangkitcapstone2024.response.Financial.ResponseAddFInancial
import com.example.fintrack_bangkitcapstone2024.response.Financial.ResponseFinancialData
import com.example.fintrack_bangkitcapstone2024.response.Financial.ResponseForcasting
import com.example.fintrack_bangkitcapstone2024.response.ResonseReport.ResponseDataAnalysis
import com.example.fintrack_bangkitcapstone2024.response.ResponseLogin
import com.example.fintrack_bangkitcapstone2024.response.ResponseRegister
import com.example.fintrack_bangkitcapstone2024.response.Usaha.ResponseListUsahaById
import com.example.fintrack_bangkitcapstone2024.response.Usaha.ResponseReportToday
import com.example.fintrack_bangkitcapstone2024.response.Usaha.ResponseUsaha
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiService {
    @POST("auth/signUpWithEmail")
    fun registerData(@Body registerUser: RequestRegister): Call<ResponseRegister>

    @POST("auth/signInWithEmail")
    fun loginUser(@Body requestLogin: RequestLogin): Call<ResponseLogin>

    @POST("usaha/financial/")
    fun addFinancialData(@Body requestRegister: RequestFinancials): Call<ResponseAddFInancial>

    @POST("usaha/")
    fun getUsaha(@Body requestRegister: RequestUsaha): Call<ResponseUsaha>

    @Multipart
    @POST("usaha/financial/csv")
    fun uploadCsvFile(
        @Part file: MultipartBody.Part,
        @Part("usaha_id") usaha_id: RequestBody
    ): Call<ResponseBody>

    @PUT("users/{id}")
    fun updateUserDetails(
        @Path("id") id: String,
        @Body name: String
    ): Call<ResponseRegister>

    @PUT("users/{id}/password")
    fun updateUserPassword(
        @Path("id") id: String,
        @Body requestUpdatePassword: RequestUpdatePassword
    ): Call<ResponseRegister>

    @GET("usaha/{usaha_id}/financial")
    fun getFinancialDataFromUsaha(
        @Path("usaha_id") usahaId: String,
    ): Call<ResponseFinancialData>


    @GET("usaha/{usaha_id}")
    fun getUsahaById(@Path("usaha_id") usahaId: String): Call<ResponseUsaha>

    @GET("usaha/{usahaId}/analysis")
    fun getWeeklyData(@Path("usahaId") usahaId: String): Call<ResponseDataAnalysis>

    @GET("usaha/owner/{id}")
    fun getListUsahaByOwner(@Path("id") id: String): Call<ResponseListUsahaById>


    @GET("usaha/{usahaId}/forecasting")
    fun getDataForcasting(@Path("usahaId") usahaId: String): Call<ResponseForcasting>

    @GET("usaha/{usahaId}/laporan")
    fun getReport(@Path("usahaId") usahaId: String): Call<ResponseReportToday>


}