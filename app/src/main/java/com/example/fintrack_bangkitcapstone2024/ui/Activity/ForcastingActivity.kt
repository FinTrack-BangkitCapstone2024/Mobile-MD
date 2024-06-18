package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityForcastingBinding

class ForcastingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForcastingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForcastingBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}