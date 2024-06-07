package com.example.fintrack_bangkitcapstone2024.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fintrack_bangkitcapstone2024.databinding.FragmentReportMontlyBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportMontlyFragment : Fragment() {
    private lateinit var binding: FragmentReportMontlyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incomeEntries = listOf(
            BarEntry(0f, 6000f), // January
            BarEntry(1f, 8000f), // February
            BarEntry(2f, 5000f), // March
            BarEntry(3f, 4000f), // April
            BarEntry(4f, 7000f)  // May
        )

        val expenseEntries = listOf(
            BarEntry(0f, 4000f), // January
            BarEntry(1f, 6000f), // February
            BarEntry(2f, 6000f), // March
            BarEntry(3f, 3000f), // April
            BarEntry(4f, 5000f)  // May
        )

        val incomeDataSet = BarDataSet(incomeEntries, "Pemasukan").apply {
            color = Color.parseColor("#7BAEDD") // Set color to red
        }

        val expenseDataSet = BarDataSet(expenseEntries, "Pengeluaran").apply {
            color = Color.parseColor("#153C60") // Set color to blue
        }

        val data = BarData(incomeDataSet, expenseDataSet).apply {
            barWidth = 0.3f // set the width of each bar
        }

        binding.barChart.data = data
        binding.barChart.xAxis.apply {
            valueFormatter = ReportMontlyFragment.MonthValueFormatter()
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawGridLines(true) // Enable horizontal grid lines
            gridColor = Color.LTGRAY // Set the color for the grid lines
            enableGridDashedLine(10f, 10f, 0f) // Set the dashed line style
        }

        binding.barChart.axisLeft.apply {
            setDrawGridLines(true) // Enable horizontal grid lines
            axisMinimum = 0f
            gridColor = Color.LTGRAY // Set the color for the grid lines
            enableGridDashedLine(10f, 10f, 0f) // Set the dashed line style
        }

        binding.barChart.axisRight.apply {
            setDrawGridLines(false) // Disable right Y-axis grid lines
            isEnabled = false
        }

        binding.barChart.description.isEnabled = false
        binding.barChart.legend.isEnabled = true

        binding.barChart.groupBars(0f, 0.4f, 0.05f) // group bars together with a specified spacing
        binding.barChart.invalidate() // refresh the chart
    }

    class MonthValueFormatter : ValueFormatter() {
        private val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May")

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