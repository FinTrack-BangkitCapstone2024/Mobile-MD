package com.example.fintrack_bangkitcapstone2024.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fintrack_bangkitcapstone2024.Api.ApiConfig
import com.example.fintrack_bangkitcapstone2024.response.Financial.ResponseFinancialData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransaksiViewModel : ViewModel() {
    private val _financialData = MutableLiveData<ResponseFinancialData>()
    val financialData: LiveData<ResponseFinancialData> = _financialData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getFinancialData(usahaId: String) {
        val api = ApiConfig.getApiService().getFinancialDataFromUsaha(usahaId)
        api.enqueue(object : Callback<ResponseFinancialData> {
            override fun onResponse(
                call: Call<ResponseFinancialData>,
                response: Response<ResponseFinancialData>
            ) {
                if (response.isSuccessful) {
                    _financialData.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<ResponseFinancialData>, t: Throwable) {
                _errorMessage.value = t.message
            }
        })
    }
}