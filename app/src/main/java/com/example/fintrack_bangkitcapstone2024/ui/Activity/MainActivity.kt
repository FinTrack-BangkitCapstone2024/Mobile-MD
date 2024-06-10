package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fintrack_bangkitcapstone2024.adapter.ItemTransaksiAdapter
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityMainBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.ui.Activity.profile.ProfileActivity
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.TransaksiViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

class MainActivity : AppCompatActivity() {

    // as a Dahshboard

    // make binding
    private lateinit var binding: ActivityMainBinding

    private val authViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }




    private val transaksiViewModel by lazy {
        ViewModelProvider(this)[TransaksiViewModel::class.java]
    }


    private var getUsahaIdtest: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {


        val userLoginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[UserViewModel::class.java]

        // Enable Activity Transitions. Optionally enable Activity transitions in your
        // theme with <item name=”android:windowActivityTransitions”>true</item>.
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        // Attach a callback used to capture the shared elements from this Activity to be used
        // by the container transform transition
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        // Keep system bars (status bar, navigation bar) persistent throughout the transition.
        window.sharedElementsUseOverlay = false
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLoginViewModel.getUsahaId().observe(this) { usahId ->
            getUsahaIdtest =usahId
            Log.d("Response", "Usaha ID: $getUsahaIdtest")
            transaksiViewModel.getFinancialData(getUsahaIdtest)
        }




        transaksiViewModel.financialData.observe(this, Observer { response ->
            response?.data?.let {
                val financialDataList = listOf(it)
                val adapter = ItemTransaksiAdapter(financialDataList)
                binding.rvTransaksi.layoutManager = LinearLayoutManager(this)
                binding.rvTransaksi.adapter = adapter
            }
        })


        binding.addInput.setOnClickListener {
            startActivity(Intent(this, AddTransaksiActivity::class.java))
        }

        binding.provfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnToReport.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }

    }
}