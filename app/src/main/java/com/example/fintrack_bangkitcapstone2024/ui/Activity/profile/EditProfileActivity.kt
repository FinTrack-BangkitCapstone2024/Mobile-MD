package com.example.fintrack_bangkitcapstone2024.ui.Activity.profile

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityEditProfileBinding
import com.example.fintrack_bangkitcapstone2024.response.ResponseRegister
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditProfileActivity : AppCompatActivity() {


    private lateinit var userIdBackup: String

    private lateinit var binding: ActivityEditProfileBinding

    private val authViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private val userViewModel by lazy {
        val preferences = UserPreferences.getInstance(dataStore)
        ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]
    }


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            // Handle the captured image bitmap
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            // Handle the picked image URI
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEditPhotoProfile.setOnClickListener {
            // make toast coming soon
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
        }

        userViewModel.getName().observe(this) {
            binding.cvName.setText(it)
        }
        userViewModel.getEmail().observe(this){
            binding.cvEmailEdit.setText(it)
        }

        val editText = findViewById<EditText>(R.id.cv_email_edit)
        editText.isFocusable = false
        editText.isClickable = false

        userViewModel.getUserId().observe(this) { userIdBackup = it }

        binding.btnSaveChanges.setOnClickListener { updateUserProfile() }

        authViewModel.userUpdateResponse.observe(this) { response ->
            handleUpdateResponse(response)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            cameraLauncher.launch(takePictureIntent)
        } else {
            Log.e("EditProfileActivity", "No camera app available to handle the intent")
        }
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(pickPhotoIntent)
    }

    private fun updateUserProfile() {
            val newName = binding.cvName.text.toString()
            userViewModel.saveName(newName)

            authViewModel.updateUser(id = userIdBackup,newName)
    }

    private fun handleUpdateResponse(response: ResponseRegister?) {
        if (response != null) {
            Log.d("EditProfileActivity", "Update user response: ${response}")
            response.data?.user?.let {
                userViewModel.saveName(it.name)
                userViewModel.updateName(it.name) // Update the LiveData

                // Update the displayed data
                binding.cvName.setText(it.name)
            }
        } else {
            Log.d("EditProfileActivity", "Update user request not successful")
        }
        showDialog()
    }

    private fun showDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null)
        val builder = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Go To Home") { _, _ ->
               finish()
            }
        builder.show()
    }
}
