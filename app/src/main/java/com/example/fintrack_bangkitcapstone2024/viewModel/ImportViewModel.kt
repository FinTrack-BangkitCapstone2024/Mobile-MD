package com.example.fintrack_bangkitcapstone2024.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fintrack_bangkitcapstone2024.Api.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImportViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _uploadResponse = MutableLiveData<Response<ResponseBody>>()
    val uploadResponse: LiveData<Response<ResponseBody>> = _uploadResponse

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean> = _uploadSuccess


    fun uploadFile(file: MultipartBody.Part, usaha_id: RequestBody) {
        _isLoading.value = true

        val call = ApiConfig.getApiService().uploadCsvFile(file, usaha_id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _uploadResponse.value = response
                    _message.value = "File has been successfully uploaded"
                    _uploadSuccess.value = true
                } else {
                    _message.value = "Failed to upload file: ${response.message()}"
                    _uploadSuccess.value = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error message: " + t.message.toString()
                _uploadSuccess.value = false
            }
        })
    }
}