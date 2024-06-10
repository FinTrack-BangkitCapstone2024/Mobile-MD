package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityAddTransaksiBinding
import com.example.fintrack_bangkitcapstone2024.request.RequestFinancials
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class AddTransaksiActivity : AppCompatActivity() {
    private var amount: Long = 100000
    private lateinit var binding: ActivityAddTransaksiBinding
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private var getUsahaIdtest: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = UserPreferences.getInstance(dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]
        userViewModel.getUsahaId().observe(this) { usahaId ->
            getUsahaIdtest = usahaId.toString()
        }

        binding.btnCreateTransaksi.setOnClickListener {
            val requestFinancials = RequestFinancials(
                jumlah = binding.tvCountTransaksi.text.toString(),
                description = binding.descriptionTransaksi.editText?.text.toString(),
                tanggal = convertDateToApiFormat(binding.dateTransakasi.editText?.text.toString()),
                tipe = "pengeluaran",
                usahaId = getUsahaIdtest
            )
            authViewModel.createFinancials(requestFinancials)
        }



        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.plusButton.setOnClickListener {
            amount += 10000
            updateAmountDisplay()
        }

        binding.minusButton.setOnClickListener {
            if (amount > 0) {
                amount -= 10000
                updateAmountDisplay()
            }
        }

        binding.tvCountTransaksi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    val cleanString = s.toString().replace("\\D".toRegex(), "")
                    amount = cleanString.toLong()
                }
            }
        })
    }

    private fun updateAmountDisplay() {
        val formattedAmount = NumberFormat.getInstance(Locale("id", "ID")).format(amount)
        binding.tvCountTransaksi.setText("$formattedAmount")
    }

    fun convertDateToApiFormat(date: String): String {
        val inputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate)
    }
}
