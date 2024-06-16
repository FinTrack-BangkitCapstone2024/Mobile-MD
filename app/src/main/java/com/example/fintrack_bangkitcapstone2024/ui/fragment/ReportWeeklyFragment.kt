package com.example.fintrack_bangkitcapstone2024.ui.fragment

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
import com.example.fintrack_bangkitcapstone2024.viewModel.AnalyzeDataViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportWeeklyFragment : Fragment() {

    private lateinit var binding: FragmentReportWeeklyBinding
    private lateinit var weeklyDataViewModel: AnalyzeDataViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barChart = view.findViewById<BarChart>(R.id.barChart)

        // Get an instance of UserPreferences
        val preferences = UserPreferences.getInstance(requireContext().dataStore)

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

        // Observe the weeklyData LiveData
        weeklyDataViewModel.weeklyData.observe(viewLifecycleOwner) { weeklyData ->
            if (weeklyData != null) {
                Log.d("ReportWeeklyFragmentWeeklyData", "Weekly Data: $weeklyData")
                val pengeluaran = weeklyData.pengeluaran
                val masukan = weeklyData.masukan
                val day = weeklyData.day

                Log.d("ReportWeeklyFragmentPengluaran", "Pengeluaran: $pengeluaran")
                Log.d("ReportWeeklyFragmentMasukan", "Masukan: $masukan")

                val incomeEntries = mutableListOf<BarEntry>()
                val expenseEntries = mutableListOf<BarEntry>()

                // Loop through each day
                for (i in day?.indices ?: emptyList()) {
                    val currentDay = day?.get(i)
                    val currentPengeluaran = pengeluaran?.get(i) ?: 0
                    val currentMasukan = masukan?.get(i) ?: 0
                    Log.d("ReportWeeklyFragment", "Day: $currentDay, Income: $currentMasukan, Expense: $currentPengeluaran")

                    // Add entries to the lists
                    incomeEntries.add(
                        BarEntry(
                            i.toFloat(),
                            (currentMasukan as? String)?.toFloatOrNull() ?: 0f
                        )
                    )
                    expenseEntries.add(
                        BarEntry(
                            i.toFloat(),
                            (currentPengeluaran as? String)?.toFloatOrNull() ?: 0f
                        )
                    )
                }

                // Log the entries for debugging
                Log.d("ReportWeeklyFragment", "Income Entries: $incomeEntries")
                Log.d("ReportWeeklyFragment", "Expense Entries: $expenseEntries")

                // Rest of your code...
            } else {
                Log.d("ReportWeeklyFragment", "Failed to fetch data")
            }
        }
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