package com.example.fintrack_bangkitcapstone2024.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.databinding.FragmentReportYearlyBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AnalyzeDataViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ReportYearlyFragment : Fragment() {
    private var _binding: FragmentReportYearlyBinding? = null
    private val binding get() = _binding!!
    private lateinit var analyzeDataViewModel: AnalyzeDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportYearlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get an instance of UserPreferences
        val preferences = UserPreferences.getInstance(requireContext().dataStore)

        // Get an instance of ViewModelFactory
        val factory = ViewModelFactory(preferences)

        // Use ViewModelFactory to get an instance of AnalyzeDataViewModel
        analyzeDataViewModel =
            ViewModelProvider(this, factory).get(AnalyzeDataViewModel::class.java)

        analyzeDataViewModel.yearlyData.observe(viewLifecycleOwner) { yearlyData ->
            if (yearlyData != null) {
                val pengeluaran = yearlyData.pengeluaran
                val masukan = yearlyData.masukan
                val tahun = yearlyData.tahun

                val incomeEntries = ArrayList<Entry>()
                val expenseEntries = ArrayList<Entry>()

                // Loop through each year
                for (i in tahun?.indices ?: emptyList()) {
                    val currentTahun = tahun?.get(i)
                    val currentPengeluaran = pengeluaran?.get(i) ?: 0
                    val currentMasukan = masukan?.get(i) ?: 0

                    // Add entries to the lists
                    incomeEntries.add(
                        Entry(
                            i.toFloat(),
                            (currentMasukan as? String)?.toFloatOrNull() ?: 0f
                        )
                    )
                    expenseEntries.add(
                        Entry(
                            i.toFloat(),
                            (currentPengeluaran as? String)?.toFloatOrNull() ?: 0f
                        )
                    )
                }

                // Log the entries for debugging
                Log.d("ReportYearlyFragment", "Income Entries: $incomeEntries")
                Log.d("ReportYearlyFragment", "Expense Entries: $expenseEntries")

                // Create LineDataSet and LineData
                val incomeDataSet = LineDataSet(incomeEntries, "Pemasukan").apply {
                    color = Color.BLUE
                    valueTextColor = Color.BLACK
                    valueTextSize = 16f
                    setDrawFilled(true)
                    fillColor = Color.BLUE
                }

                val expenseDataSet = LineDataSet(expenseEntries, "Pengeluaran").apply {
                    color = Color.RED
                    valueTextColor = Color.BLACK
                    valueTextSize = 16f
                    setDrawFilled(true)
                    fillColor = Color.RED
                }

                val lineData = LineData(incomeDataSet, expenseDataSet)

                // Set data to the chart
                binding.lineChart.data = lineData
                binding.lineChart.invalidate() // refresh chart
            } else {
                Log.d("ReportYearlyFragment", "Failed to fetch data")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}