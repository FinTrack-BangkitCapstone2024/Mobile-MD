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
import com.example.fintrack_bangkitcapstone2024.databinding.FragmentReportWeeklyBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.example.fintrack_bangkitcapstone2024.viewModel.WeeklyDataViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportWeeklyFragment : Fragment() {

    private lateinit var binding: FragmentReportWeeklyBinding
    private lateinit var weeklyDataViewModel: WeeklyDataViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barChart = view.findViewById<BarChart>(R.id.barChart)

        // Get an instance of UserPreferences
        val preferences = UserPreferences.getInstance(requireContext().dataStore)

        Log.d("UserPreferences", "Data: ${preferences.toString()}")

        // Get an instance of ViewModelFactory
        val factory = ViewModelFactory(preferences)

        // Use ViewModelFactory to get an instance of WeeklyDataViewModel
        weeklyDataViewModel = ViewModelProvider(this, factory).get(WeeklyDataViewModel::class.java)

        weeklyDataViewModel = ViewModelProvider(this).get(WeeklyDataViewModel::class.java)

        weeklyDataViewModel.fetchWeeklyData("vECigkVTb8RdoKslDMvq") // replace with your actual idUsaha

        weeklyDataViewModel.weeklyDataResponse.observe(viewLifecycleOwner, { response ->
            if (response != null) {
                val dataWeekly = response.data
                val pengeluaran = dataWeekly.pengeluaran
                val masukan = dataWeekly.masukan
                val day = dataWeekly.day

                val incomeEntries = mutableListOf<BarEntry>()
                val expenseEntries = mutableListOf<BarEntry>()

                // Loop through each day
                for (i in day.indices) {
                    val currentDay = day[i]
                    val currentPengeluaran = pengeluaran[i]
                    val currentMasukan = masukan[i]

                    // Add entries to the lists
                    incomeEntries.add(BarEntry(i.toFloat(), currentMasukan.toFloat()))
                    expenseEntries.add(BarEntry(i.toFloat(), currentPengeluaran.toFloat()))
                }

                val incomeDataSet = BarDataSet(incomeEntries, "Pemasukan").apply {
                    color = Color.parseColor("#7BAEDD") // Set color to red
                }

                val expenseDataSet = BarDataSet(expenseEntries, "Pengeluaran").apply {
                    color = Color.parseColor("#153C60") // Set color to blue
                }

                val data = BarData(incomeDataSet, expenseDataSet).apply {
                    barWidth = 0.35f // set the width of each bar
                    groupBars(-0.5f, 0.2f, 0.03f) // group bars together with a specified spacing
                }

                binding.barChart.data = data
                binding.barChart.xAxis.apply {
                    valueFormatter = DayOfWeekValueFormatter(day)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(true) // Enable horizontal grid lines
                    gridColor = Color.LTGRAY // Set the color for the grid lines
                    enableGridDashedLine(10f, 10f, 0f) // Set the dashed line style
                }

                binding.barChart.invalidate() // refresh the chart
            }
            else {
                Log.d("ReportWeeklyFragment", "Failed to fetch data")
            }
        })
    }
        class DayOfWeekValueFormatter(private val daysOfWeek: List<String>) : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return daysOfWeek.getOrNull(value.toInt()) ?: value.toString()
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            binding = FragmentReportWeeklyBinding.inflate(inflater, container, false)
            return binding.root
        }
    }