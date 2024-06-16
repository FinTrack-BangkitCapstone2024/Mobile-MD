package com.example.fintrack_bangkitcapstone2024.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.FragmentReportMontlyBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AnalyzeDataViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportMontlyFragment : Fragment() {
    private lateinit var binding: FragmentReportMontlyBinding
    private lateinit var weeklyDataViewModel: AnalyzeDataViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barChart = view.findViewById<BarChart>(R.id.barChart)

        // Set chart properties
        barChart.setDrawGridBackground(false)
        barChart.setDrawBorders(false)
        barChart.description = null
        barChart.legend.isEnabled = false
        barChart.setBackgroundColor(Color.WHITE) // Set the background color to white

        // Move the view to the next 6 entries
        barChart.moveViewToX(barChart.lowestVisibleX + 6)

        // Move the view to the previous 6 entries
        barChart.moveViewToX(barChart.lowestVisibleX - 6)

        val xAxis = barChart.xAxis
        xAxis.textColor = Color.DKGRAY // Set the color of the x-axis labels to dark gray
        xAxis.setDrawGridLines(false) // Remove grid lines

        barChart.axisLeft.textColor =
            Color.DKGRAY // Set the color of the left y-axis labels to dark gray
        barChart.axisLeft.setDrawGridLines(false) // Remove grid lines

        barChart.axisRight.setDrawLabels(false)
        barChart.axisRight.setDrawGridLines(false)

        // Check if barChart.data is null, and if it is, initialize it
        if (barChart.data == null) {
            barChart.data = BarData()
        }

        val valueFormatter = DefaultValueFormatter(0) // Set the number of decimal places
        barChart.data.setValueFormatter(valueFormatter)
        barChart.data.setValueTextColor(Color.DKGRAY) // Set the color of the value labels to dark gray
        barChart.data.setValueTextSize(12f) // Set the size of the value labels
        // Add animation
        barChart.animateY(1000) // Animate the y values for 1000 milliseconds

        // Get an instance of UserPreferences
        val preferences = UserPreferences.getInstance(requireContext().dataStore)

        // Enable dragging on the chart
        barChart.setDragEnabled(true)

        // Get an instance of ViewModelFactory
        val factory = ViewModelFactory(preferences)

        // Use ViewModelFactory to get an instance of AnalyzeDataViewModel
        weeklyDataViewModel = ViewModelProvider(this, factory).get(AnalyzeDataViewModel::class.java)

        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userViewModel.getUsahaId().observe(viewLifecycleOwner) {
            Log.d("ReportWeeklyFragment", "User ID: $it")
            weeklyDataViewModel.fetchAnalizeData(it)
        }

        val entries = mutableListOf<BarEntry>()
        for (i in 0 until 6) {
            entries.add(BarEntry(i.toFloat(), (i + 1) * 10f))
        }
        val dataSet = BarDataSet(entries, "Sample Data")
        val data = BarData(dataSet)
        barChart.data = data
        barChart.invalidate() // refresh the chart

        weeklyDataViewModel.monthlyData.observe(viewLifecycleOwner) { monthlyData ->
            if (monthlyData != null) {
                val pengeluaran = monthlyData.pengeluaran
                val masukan = monthlyData.masukan
                val bulan = monthlyData.bulan

                if (bulan != null) {
                    // Create an instance of MonthValueFormatter and pass the list of months to it
                    val monthValueFormatter = MonthValueFormatter(bulan)

                    // Apply the monthValueFormatter to the xAxis
                    xAxis.valueFormatter = monthValueFormatter
                } else {
                    Log.d("ReportMontlyFragment", "Month data is null")
                }

                if (pengeluaran != null && masukan != null && bulan != null) {
                    val incomeEntries = mutableListOf<BarEntry>()
                    val expenseEntries = mutableListOf<BarEntry>()

                    // Loop through each month
                    for (i in bulan.indices) {
                        val currentBulan = bulan[i]
                        val currentPengeluaran = pengeluaran[i]
                        val currentMasukan = masukan[i]

                        // Add entries to the lists
                        incomeEntries.add(
                            BarEntry(
                                i.toFloat(),
                                currentMasukan.toFloat()
                            )
                        )
                        expenseEntries.add(
                            BarEntry(
                                i.toFloat() + 0.3f, // Add a small offset for the X position
                                currentPengeluaran.toFloat()
                            )
                        )
                    }

                    val incomeDataSet = BarDataSet(incomeEntries, "Pemasukan").apply {
                        color = Color.parseColor("#7BAEDD") // Set color to red
                    }

                    val expenseDataSet = BarDataSet(expenseEntries, "Pengeluaran").apply {
                        color = Color.parseColor("#153C60") // Set color to blue
                    }

                    val data = BarData(incomeDataSet, expenseDataSet).apply {
                        barWidth = 0.3f // set the width of each bar
                    }

                    val groupSpace = 0.4f
                    val barSpace = 0.02f
                    val barWidth = 0.3f
                    data.barWidth = barWidth

                    barChart.data = data
                    barChart.groupBars(0f, groupSpace, barSpace)
                    barChart.invalidate() // refresh the chart
                } else {
                    Log.d("ReportMontlyFragment", "One or more data lists are null")
                }
            } else {
                Log.d("ReportMontlyFragment", "Failed to fetch data")
            }
        }
    }

    class MonthValueFormatter(private val months: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return months.getOrNull(value.toInt()) ?: value.toString()
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