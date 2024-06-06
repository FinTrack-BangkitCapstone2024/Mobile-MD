package com.example.fintrack_bangkitcapstone2024.ui.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.FragmentReportYearlyBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ReportYearlyFragment : Fragment() {
    private var _binding: FragmentReportYearlyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportYearlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// Buat data untuk chart
        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, 30f))
        entries.add(Entry(1f, 80f))
        entries.add(Entry(2f, 60f))
        entries.add(Entry(3f, 50f))
// dan seterusnya...

// Buat LineDataSet dan LineData
        val lineDataSet = LineDataSet(entries, "Data Set 1")
        lineDataSet.color = Color.BLUE
        lineDataSet.valueTextColor = Color.BLACK
        lineDataSet.valueTextSize = 16f

// Set shape di bawah garis
        lineDataSet.setDrawFilled(true)

// Gunakan gradien sebagai fill drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            lineDataSet.fillDrawable = context?.let { ContextCompat.getDrawable(it, R.drawable.gradient_chart) }
        } else {
            lineDataSet.fillColor = Color.BLUE
        }

        val lineData = LineData(lineDataSet)

// Set data ke chart
        binding.lineChart.data = lineData
        binding.lineChart.invalidate() // refresh chart
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}