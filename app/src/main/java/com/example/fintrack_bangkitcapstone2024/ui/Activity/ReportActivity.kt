package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityReportBinding
import com.example.fintrack_bangkitcapstone2024.ui.fragment.ReportMontlyFragment
import com.example.fintrack_bangkitcapstone2024.ui.fragment.ReportWeeklyFragment
import com.example.fintrack_bangkitcapstone2024.ui.fragment.ReportYearlyFragment

class ReportActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    lateinit var tab1: TextView
    lateinit var tab2: TextView
    lateinit var tab3: TextView
    lateinit var select: TextView

    private lateinit var binding: ActivityReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        tab1 = findViewById(R.id.tab1)
        tab2 = findViewById(R.id.tab2)
        tab3 = findViewById(R.id.tab3)
        select = findViewById(R.id.textSelected)

        val tab1 = findViewById<TextView>(R.id.tab1)
        val tab2 = findViewById<TextView>(R.id.tab2)
        val tab3 = findViewById<TextView>(R.id.tab3)



        val fragment1 = ReportWeeklyFragment()
        val fragment2 = ReportMontlyFragment()
        val fragment3 = ReportYearlyFragment()

        // Tampilkan fragment pertama secara default
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment1)
        fragmentTransaction.commit()

        tab1.setOnClickListener {
            tab1.setTextColor(resources.getColor(R.color.white))
            tab2.setTextColor(resources.getColor(R.color.black))
            tab3.setTextColor(resources.getColor(R.color.black))
            select.animate().x(0f).setDuration(60)

            // Ganti fragment yang ditampilkan dengan fragment1
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment1)
            fragmentTransaction.commit()
        }

        tab2.setOnClickListener {
            tab1.setTextColor(resources.getColor(R.color.black))
            tab2.setTextColor(resources.getColor(R.color.white))
            tab3.setTextColor(resources.getColor(R.color.black))
            val size = tab2.width
            select.animate().x(size.toFloat()).setDuration(60)

            // Ganti fragment yang ditampilkan dengan fragment2
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment2)
            fragmentTransaction.commit()
        }

        tab3.setOnClickListener {
            tab1.setTextColor(resources.getColor(R.color.black))
            tab2.setTextColor(resources.getColor(R.color.black))
            tab3.setTextColor(resources.getColor(R.color.white))
            val size = tab2.width * 2
            select.animate().x(size.toFloat()).setDuration(60)
            // Ganti fragment yang ditampilkan dengan fragment3
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment3)
            fragmentTransaction.commit()
        }
    }
}