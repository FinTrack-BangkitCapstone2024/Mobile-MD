package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.adapter.ItemTransaksiAdapter
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityReportBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.ui.fragment.ReportMontlyFragment
import com.example.fintrack_bangkitcapstone2024.ui.fragment.ReportWeeklyFragment
import com.example.fintrack_bangkitcapstone2024.ui.fragment.ReportYearlyFragment
import com.example.fintrack_bangkitcapstone2024.utils.CurrencyUtil.toRupiah
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory

class ReportActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    lateinit var tab1: TextView
    lateinit var tab2: TextView
    lateinit var tab3: TextView
    lateinit var select: TextView

    private lateinit var dropdownMenu: AutoCompleteTextView


    private val authViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private lateinit var binding: ActivityReportBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val userLoginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[UserViewModel::class.java]


        userLoginViewModel.getUsahaId().observe(this) { usahId ->
            Log.d("Response", "Usaha ID: $usahId")
            authViewModel.getReport(usahId)
            authViewModel.getFinancialData(usahId)

        }


        authViewModel.dataTodayProfit.observe(this, Observer { response ->
            binding.jumlahIncomeProfit.text = response.data.pemasukan.jumlah.toRupiah()
            binding.jumlahExpenseProfit.text = response.data.pengeluaran.jumlah.toRupiah()
            binding.selisihIncomeProfit.text = response.data.pemasukan.selisih.toString()
            binding.selisihExpenseProfit.text = response.data.pengeluaran.selisih.toString()
            binding.persenIncome.text = "${response.data.pemasukan.persentase}%"
            binding.persenExpense.text = "${response.data.pengeluaran.persentase}%"

        })

        authViewModel.financialData.observe(this, Observer { response ->
            Log.d("MainActivity", "Response: $response")
            response?.data?.let {
                val adapter = ItemTransaksiAdapter(it.toMutableList())
                binding.rvTransaksi.layoutManager = LinearLayoutManager(this)
                binding.rvTransaksi.adapter = adapter

                adapter.sortByAmountDescending()
//                adapter.sortByAmountAscending()
            }
        })

        authViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                binding.overlay.visibility = View.VISIBLE
                binding.progressIndicatorReport.visibility = View.VISIBLE
            } else {
                binding.overlay.visibility = View.GONE
                binding.progressIndicatorReport.visibility = View.GONE
            }
        })


        tab1 = findViewById(R.id.tab1)
        tab2 = findViewById(R.id.tab2)
        tab3 = findViewById(R.id.tab3)
        select = findViewById(R.id.textSelected)

        val tab1 = findViewById<TextView>(R.id.tab1)
        val tab2 = findViewById<TextView>(R.id.tab2)
        val tab3 = findViewById<TextView>(R.id.tab3)


        val dropdownMenu = findViewById<AutoCompleteTextView>(R.id.dropdown_menu_items)

        val filterOptions =
            arrayOf("Terbaru", "Terlama", "Terbesar", "Terkecil")


        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, filterOptions)


        dropdownMenu.setAdapter(adapter)

        dropdownMenu.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()

            // Get the current adapter
            val adapter = binding.rvTransaksi.adapter as? ItemTransaksiAdapter

            // Perform action based on the selected item
            when (selectedItem) {
                "Terbaru" -> {
                    // Sort by newest date
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.d("Dropdown", "Sorting by newest date")
                        adapter?.sortByDateDescending()
                        binding.filterText.setText("Terbaru")

                    }
                }

                "Terlama" -> {
                    // Sort by oldest date
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.d("Dropdown", "Sorting by oldest date")
                        adapter?.sortByDateAscending()
                        binding.filterText.setText("Terlama")
                    }
                }

                "Terbesar" -> {
                    // Sort by amount descending
                    adapter?.sortByAmountDescending()
                    binding.filterText.setText("Terbesar")
                }

                "Terkecil" -> {
                    // Sort by amount ascending
                    adapter?.sortByAmountAscending()
                    binding.filterText.setText("Terkecil")
                }
            }
        }


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
