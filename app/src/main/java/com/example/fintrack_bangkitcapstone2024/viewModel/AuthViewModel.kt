package com.example.fintrack_bangkitcapstone2024.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fintrack_bangkitcapstone2024.Api.ApiConfig
import com.example.fintrack_bangkitcapstone2024.request.RequestFinancials
import com.example.fintrack_bangkitcapstone2024.request.RequestLogin
import com.example.fintrack_bangkitcapstone2024.request.RequestRegister
import com.example.fintrack_bangkitcapstone2024.request.RequestUpdatePassword
import com.example.fintrack_bangkitcapstone2024.request.RequestUsaha
import com.example.fintrack_bangkitcapstone2024.response.Financial.DataForcasting
import com.example.fintrack_bangkitcapstone2024.response.Financial.ResponseAddFInancial
import com.example.fintrack_bangkitcapstone2024.response.Financial.ResponseFinancialData
import com.example.fintrack_bangkitcapstone2024.response.Financial.ResponseForcasting
import com.example.fintrack_bangkitcapstone2024.response.ResponseLogin
import com.example.fintrack_bangkitcapstone2024.response.ResponseRegister
import com.example.fintrack_bangkitcapstone2024.response.Usaha.ResponseUsaha
import com.example.fintrack_bangkitcapstone2024.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AuthViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    var isError: Boolean = false

    private val _isErrorBusines = MutableLiveData<Boolean>()
    val isErrorBusines: LiveData<Boolean> = _isErrorBusines


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    private val _financialData = MutableLiveData<ResponseFinancialData>()
    val financialData: LiveData<ResponseFinancialData> = _financialData

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _userLogin = MutableLiveData<ResponseLogin>()
    val userLogin: LiveData<ResponseLogin> = _userLogin


    private val _usahaId = MutableLiveData<String>()
    val usahaId: LiveData<String> = _usahaId

    private val _forcastingData = MutableLiveData<DataForcasting?>()
    val forcastingData: LiveData<DataForcasting?> = _forcastingData


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
        if (t is IOException) {
            // This is a network failure or request cancellation
            _errorMessage.value = "No connection"
        } else {
            // Unknown error occurred
            _errorMessage.value = t.message
        }
    }

    fun getDataForcasting(usahaId: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getDataForcasting(usahaId)
        api.enqueue(object : Callback<ResponseForcasting> {
            override fun onResponse(
                call: Call<ResponseForcasting>,
                response: Response<ResponseForcasting>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = "Successfully fetched data"
                    _forcastingData.value = response.body()?.data
                } else {
                    _message.value = "Failed to fetch data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResponseForcasting>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error message: " + t.message.toString()
            }
        })
    }

    fun getFinancialData(usahaId: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getFinancialDataFromUsaha(usahaId)
        api.enqueue(object : Callback<ResponseFinancialData> {
            override fun onResponse(
                call: Call<ResponseFinancialData>,
                response: Response<ResponseFinancialData>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _financialData.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<ResponseFinancialData>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }

    fun getUsahaById(usahaId: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getUsahaById(usahaId)
        api.enqueue(object : Callback<ResponseUsaha> {
            override fun onResponse(
                call: Call<ResponseUsaha>,
                response: Response<ResponseUsaha>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    _usahaResponse.value = response.body()
                    _message.value = "Successfully fetched usaha data"
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
                    _isErrorBusines.value = false
                    _usahaResponse.value = response.body()
                    _message.value = "Usaha has been successfully created"
                    _usahaId.value = response.body()?.data?.id
                } else {
                    _isErrorBusines.value = true
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
                _isErrorBusines.value = true
                _message.value = "Error message: " + t.message.toString()
            }
        })
    }

    fun createFinancials(requestFinancials: RequestFinancials) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().addFinancialData(requestFinancials)
        api.enqueue(object : Callback<ResponseAddFInancial> {
            override fun onResponse(
                call: Call<ResponseAddFInancial>,
                response: Response<ResponseAddFInancial>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    _message.value = "Financial request has been successfully created"
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

            override fun onFailure(call: Call<ResponseAddFInancial>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    fun updateUser(id: String, name:String ) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().updateUserDetails(id,name )
        api.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userUpdateResponse.value = response.body()
                } else {
                    _userUpdateResponse.value = null
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                _isLoading.value = false
                _userUpdateResponse.value = null
            }
        })
    }

    fun updatePassword(id: String, requestUpdatePassword: RequestUpdatePassword) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().updateUserPassword(id, requestUpdatePassword)
        api.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userUpdateResponse.value = response.body()
                    _message.value = response.message()
                    Log.d("test","Update password response: ${_message}")
                } else {
                    _userUpdateResponse.value = null
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                _isLoading.value = false
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
                        502 -> _message.value = "Bad Gateway"
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
