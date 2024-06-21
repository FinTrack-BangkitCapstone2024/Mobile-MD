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
import com.github.mikephil.charting.components.LimitLine
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
            val incomeEntries = mutableListOf<Entry>()
            val expenseEntries = mutableListOf<Entry>()

            // Tambahkan entri dari monthlyData
            monthlyData?.let {
                for (i in it.tanggal.indices) {
                    incomeEntries.add(Entry(i.toFloat(), it.masukan[i].toFloat()))
                    expenseEntries.add(Entry(i.toFloat(), it.pengeluaran[i].toFloat()))
                }
            }

            // Buat LimitLine setelah menambahkan entri monthlyData
            val limitLine = LimitLine(incomeEntries.size.toFloat()).apply {
                lineWidth = 2f
                lineColor = Color.BLACK
                enableDashedLine(10f, 10f, 0f)
            }

            // Tambahkan LimitLine ke LineChart
            binding.lineChartForcasting.xAxis.addLimitLine(limitLine)

            // Amati forcastingData di dalam pengamat monthlyData
            authViewModel.forcastingData.observe(this) { forcastingData ->
                // Tambahkan entri dari forcastingData
                forcastingData?.let {
                    val start = incomeEntries.size
                    for (i in it.pemasukan.indices) {
                        incomeEntries.add(Entry((start + i).toFloat(), it.pemasukan[i].toFloat()))
                        expenseEntries.add(
                            Entry(
                                (start + i).toFloat(),
                                it.pengeluaran[i].toFloat()
                            )
                        )
                    }
                }

                // Buat set data
                val incomeDataSet = LineDataSet(incomeEntries, "Pemasukan").apply {
                    color = Color.parseColor("#3AB400")
                    lineWidth = 2f
                    setDrawCircles(false)
                    setDrawValues(false)
                }

                val expenseDataSet = LineDataSet(expenseEntries, "Pengeluaran").apply {
                    color = Color.parseColor("#E20000")
                    lineWidth = 2f
                    setDrawCircles(false)
                    setDrawValues(false)
                }

                // Buat LineData dengan set data
                val data = LineData(incomeDataSet, expenseDataSet)

                // Set data ke LineChart
                binding.lineChartForcasting.data = data

                // Segarkan chart
                binding.lineChartForcasting.invalidate()
            }
        }
    }
}