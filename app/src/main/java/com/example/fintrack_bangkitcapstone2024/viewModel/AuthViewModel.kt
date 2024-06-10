package com.example.fintrack_bangkitcapstone2024.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fintrack_bangkitcapstone2024.Api.ApiConfig
import com.example.fintrack_bangkitcapstone2024.request.RequestFinancials
import com.example.fintrack_bangkitcapstone2024.request.RequestLogin
import com.example.fintrack_bangkitcapstone2024.request.RequestRegister
import com.example.fintrack_bangkitcapstone2024.request.RequestUpdate
import com.example.fintrack_bangkitcapstone2024.request.RequestUsaha
import com.example.fintrack_bangkitcapstone2024.response.ResponseLogin
import com.example.fintrack_bangkitcapstone2024.response.ResponseRegister
import com.example.fintrack_bangkitcapstone2024.response.Usaha.ResponseUsaha
import com.example.fintrack_bangkitcapstone2024.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    var isError: Boolean = false

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _userLogin = MutableLiveData<ResponseLogin>()
    val userLogin: LiveData<ResponseLogin> = _userLogin


    private val _usahaId = MutableLiveData<String>()
    val usahaId: LiveData<String> = _usahaId


    private val _usahaResponse = MutableLiveData<ResponseUsaha?>()
    val usahaResponse: LiveData<ResponseUsaha?> = _usahaResponse


    private val _userUpdateResponse = MutableLiveData<ResponseRegister?>()
    val userUpdateResponse: LiveData<ResponseRegister?> = _userUpdateResponse

    private fun handleResponse(
        response: Response<ResponseRegister>,
        successMessage: String
    ) {
        _isLoading.value = false
        if (response.isSuccessful) {
            isError = false
            _userUpdateResponse.value = response.body()
            _message.value = successMessage
        } else {
            isError = true
            when (response.code()) {
                400 -> _message.value = "Bad request"
                408 -> _message.value = "Your internet connection is slow, please try again"
                500 -> _message.value = "An error occurred, please try again"
                else -> Log.d("Response code", response.message())
            }
        }
    }

    private fun handleFailure(t: Throwable) {
        _isLoading.value = false
        isError = true
        _message.value = "Error message: " + t.message.toString()
    }

    fun createUsaha(requestUsaha: RequestUsaha) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getUsaha(requestUsaha)
        api.enqueue(object : Callback<ResponseUsaha> {
            override fun onResponse(
                call: Call<ResponseUsaha>,
                response: Response<ResponseUsaha>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    _usahaResponse.value = response.body()
                    _message.value = "Usaha has been successfully created"
                    _usahaId.value = response.body()?.data?.id
                    Log.d(
                        "Response",
                        "Full response: ${response.body()}"
                    ) // Mencetak seluruh respons
                    Log.d("Response", "Usaha ID: ${response.body()?.data?.id}") // Mencetak ID
                } else {
                    isError = true
                    when (response.code()) {
                        400 -> _message.value = "Bad request"
                        408 -> _message.value = "Your internet connection is slow, please try again"
                        500 -> _message.value = "An error occurred, please try again"
                        else -> Log.d("Response code", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseUsaha>, t: Throwable) {
                _isLoading.value = false
                isError = true
                _message.value = "Error message: " + t.message.toString()
            }
        })
    }

    fun createFinancials(requestFinancials: RequestFinancials) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().addFinancialData(requestFinancials)
        api.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                handleResponse(response, "Financial request has been successfully created")
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    fun updateUser(id: String, requestUpdateUser: RequestUpdate) {
        val api = ApiConfig.getApiService().updateUser(id, requestUpdateUser)
        api.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                if (response.isSuccessful) {
                    _userUpdateResponse.value = response.body()
                } else {
                    _userUpdateResponse.value = null
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                _userUpdateResponse.value = null
            }
        })
    }

    fun getResponseRegister(registerUser: RequestRegister) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().registerData(registerUser)
        api.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                handleResponse(response, "The account has been successfully created")
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    fun getResponseLogin(loginDataAccount: RequestLogin) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().loginUser(loginDataAccount)
        api.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    _userLogin.value = response.body()!!
                    _currentUser.value = response.body()!!.data.user
                    _message.value = "Halo ${_userLogin.value!!.data.user.name}!"
                } else {
                    isError = true
                    when (response.code()) {
                        200, 201 -> _message.value = "Register Berhasil"
                        401 -> _message.value =
                            "Email or password you entered is wrong, please try again"

                        408 -> _message.value = "Your internet connection is slow, please try again"
                        else -> _message.value = "Error message: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                handleFailure(t)
            }
        })
    }
}
