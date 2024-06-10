package com.example.fintrack_bangkitcapstone2024.ui.Activity.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityProfileBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.CreateBusinessActivity
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.WelcomeActivity
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
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

        binding.changePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.addNewUsaha.setOnClickListener {
            startActivity(Intent(this, CreateBusinessActivity::class.java))
        }

        // get email user
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]

        // Mengambil email pengguna
        userViewModel.getEmail().observe(this, { email ->
            binding.tvGmailUser.text = email
        })



        // Mengambil status sesi login
        userViewModel.getName().observe(this, { name ->
            binding.nameUser.text = name
        })

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
            ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]
        loginViewModel.clearDataLogin()
        Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
        val intent = Intent(this, WelcomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}