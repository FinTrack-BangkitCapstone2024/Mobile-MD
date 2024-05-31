package com.example.fintrack_bangkitcapstone2024

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityAddTransaksiBinding
import java.text.NumberFormat
import java.util.Locale

class AddTransaksiActivity : AppCompatActivity() {

    private var amount: Long = 100000
    private lateinit var binding: ActivityAddTransaksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.tvCountTransaksi.setText("Rp$formattedAmount")
    }
}