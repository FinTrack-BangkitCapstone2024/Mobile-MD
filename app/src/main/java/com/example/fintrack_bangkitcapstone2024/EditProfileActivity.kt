package com.example.fintrack_bangkitcapstone2024

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityEditProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveChanges.setOnClickListener {
            showDialog()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }


    private fun showDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null)
        val builder = MaterialAlertDialogBuilder(this)
            .setView(dialogView)

            .setPositiveButton("Go To Home") { dialog, which ->
                startActivity(Intent(this, ProfileActivity::class.java))                }
        builder.show()
    }
}