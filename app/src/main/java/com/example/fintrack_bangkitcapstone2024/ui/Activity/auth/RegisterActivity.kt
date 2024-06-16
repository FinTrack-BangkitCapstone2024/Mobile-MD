package com.example.fintrack_bangkitcapstone2024.ui.Activity.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityRegisterBinding
import com.example.fintrack_bangkitcapstone2024.request.RequestLogin
import com.example.fintrack_bangkitcapstone2024.request.RequestRegister
import com.example.fintrack_bangkitcapstone2024.ui.Activity.CreateBusinessActivity
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel by lazy{
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupUI()
        setupObserverViewModel()

    }

    private fun clearInputFocus() {
        binding.apply {
            cvEmailRegister.clearFocus()
            cvPassRegister.clearFocus()
            cvPassSame.clearFocus()
        }
    }

    private fun validateInputs(): Boolean {
        return binding.cvEmailRegister.isEmailValid && binding.cvPassRegister.isPasswordValid && binding.cvPassSame.isPasswordValid
    }

    private fun tampilkanErrorValidasi() {
        if (!binding.cvEmailRegister.isEmailValid) binding.cvEmailRegister.error =
            resources.getString(R.string.emptyEmail)
        if (!binding.cvPassRegister.isPasswordValid) binding.cvPassRegister.error =
            resources.getString(R.string.emtyPassword)
        if (!binding.cvPassSame.isPasswordValid) binding.cvPassSame.error =
            resources.getString(R.string.emtyPasswordConfirm)

        Toast.makeText(this, getString(R.string.makeSureData), Toast.LENGTH_SHORT).show()
    }


    private fun setupUI() {
        binding.btnToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnSignup.setOnClickListener {

            clearInputFocus()
            if (validateInputs()) {

                val dataRegister = RequestRegister(
                    name = binding.cvName.text.toString().trim(),
                    password = binding.cvPassRegister.text.toString().trim(),
                    email = binding.cvEmailRegister.text.toString().trim()
                )
                authViewModel.getResponseRegister(dataRegister)
                Log.d("dataRegister", "$dataRegister")

            } else {
                tampilkanErrorValidasi()
            }
        }
    }


    private fun setupObserverViewModel() {
        Log.d("userLoginViewModel", "memasukan login")
        val userLoginViewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(dataStore)))[UserViewModel::class.java]
        userLoginViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                navigateToMainActivity()
            }
        }
        authViewModel.message.observe(this) { regist ->
            handleResponseRegister(authViewModel.isError, regist)
        }

        authViewModel.message.observe(this) { login ->
            handleResponseLogin(authViewModel.isError, login, userLoginViewModel)
        }


    }

    private fun handleResponseLogin(isError: Boolean, message: String, userViewModel: UserViewModel) {
        if (isError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } else {
            val user = authViewModel.userLogin.value
            user?.let { dataUser ->
                userViewModel.saveLoginSession(true)
                userViewModel.saveToken(dataUser.data.token.accessToken)
                userViewModel.saveName(dataUser.data.user.name)
                userViewModel.saveUserId(dataUser.data.user.id)
                userViewModel.saveEmail(dataUser.data.user.email)
                userViewModel.savePassword(dataUser.data.user.password)

            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleResponseRegister(isError: Boolean, message: String) {
        if (!isError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val dataLoginUser = RequestLogin(
                binding.cvPassRegister.text.toString(),
                binding.cvEmailRegister.text.toString()
            )
            authViewModel.getResponseLogin(dataLoginUser)
        } else {
            if (message == "1") {
                binding.cvEmailRegister.setErrorMessage(
                    resources.getString(R.string.email_already),
                    binding.cvEmailRegister.text.toString()
                )
                Toast.makeText(
                    this,
                    resources.getString(R.string.email_already),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun navigateToMainActivity() {
        val intent = Intent(this, CreateBusinessActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}