package com.example.fintrack_bangkitcapstone2024.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintrack_bangkitcapstone2024.Api.ApiConfig
import com.example.fintrack_bangkitcapstone2024.response.dataResonse.ResponseWeekly
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeeklyDataViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _weeklyDataResponse = MutableLiveData<ResponseWeekly?>()
    val weeklyDataResponse: MutableLiveData<ResponseWeekly?> = _weeklyDataResponse


    fun fetchWeeklyData(idUsaha: String) {
        val api = ApiConfig.getApiService().getWeeklyData(idUsaha)
        api.enqueue(object : Callback<ResponseWeekly> {
            override fun onResponse(
                call: Call<ResponseWeekly>,
                response: Response<ResponseWeekly>
            ) {
                if (response.isSuccessful) {
                    _weeklyDataResponse.value = response.body()

                    // Save the data to DataStore
                    viewModelScope.launch {
                        response.body()?.data?.let { data ->
                            userPreferences.savePengeluaran(data.pengeluaran)
                            userPreferences.saveMasukan(data.masukan)
                            userPreferences.saveDay(data.day)
                        }
                    }
                } else {
                    _weeklyDataResponse.value = null
                }
            }

            override fun onFailure(call: Call<ResponseWeekly>, t: Throwable) {
                _weeklyDataResponse.value = null
            }
        })
    }
}