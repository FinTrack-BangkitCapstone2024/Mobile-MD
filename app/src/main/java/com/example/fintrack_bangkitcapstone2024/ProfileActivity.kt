package com.example.fintrack_bangkitcapstone2024

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityProfileBinding
import com.example.fintrack_bangkitcapstone2024.viewModel.LoginUserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private val pref = UserPreferences.getInstance(dataStore)

    private lateinit var binding : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.logout))
                setMessage(getString(R.string.confirm_logout))
                setPositiveButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }
                setNegativeButton(getString(R.string.logout_with_space)) { _, _ ->
                    logout()
                }
                create()
                show()
            }

        }

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }


    }

    private fun logout() {
        val loginViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[LoginUserViewModel::class.java]
        loginViewModel.clearDataLogin()
        Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
        val intent = Intent(this, WelcomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}