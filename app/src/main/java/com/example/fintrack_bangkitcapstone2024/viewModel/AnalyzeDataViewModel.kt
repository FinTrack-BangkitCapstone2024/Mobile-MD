package com.example.fintrack_bangkitcapstone2024.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintrack_bangkitcapstone2024.Api.ApiConfig
import com.example.fintrack_bangkitcapstone2024.response.ResonseReport.Monthly
import com.example.fintrack_bangkitcapstone2024.response.ResonseReport.ResponseDataAnalysis
import com.example.fintrack_bangkitcapstone2024.response.ResonseReport.Weekly
import com.example.fintrack_bangkitcapstone2024.response.ResonseReport.Yearly
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalyzeDataViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _AnalysisDataResponse = MutableLiveData<ResponseDataAnalysis?>()
    val weeklyDataResponse: MutableLiveData<ResponseDataAnalysis?> = _AnalysisDataResponse

    private val _yearlyData = MutableLiveData<Yearly?>()
    val yearlyData: LiveData<Yearly?> = _yearlyData

    private val _monthlyData = MutableLiveData<Monthly?>()
    val monthlyData: LiveData<Monthly?> = _monthlyData

    private val _weeklyData = MutableLiveData<Weekly?>()
    val weeklyData: LiveData<Weekly?> = _weeklyData

    fun fetchAnalizeData(idUsaha: String) {
        val api = ApiConfig.getApiService().getWeeklyData(idUsaha)
        api.enqueue(object : Callback<ResponseDataAnalysis> {
            override fun onResponse(
                call: Call<ResponseDataAnalysis>,
                response: Response<ResponseDataAnalysis>
            ) {
                if (response.isSuccessful) {
                    _AnalysisDataResponse.value = response.body()

                    // Save the data to LiveData
                    _yearlyData.value = response.body()?.data?.yearly
                    _monthlyData.value = response.body()?.data?.monthly
                    _weeklyData.value = response.body()?.data?.weekly
                    Log.d(
                        "AnalyzeDataViewModel",
                        "Data saved to LiveData${_yearlyData.value},\n ${_monthlyData.value},\n ${_weeklyData.value}"
                    )

                    // Save the data to DataStore
                    viewModelScope.launch {
                        response.body()?.data?.let { data ->
//                            userPreferences.savePengeluaran(data.pengeluaran)
//                            userPreferences.saveMasukan(data.masukan)
//                            userPreferences.saveDay(data.day)
                        }
                        Log.d(
                            "AnalyzeDataViewModel",
                            "Data saved to DataStore${userPreferences.getMasukan()}, ${userPreferences.getPengeluaran()}, ${userPreferences.getDay()}, ${userPreferences.getMasukan()}"
                        )
                    }
                } else {
                    _AnalysisDataResponse.value = null
                }
            }

            override fun onFailure(call: Call<ResponseDataAnalysis>, t: Throwable) {
                _AnalysisDataResponse.value = null
            }
        })
    }
}