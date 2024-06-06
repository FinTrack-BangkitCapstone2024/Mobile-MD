package com.example.fintrack_bangkitcapstone2024.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.FragmentReportWeeklyBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportWeeklyFragment : Fragment() {

    private lateinit var binding: FragmentReportWeeklyBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barChart = view.findViewById<BarChart>(R.id.barChart)

        val incomeEntries = listOf(
            BarEntry(0f, 6000f), // Sunday
            BarEntry(1f, 8000f), // Monday
            BarEntry(2f, 5000f), // Tuesday
            BarEntry(3f, 4000f), // Wednesday
            BarEntry(4f, 7000f), // Thursday
            BarEntry(5f, 8000f), // Friday
            BarEntry(10f, 7000f)  // Saturday
        )

        val expenseEntries = listOf(
            BarEntry(0f, 4000f), // Sunday
            BarEntry(1f, 6000f), // Monday
            BarEntry(2f, 6000f), // Tuesday
            BarEntry(3f, 3000f), // Wednesday
            BarEntry(4f, 5000f), // Thursday
            BarEntry(5f, 6000f), // Friday
            BarEntry(6f, 5000f)  // Saturday
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
            valueFormatter = DayOfWeekValueFormatter()
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

    class DayOfWeekValueFormatter : ValueFormatter() {
        private val daysOfWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

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