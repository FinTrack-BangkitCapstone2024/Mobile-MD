package com.example.fintrack_bangkitcapstone2024.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginUserViewModel(private val pref: UserPreferences) : ViewModel() {

    fun getLoginSession(): LiveData<Boolean> {
        return pref.getLoginSession().asLiveData()
    }

    fun saveLoginSession(loginSession: Boolean) {
        viewModelScope.launch {
            pref.saveLoginSession(loginSession)
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

    fun getName(): LiveData<String> {
        return pref.getName().asLiveData()
    }

    fun saveName(token: String) {
        viewModelScope.launch {
            pref.saveName(token)
        }
    }

    fun clearDataLogin() {
        viewModelScope.launch {
            pref.clearDataLogin()
        }
    }
}