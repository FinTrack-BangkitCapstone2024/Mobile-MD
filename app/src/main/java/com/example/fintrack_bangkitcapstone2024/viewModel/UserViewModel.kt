package com.example.fintrack_bangkitcapstone2024.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fintrack_bangkitcapstone2024.response.ResonseReport.Data
import kotlinx.coroutines.launch

class UserViewModel(private val pref: UserPreferences) : ViewModel() {

    fun getLoginSession(): LiveData<Boolean> {
        return pref.getLoginSession().asLiveData()
    }


    fun saveLoginSession(loginSession: Boolean) {
        viewModelScope.launch {
            pref.saveLoginSession(loginSession)
        }
    }


    fun saveUsahaId(usahaId: String) {
        viewModelScope.launch {
            pref.saveUsahaId(usahaId)
        }
    }

    fun getUsahaId(): LiveData<String> {
        return pref.getUsahaId().asLiveData()
    }


    fun getUserId(): LiveData<String> {
        return pref.getUserId().asLiveData()
    }

    fun saveUserId(userId: String) {
        viewModelScope.launch {
            pref.saveUserId(userId)
        }
    }


    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun getEmail(): LiveData<String> {
        return pref.getEmail().asLiveData()
    }

    fun saveEmail(email: String) {
        viewModelScope.launch {
            pref.saveEmail(email)
        }
    }

    fun getName(): LiveData<String> {
        return pref.getName().asLiveData()
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            (getName() as MutableLiveData).postValue(name)
        }
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            pref.saveName(name)
        }
    }

    fun getData(): LiveData<Data?> {
        return pref.getData().asLiveData()
    }


    fun clearDataLogin() {
        viewModelScope.launch {
            pref.clearDataLogin()
        }
    }
}