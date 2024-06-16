package com.example.fintrack_bangkitcapstone2024.ui.Activity.profile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityChangePasswordBinding
import com.example.fintrack_bangkitcapstone2024.request.RequestUpdate
import com.example.fintrack_bangkitcapstone2024.response.ResponseRegister
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory

class ChangePasswordActivity : AppCompatActivity() {

    private val authViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private val userViewModel by lazy {
        val preferences = UserPreferences.getInstance(dataStore)
        ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]
    }

    private lateinit var userIdBackup: String
    private lateinit var backUpPassword: String
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        userViewModel.getUserId().observe(this, Observer { userId ->
            userIdBackup = userId
        })

        binding.btnSaveChanges.setOnClickListener { changePassword() }

        authViewModel.userUpdateResponse.observe(this) { response ->
            handleUpdateResponse(response)
        }
    }

    private fun updateUserProfile() {
        userViewModel.getName().observe(this, Observer { sameName ->
            val newPass = binding.cvPassRegister.text.toString()

            // Save name to preferences
            userViewModel.savePassword(newPass)

            // If new password is empty, return the previous password
            val requestUpdateUser = RequestUpdate(
                password = newPass,
                name = sameName
            )
            authViewModel.updateUser(userIdBackup, requestUpdateUser)
        })
    }

    private fun changePassword() {
        val currentPassword = binding.cvCurrentPassword.toString()
        userViewModel.getPassword().observe(this, Observer {
            backUpPassword = it
        })
        val newPassword = binding.cvPassRegister.text.toString()
        val confirmPassword = binding.cvPassSame.text.toString()

        when {
            currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                // One or more fields are empty
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            newPassword != confirmPassword -> {
                // New password and confirm password do not match
                Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show()
            }
            currentPassword == newPassword -> {
                // New password is the same as the current password
                Toast.makeText(this, "New password should be different from the current password", Toast.LENGTH_SHORT).show()
            }
            currentPassword != backUpPassword -> {
                // Current password is incorrect
                Log.e("ChangePasswordActivity", backUpPassword)
                Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // All conditions are met, proceed with the password change
                updateUserProfile()
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleUpdateResponse(response: ResponseRegister?) {
        if (response != null) {
            Log.d("EditProfileActivity", "Update user response: ${response}")
            response.data?.user?.let {
                userViewModel.saveName(it.name)
                userViewModel.updateName(it.name) // Update the LiveData
            }
        } else {
            Log.d("EditProfileActivity", "Update user request not successful")
        }
    }
}