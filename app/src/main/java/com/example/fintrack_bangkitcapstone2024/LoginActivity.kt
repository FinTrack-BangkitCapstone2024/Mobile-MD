package com.example.fintrack_bangkitcapstone2024

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
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.LoginUserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = UserPreferences.getInstance(dataStore)
        val loginUserViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[LoginUserViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            binding.cvEmailLogin.clearFocus()
            binding.cvPassLogin.clearFocus()

            if (isDataValid()) {
                val reqLogin = RequestLogin(
                    binding.cvPassLogin.text.toString().trim(),
                    binding.cvEmailLogin.text.toString().trim()
                )
                authViewModel.getResponseLogin(reqLogin)
            } else {
                Toast.makeText(this, getString(R.string.data_correctly), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        loginUserViewModel.getLoginSession().observe(this) { session ->
            if (session) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        authViewModel.messageLogin.observe(this) { message ->
            handleLoginResponse(
                authViewModel.isErrorLogin,
                message,
                loginUserViewModel
            )
        }

        authViewModel.isLoadingLogin.observe(this, Observer<Boolean> { isLoading ->
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
        userLoginViewModel: LoginUserViewModel
    ) {
        if (!isErrorLogin) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val user = authViewModel.userLogin.value
            user?.let { dataUser ->
                userLoginViewModel.saveLoginSession(true)
                userLoginViewModel.saveToken(dataUser.data.token.accessToken)
                userLoginViewModel.saveName(dataUser.data.user.name)
            }
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}