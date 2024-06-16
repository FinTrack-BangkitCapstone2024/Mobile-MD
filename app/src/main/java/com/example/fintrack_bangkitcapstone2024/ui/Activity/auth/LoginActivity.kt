package com.example.fintrack_bangkitcapstone2024.ui.Activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityLoginBinding
import com.example.fintrack_bangkitcapstone2024.request.RequestLogin
import com.example.fintrack_bangkitcapstone2024.ui.Activity.MainActivity
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UsahaViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private val usahaViewModel: UsahaViewModel by lazy {
        ViewModelProvider(this)[UsahaViewModel::class.java]
    }

    private lateinit var backUpUserId : String

    private val preferences by lazy { UserPreferences.getInstance(dataStore) }
    private val userViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnLogin.setOnClickListener {
            if (isDataValid()) {
                val reqLogin = RequestLogin(
                    binding.cvPassLogin.text.toString().trim(),
                    binding.cvEmailLogin.text.toString().trim()
                )
                authViewModel.getResponseLogin(reqLogin)
            } else {
                if (!binding.cvEmailLogin.isEmailValid) {
                    binding.cvEmailLogin.error = "Email is not valid"
                }
                if (!binding.cvPassLogin.isPasswordValid) {
                    binding.cvPassLogin.error = "Password is not valid"
                }
            }

        }

        userViewModel.getLoginSession().observe(this) { session ->
            if (session) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        authViewModel.message.observe(this) { message ->
            handleLoginResponse(
                authViewModel.isError,
                message,
                userViewModel
            )
        }

        authViewModel.isLoading.observe(this, Observer<Boolean> { isLoading ->
//            if (isLoading) {
//                // Tampilkan ProgressBar
//                binding.progressBarLogin.visibility = View.VISIBLE
//            } else {
//                // Sembunyikan ProgressBar
//                binding.progressBarLogin.visibility = View.GONE
//            }
        })

        binding.cvPassLogin.text?.let { binding.cvPassLogin.setSelection(it.length) }

        binding.buttonToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }

    private fun isDataValid(): Boolean {
        return binding.cvEmailLogin.isEmailValid && binding.cvPassLogin.isPasswordValid
    }

    private fun handleLoginResponse(
        isErrorLogin: Boolean,
        message: String,
        userLoginViewModel: UserViewModel
    ) {
        if (!isErrorLogin) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val user = authViewModel.userLogin.value
            user?.let { dataUser ->
                userLoginViewModel.saveLoginSession(true)
                userLoginViewModel.saveToken(dataUser.data.token.accessToken)
                userLoginViewModel.saveName(dataUser.data.user.name)
                userLoginViewModel.saveUserId(dataUser.data.user.id)
                userLoginViewModel.saveEmail(dataUser.data.user.email)
                userLoginViewModel.savePassword(dataUser.data.user.password)
            }
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}