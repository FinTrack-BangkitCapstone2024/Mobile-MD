package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityImportBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class ImportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chooseFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "text/csv"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.btnSkip.setOnClickListener {
            // move activity tampa bisa kembali dengan back
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
        val uri = data?.data
        val inputStream = contentResolver.openInputStream(uri!!)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = reader.readLine()
        while (line != null) {
            // process the csv line
            line = reader.readLine()
        }

        // Get the file name and display it
        val fileName = uri.path?.substring(uri.path!!.lastIndexOf("/") + 1)
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