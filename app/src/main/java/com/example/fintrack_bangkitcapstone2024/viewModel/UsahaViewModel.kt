package com.example.fintrack_bangkitcapstone2024.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fintrack_bangkitcapstone2024.Api.ApiConfig
import com.example.fintrack_bangkitcapstone2024.response.Usaha.ResponseListUsahaById
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsahaViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _listUsaha = MutableLiveData<ResponseListUsahaById>()
    val listUsaha: LiveData<ResponseListUsahaById> = _listUsaha

    fun getListUsahaByUserId(userId: String) {
        _isLoading.value = true
        val call = ApiConfig.getApiService().getListUsahaByOwner(userId)
        call.enqueue(object : Callback<ResponseListUsahaById> {
            override fun onResponse(call: Call<ResponseListUsahaById>, response: Response<ResponseListUsahaById>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsaha.value = response.body()
                    _message.value = "Successfully fetched list of usaha"
                } else {
                    _message.value = "Failed to fetch list of usaha: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResponseListUsahaById>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error message: " + t.message.toString()
            }
        })
    }
}