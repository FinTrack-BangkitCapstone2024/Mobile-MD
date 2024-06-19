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
import com.github.mikephil.charting.formatter.ValueFormatter

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
            if (monthlyData != null) {
                Log.d("ReportMontlyFragmentMonthlyData", "Monthly Data: $monthlyData")
                val pengeluaran = monthlyData.pengeluaran
                val masukan = monthlyData.masukan
                val tanggal = monthlyData.tanggal

                // make log for check data tanggal
                Log.d("ReportMontlyFragmentDate", "Tanggal: $tanggal")
                Log.d("ReportMontlyFragmentPengluaran", "Pengeluaran: $pengeluaran")
                Log.d("ReportMontlyFragmentMasukan", "Masukan: $masukan")

                val incomeEntries = mutableListOf<Entry>()
                val expenseEntries = mutableListOf<Entry>()

                // Loop through each day
                for (i in tanggal?.indices ?: emptyList()) {
                    val currentTanggal = tanggal?.get(i)
                    val currentPengeluaran = pengeluaran?.get(i) ?: 0
                    val currentMasukan = masukan?.get(i) ?: 0
                    Log.d(
                        "ReportMontlyFragment",
                        "Tanggal: $currentTanggal, Income: $currentMasukan, Expense: $currentPengeluaran"
                    )

                    // Add entries to the lists
                    incomeEntries.add(
                        Entry(
                            i.toFloat(),
                            currentMasukan.toFloat()
                        )
                    )
                    expenseEntries.add(
                        Entry(
                            i.toFloat(),
                            currentPengeluaran.toFloat()
                        )
                    )
                }

                // Log the entries for debugging
                Log.d("ReportMontlyFragment", "Income Entries: $incomeEntries")
                Log.d("ReportMontlyFragment", "Expense Entries: $expenseEntries")

                // Create the data sets for the income and expense data
                val incomeDataSet = LineDataSet(incomeEntries, "Pemasukan").apply {
                    color = Color.parseColor("#3AB400") // Set color to blue
                    lineWidth = 2f
                    setDrawCircles(false)
                    setDrawValues(false)
                }

                val expenseDataSet = LineDataSet(expenseEntries, "Pengeluaran").apply {
                    color = Color.parseColor("#E20000") // Set color to blue
                    lineWidth = 2f
                    setDrawCircles(false)
                    setDrawValues(false)
                }

                // Create the LineData with the data sets
                val data = LineData(incomeDataSet, expenseDataSet)

                // Set the data to the LineChart
                binding.lineChartForcasting.data = data

                // Refresh the chart
                binding.lineChartForcasting.invalidate()

                // Set the x-axis value formatter
                binding.lineChartForcasting.xAxis.valueFormatter = object : ValueFormatter() {
                    private val months = tanggal?.toTypedArray()

                    override fun getFormattedValue(value: Float): String {
                        return months?.getOrNull(value.toInt()) ?: value.toString()
                    }
                }

                // Customize the grid lines
                binding.lineChartForcasting.xAxis.setDrawGridLines(false) // Disable vertical grid lines
                binding.lineChartForcasting.axisLeft.setDrawGridLines(true) // Enable horizontal grid lines on left Y-axis
                binding.lineChartForcasting.axisRight.setDrawGridLines(true) // Enable horizontal grid lines on right Y-axis
                binding.lineChartForcasting.axisLeft.gridColor =
                    Color.parseColor("#E3E9ED") // Set left Y-axis grid color to gray
                binding.lineChartForcasting.axisRight.gridColor =
                    Color.parseColor("#E3E9ED") // Set right Y-axis grid color to gray
            } else {
                Log.d("ReportMontlyFragment", "Failed to fetch data")
            }
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