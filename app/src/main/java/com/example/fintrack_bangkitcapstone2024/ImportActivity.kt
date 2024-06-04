package com.example.fintrack_bangkitcapstone2024

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityImportBinding

class ImportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityImportBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}