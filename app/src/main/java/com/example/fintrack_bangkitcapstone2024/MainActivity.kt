package com.example.fintrack_bangkitcapstone2024

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // as a login page

    // make binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}