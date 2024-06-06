package com.example.fintrack_bangkitcapstone2024.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fintrack_bangkitcapstone2024.Api.ApiConfig
import com.example.fintrack_bangkitcapstone2024.request.RequestLogin
import com.example.fintrack_bangkitcapstone2024.request.RequestRegister
import com.example.fintrack_bangkitcapstone2024.request.RequestUpdate
import com.example.fintrack_bangkitcapstone2024.response.ResponseLogin
import com.example.fintrack_bangkitcapstone2024.response.ResponseRegister
import com.example.fintrack_bangkitcapstone2024.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel: ViewModel() {


    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin
    private val _messageLogin = MutableLiveData<String>()
    val messageLogin: LiveData<String> = _messageLogin
    var isErrorLogin: Boolean = false

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: MutableLiveData<User?> = _currentUser


    private val _userLogin = MutableLiveData<ResponseLogin>()
    val userLogin: LiveData<ResponseLogin> = _userLogin

    var isErrorRegist: Boolean = false
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val messageRegister: LiveData<String> = _message

    private val _userUpdateResponse = MutableLiveData<ResponseRegister?>()
    val userUpdateResponse: MutableLiveData<ResponseRegister?> = _userUpdateResponse


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


    fun logout() {
        _currentUser.value = null
    }


    fun getResponseRegister(registerUser: RequestRegister) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().registerData(registerUser)
        api.enqueue(object : retrofit2.Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                _isLoading.value = false
                if (response.code() == 201) {
                    isErrorRegist = false
                    _message.value = "Yes, the account has been successfully created"
                } else {
                    isErrorRegist = true
                    when (response.code()) {
                        400 -> _message.value = "Bad request"
                        408 -> _message.value = "Your internet connection is slow, please try again"
                        500 -> _message.value =
                            "The email you entered is already registered, please use another email"

                        else -> Log.d("respons code", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                isErrorRegist = true
                _isLoading.value = false
                _message.value = "Error message2: " + t.message.toString()
            }
        })
    }

    fun getResponseLogin(loginDataAccount: RequestLogin) {
        _isLoadingLogin.value = true
        val api = ApiConfig.getApiService().loginUser(loginDataAccount)
        api.enqueue(object : retrofit2.Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                _isLoadingLogin.value = false
                val responseBody = response.body()

                if (response.isSuccessful) {
                    isErrorLogin = false
                    _userLogin.value = responseBody!!
                    _currentUser.value = responseBody.data.user // Update currentUser
                    _messageLogin.value = "Halo ${_userLogin.value!!.data.user.name}!"
                } else {
                    isErrorLogin = true
                    when (response.code()) {
                        200, 201 -> _messageLogin.value = "Register Berhasil"
                        401 -> _messageLogin.value =
                            "Email or password you entered is wrong, please try again"

                        408 -> _messageLogin.value =
                            "Your internet connection is slow, please try again"

                        else -> _messageLogin.value = "Error message: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                isErrorLogin = true
                _isLoadingLogin.value = false
                _messageLogin.value = "Error message2: " + t.message.toString()
            }
        })
    }
}
