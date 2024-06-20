package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityForcastingBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AnalyzeDataViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ForcastingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForcastingBinding
    private val authViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private lateinit var analyzeDataViewModel: AnalyzeDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForcastingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val preferences = UserPreferences.getInstance(dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]
        analyzeDataViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[AnalyzeDataViewModel::class.java]

        userViewModel.getUsahaId().observe(this) {
            authViewModel.getDataForcasting(it)
            analyzeDataViewModel.fetchAnalizeData(it)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        analyzeDataViewModel.monthlyData.observe(this) { monthlyData ->
            val totalPemasukanMonthly = monthlyData?.masukan?.sum() ?: 0
            val totalPengeluaranMonthly = monthlyData?.pengeluaran?.sum() ?: 0
            val balanceMonthly = totalPemasukanMonthly - totalPengeluaranMonthly
            Log.d("ForcastingData", "Monthly Balance: $balanceMonthly")

            // Add balanceMonthly to the line chart
            val balanceEntries = mutableListOf<Entry>()
            balanceEntries.add(Entry(0f, balanceMonthly.toFloat()))
            val balanceDataSet = LineDataSet(balanceEntries, "Balance").apply {
                color = Color.parseColor("#0000FF") // Set color to blue
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
            }
            val data = LineData(balanceDataSet)
            binding.lineChartForcasting.data = data
            binding.lineChartForcasting.invalidate()
        }

        authViewModel.forcastingData.observe(this) { dataForcasting ->
            val totalPemasukanForcasting =
                dataForcasting?.pemasukan?.sumByDouble { it as Double } ?: 0.0
            val totalPengeluaranForcasting =
                dataForcasting?.pengeluaran?.sumByDouble { it as Double } ?: 0.0
            val balanceForcasting = totalPemasukanForcasting - totalPengeluaranForcasting
            Log.d("ForcastingData", "Forcasting Balance: $balanceForcasting")

            // Add balanceForcasting to the line chart
            val balanceEntries = mutableListOf<Entry>()
            balanceEntries.add(Entry(1f, balanceForcasting.toFloat()))
            val balanceDataSet = LineDataSet(balanceEntries, "Balance").apply {
                color = Color.parseColor("#0000FF") // Set color to blue
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
            }
            val data = LineData(balanceDataSet)
            binding.lineChartForcasting.data = data
            binding.lineChartForcasting.invalidate()
        }

        authViewModel.forcastingData.observe(this) { dataForcasting ->
            val totalPemasukanForcasting =
                dataForcasting?.pemasukan?.sumByDouble { it as Double } ?: 0.0
            val totalPengeluaranForcasting =
                dataForcasting?.pengeluaran?.sumByDouble { it as Double } ?: 0.0
            val balanceForcasting = totalPemasukanForcasting - totalPengeluaranForcasting
            Log.d("ForcastingData", "Forcasting Balance: $balanceForcasting")
        }

        analyzeDataViewModel.monthlyData.observe(this) { monthlyData ->
            val totalPemasukanMonthly = monthlyData?.masukan?.sum() ?: 0
            val totalPengeluaranMonthly = monthlyData?.pengeluaran?.sum() ?: 0
            val balanceMonthly = totalPemasukanMonthly - totalPengeluaranMonthly
            Log.d("ForcastingData", "Monthly Balance: $balanceMonthly")
        }
    }
}