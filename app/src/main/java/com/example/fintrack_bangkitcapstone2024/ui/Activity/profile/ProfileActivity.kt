package com.example.fintrack_bangkitcapstone2024.ui.Activity.profile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityProfileBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.CreateBusinessActivity
import com.example.fintrack_bangkitcapstone2024.ui.Activity.MainActivity
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.WelcomeActivity
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.UsahaViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private val pref = UserPreferences.getInstance(dataStore)

    private val preferences by lazy { UserPreferences.getInstance(dataStore) }
    private val userViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]
    }
    private lateinit var binding: ActivityProfileBinding
    private var dialog: Dialog? = null
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

        binding.switchUsaha.setOnClickListener {
            showDialogToChooseUsaha()
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

    private fun showDialogToChooseUsaha() {
        val usahaViewModel = UsahaViewModel()

        userViewModel.getUserId().observe(this, Observer { userId ->
            usahaViewModel.getListUsahaByUserId(userId)
        })

        usahaViewModel.listUsaha.observe(this, Observer { response ->
            if (response != null && !isFinishing) {
                // If the response is not null, create an AlertDialog with a list of Usaha
                val usahaNames = response.data.usaha.map { it.nama }.toTypedArray()
                var checkedItem = 0 // This will be the index of the selected radio button

                val dialog = AlertDialog.Builder(this)
                    .setTitle("Choose Usaha")
                    .setSingleChoiceItems(usahaNames, checkedItem) { _, which ->
                        // Update the index of the checked item when a radio button is clicked
                        checkedItem = which
                    }
                    .setPositiveButton("OK") { _, _ ->
                        // Save the ID of the selected Usaha when the OK button is clicked
                        val chosenUsahaId = response.data.usaha[checkedItem].id
                        userViewModel.saveUsahaId(chosenUsahaId)
                        Toast.makeText(this, "Usaha berhasil Digantikan menjadi ${response.data.usaha[checkedItem].nama}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel", null)
                    .create()

                dialog.show()
            }
        })
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