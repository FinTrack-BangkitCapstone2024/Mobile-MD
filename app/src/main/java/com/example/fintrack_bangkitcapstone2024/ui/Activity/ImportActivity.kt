package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityImportBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.ImportViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ImportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImportBinding
    private lateinit var viewModel: ImportViewModel
    private var selectedUri: Uri? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ImportViewModel::class.java)
        val userLoginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[UserViewModel::class.java]
        binding.chooseFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, REQUEST_CODE)
        }
        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                binding.overlay.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                binding.overlay.visibility = View.GONE
                binding.progressIndicator.visibility = View.GONE
            }
        })

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null)
        val goToHomeButton = dialogView.findViewById<Button>(R.id.go_to_home)
        goToHomeButton.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)

            }

        }
        dialog = MaterialAlertDialogBuilder (this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        viewModel.uploadSuccess.observe(this, Observer { success ->
            if (success) {
                dialog?.show()
            }
        })

        binding.uploadButton.setOnClickListener {
            userLoginViewModel.getUsahaId().observe(this, Observer { usahaId ->
                selectedUri?.let { uri ->
                    // Get the file name from Uri
                    val fileName = uri.path?.substring(uri.path?.lastIndexOf("/") ?: 0)

                    // Get InputStream from Uri
                    val inputStream = contentResolver.openInputStream(uri)

                    // Create a RequestBody instance from the InputStream and MediaType
                    val requestFile = inputStream?.let { stream ->
                        RequestBody.create("text/csv".toMediaTypeOrNull(), stream.readBytes())
                    }

                    // Create MultipartBody.Part using file request-body and file name
                    val body = requestFile?.let { reqFile ->
                        MultipartBody.Part.createFormData("file", fileName, reqFile)
                    }

                    val usahaIdRequestBody =
                        RequestBody.create("text/plain".toMediaTypeOrNull(), usahaId)

                    // Call the uploadFile function
                    body?.let { viewModel.uploadFile(it, usahaIdRequestBody) }
                }
            })
        }

        binding.useTemplate.setOnClickListener {
            val url = getString(R.string.url_xlsx)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.btnSkip.setOnClickListener {
            // move activity tampa bisa kembali dengan back
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        dialog?.dismiss()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            selectedUri = data?.data
            val inputStream = contentResolver.openInputStream(selectedUri!!)
            val file = File(cacheDir, "data.csv")
            inputStream?.copyTo(file.outputStream())

            // Get the file name and display it
            val fileName = file.name
            binding.selectedFileName.text = fileName

            // Change the button text and hide the LinearLayout
            binding.chooseFile.text = "Choose another file"
            binding.showOr.visibility = View.GONE
        }
    }

    companion object {
        const val REQUEST_CODE = 1
    }
}