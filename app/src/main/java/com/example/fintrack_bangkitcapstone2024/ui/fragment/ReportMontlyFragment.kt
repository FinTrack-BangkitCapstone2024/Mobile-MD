package com.example.fintrack_bangkitcapstone2024.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.databinding.FragmentReportMontlyBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AnalyzeDataViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportMontlyFragment : Fragment() {
    private lateinit var binding: FragmentReportMontlyBinding
    private lateinit var monthlyDataViewModel: AnalyzeDataViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get an instance of UserPreferences
        val preferences = UserPreferences.getInstance(requireContext().dataStore)

        // Get an instance of ViewModelFactory
        val factory = ViewModelFactory(preferences)

        // Use ViewModelFactory to get an instance of AnalyzeDataViewModel
        monthlyDataViewModel =
            ViewModelProvider(this, factory).get(AnalyzeDataViewModel::class.java)

        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userViewModel.getUsahaId().observe(viewLifecycleOwner) {
            Log.d("ReportMontlyFragment", "User ID: $it")
            monthlyDataViewModel.fetchAnalizeData(it)
        }

        monthlyDataViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.overlay.visibility = View.VISIBLE
                binding.progressIndicatorReport.visibility = View.VISIBLE
            } else {
                binding.overlay.visibility = View.GONE
                binding.progressIndicatorReport.visibility = View.GONE
            }
        }




        // Observe the monthlyData LiveData
        monthlyDataViewModel.monthlyData.observe(viewLifecycleOwner) { monthlyData ->
            if (monthlyData != null) {
                Log.d("ReportMontlyFragmentMonthlyData", "Monthly Data: $monthlyData")
                val pengeluaran = monthlyData.pengeluaran
                val masukan = monthlyData.masukan
                val tanggal = monthlyData.tanggal

                // make log for check data tanggal
//                Log.d("ReportMontlyFragmentDate", "Tanggal: $tanggal")
//                Log.d("ReportMontlyFragmentPengluaran", "Pengeluaran: $pengeluaran")
//                Log.d("ReportMontlyFragmentMasukan", "Masukan: $masukan")

                val incomeEntries = mutableListOf<Entry>()
                val expenseEntries = mutableListOf<Entry>()

                // Loop through each day
                for (i in tanggal?.indices ?: emptyList()) {
                    val currentTanggal = tanggal?.get(i)
                    val currentPengeluaran = pengeluaran?.get(i) ?: 0
                    val currentMasukan = masukan?.get(i) ?: 0
//                    Log.d(
//                        "ReportMontlyFragment",
//                        "Tanggal: $currentTanggal, Income: $currentMasukan, Expense: $currentPengeluaran"
//                    )

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
                binding.lineChart.data = data

                // Refresh the chart
                binding.lineChart.invalidate()

                // Set the x-axis value formatter
                binding.lineChart.xAxis.valueFormatter = object : ValueFormatter() {
                    private val months = tanggal?.toTypedArray()

                    override fun getFormattedValue(value: Float): String {
                        return months?.getOrNull(value.toInt()) ?: value.toString()
                    }
                }

                // Customize the grid lines
                binding.lineChart.xAxis.setDrawGridLines(false) // Disable vertical grid lines
                binding.lineChart.axisLeft.setDrawGridLines(true) // Enable horizontal grid lines on left Y-axis
                binding.lineChart.axisRight.setDrawGridLines(true) // Enable horizontal grid lines on right Y-axis
                binding.lineChart.axisLeft.gridColor =
                    Color.parseColor("#E3E9ED") // Set left Y-axis grid color to gray
                binding.lineChart.axisRight.gridColor =
                    Color.parseColor("#E3E9ED") // Set right Y-axis grid color to gray
            } else {
                Log.d("ReportMontlyFragment", "Failed to fetch data")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportMontlyBinding.inflate(inflater, container, false)
        return binding.root
    }
}
