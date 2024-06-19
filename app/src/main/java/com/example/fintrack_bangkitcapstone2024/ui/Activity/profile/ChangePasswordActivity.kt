package com.example.fintrack_bangkitcapstone2024.ui.Activity.profile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityChangePasswordBinding
import com.example.fintrack_bangkitcapstone2024.request.RequestUpdatePassword
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

        userViewModel.getUserId().observe(this, Observer {
            userIdBackup = it
        })

        binding.btnSaveChanges.setOnClickListener { changePassword() }


    }

    private fun updateUserProfile() {
        userViewModel.getName().observe(this, Observer { sameName ->
            val newPass = binding.cvPassRegister.text.toString()
            val oldPass = binding.cvCurrentPassword.text.toString()

            val requestUpdatePassword = RequestUpdatePassword(
                oldPass,
                newPass
            )
            authViewModel.updatePassword(userIdBackup,requestUpdatePassword)
        })
    }

    private fun changePassword() {

        val currentPassword = binding.cvCurrentPassword.toString()
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

            else -> {
                // All conditions are met, proceed with the password change
                updateUserProfile()
                authViewModel.userUpdateResponse.observe(this) { response ->
                    handleUpdateResponse(response)
                }

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
            // buatkan toast
            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
            // buatkan intent ke profile activity
            finish()
        } else {
            Log.d("EditProfileActivity", "Update user request not successful")
            Log.d("EditProfileActivity", "Chek your password and try again")
            // buatkan toast
            Toast.makeText(this, "Check your password and try again", Toast.LENGTH_SHORT).show()
        }
    }
}