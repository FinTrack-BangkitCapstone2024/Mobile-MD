package com.example.fintrack_bangkitcapstone2024.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.databinding.FragmentReportWeeklyBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AnalyzeDataViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportWeeklyFragment : Fragment() {

    private lateinit var binding: FragmentReportWeeklyBinding
    private lateinit var weeklyDataViewModel: AnalyzeDataViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get an instance of UserPreferences
        val preferences = UserPreferences.getInstance(requireContext().dataStore)

        // Get an instance of ViewModelFactory
        val factory = ViewModelFactory(preferences)

        // Use ViewModelFactory to get an instance of AnalyzeDataViewModel
        weeklyDataViewModel =
            ViewModelProvider(this, factory).get(AnalyzeDataViewModel::class.java)

        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userViewModel.getUsahaId().observe(viewLifecycleOwner) {
            Log.d("ReportMontlyFragment", "User ID: $it")
            weeklyDataViewModel.fetchAnalizeData(it)
        }
        weeklyDataViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.overlay.visibility = View.VISIBLE
                binding.progressIndicatorReport.visibility = View.VISIBLE
            } else {
                binding.overlay.visibility = View.GONE
                binding.progressIndicatorReport.visibility = View.GONE
            }
        }

        // Observe the weeklyData LiveData
        weeklyDataViewModel.weeklyData.observe(viewLifecycleOwner) { weeklyData ->
            if (weeklyData != null) {
                Log.d("ReportMontlyFragmentweeklyData", "weekly Data: $weeklyData")
                val pengeluaran = weeklyData.pengeluaran
                val masukan = weeklyData.masukan
                val tanggal = weeklyData.day

                // make log for check data tanggal
                Log.d("ReportMontlyFragmentDate", "Tanggal: $tanggal")
                Log.d("ReportMontlyFragmentPengluaran", "Pengeluaran: $pengeluaran")
                Log.d("ReportMontlyFragmentMasukan", "Masukan: $masukan")

                val incomeEntries = mutableListOf<BarEntry>()
                val expenseEntries = mutableListOf<BarEntry>()

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
                        BarEntry(
                            i.toFloat(),
                            currentMasukan.toFloat()
                        )
                    )
                    expenseEntries.add(
                        BarEntry(
                            i.toFloat(),
                            currentPengeluaran.toFloat()
                        )
                    )
                }

                // Log the entries for debugging
                Log.d("ReportMontlyFragment", "Income Entries: $incomeEntries")
                Log.d("ReportMontlyFragment", "Expense Entries: $expenseEntries")

                // Create the data sets for the income and expense data
                val incomeDataSet = BarDataSet(incomeEntries, "Pemasukan").apply {
                    color = Color.parseColor("#3AB400") // Set color to blue
                    setDrawValues(false)
                }

                val expenseDataSet = BarDataSet(expenseEntries, "Pengeluaran").apply {
                    color = Color.parseColor("#E20000") // Set color to blue
                    setDrawValues(false)
                }

                // Create the LineData with the data sets
                val data = BarData(incomeDataSet, expenseDataSet)

                // Set the data to the barChart
                binding.barChart.data = data

                // Refresh the chart
                binding.barChart.invalidate()

                // Set the x-axis value formatter
                binding.barChart.xAxis.valueFormatter = object : ValueFormatter() {
                    private val months = tanggal?.toTypedArray()

                    override fun getFormattedValue(value: Float): String {
                        return months?.getOrNull(value.toInt()) ?: value.toString()
                    }
                }

                // Customize the grid lines
                binding.barChart.xAxis.setDrawGridLines(false) // Disable vertical grid lines
                binding.barChart.axisLeft.setDrawGridLines(true) // Enable horizontal grid lines on left Y-axis
                binding.barChart.axisRight.setDrawGridLines(true) // Enable horizontal grid lines on right Y-axis
                binding.barChart.axisLeft.gridColor = Color.parseColor("#E3E9ED") // Set left Y-axis grid color to gray
                binding.barChart.axisRight.gridColor = Color.parseColor("#E3E9ED") // Set right Y-axis grid color to gray
            } else {
                Log.d("ReportMontlyFragment", "Failed to fetch data")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }
}