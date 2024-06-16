package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityAddTransaksiBinding
import com.example.fintrack_bangkitcapstone2024.request.RequestFinancials
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTransaksiActivity : AppCompatActivity() {
    private var amount: Int = 100000
    private lateinit var binding: ActivityAddTransaksiBinding
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private var getUsahaIdtest: String = ""
    private var dialog: AlertDialog? = null

    lateinit var tab1: TextView
    lateinit var tab2: TextView
    lateinit var select: TextView

    private var type = "pengeluaran"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tab1 = findViewById(R.id.tab_pengeluaran)
        tab2 = findViewById(R.id.tab_pemasukan)
        select = findViewById(R.id.textSelected2)

        tab1.setOnClickListener {
            tab1.setTextColor(resources.getColor(R.color.gray_text))
            tab2.setTextColor(resources.getColor(R.color.white_text))
            select.animate().x(0f).setDuration(60)
            type = "pengeluaran"

            // ubah warna tvCountTransaksi jadi warna merah
            binding.tvCountTransaksi.setTextColor(resources.getColor(R.color.red))
            binding.rpText.setTextColor(resources.getColor(R.color.red))
        }

        tab2.setOnClickListener {
            tab1.setTextColor(resources.getColor(R.color.white_text))
            tab2.setTextColor(resources.getColor(R.color.gray_text))
            select.animate().x(select.width.toFloat()).setDuration(60)
            type = "pemasukan"

            // ubah warna tvCountTransaksi jadi warna hijau
            binding.tvCountTransaksi.setTextColor(resources.getColor(R.color.green))
            binding.rpText.setTextColor(resources.getColor(R.color.green))
        }


        val preferences = UserPreferences.getInstance(dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]
        userViewModel.getUsahaId().observe(this) { usahaId ->
            getUsahaIdtest = usahaId.toString()
        }


        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null)
        val goToHomeButton = dialogView.findViewById<Button>(R.id.go_to_home)
        goToHomeButton.setOnClickListener {
            finish()
        }


        val titleTextWatcher = createTextWatcher(binding.titleTransaksi)
        val descriptionTextWatcher = createTextWatcher(binding.descriptionTransaksi)
        val dateTextWatcher = createTextWatcher(binding.dateTransakasi)

        binding.dateTransakasi.editText?.setOnClickListener {
            showMaterialDatePicker(this, binding.dateTransakasi.editText!!)
//            showDatePicker(this, binding.dateTransakasi.editText!!) { it.padZero() }
        }

//         Extension function to pad single digit numbers with a leading zero


        binding.titleTransaksi.editText?.addTextChangedListener(titleTextWatcher)
        binding.dateTransakasi.editText?.addTextChangedListener(dateTextWatcher)
        binding.descriptionTransaksi.editText?.addTextChangedListener(descriptionTextWatcher)

        binding.inputCsv.setOnClickListener {
            startActivity(Intent(this, ImportActivity::class.java))
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
                    amount = cleanString.toInt()
                }
            }
        })

        binding.btnCreateTransaksi.setOnClickListener {
            val isAllFieldsFilled = validateFields()

            if (!isAllFieldsFilled) {
                return@setOnClickListener
            }

            val description = binding.descriptionTransaksi.editText?.text.toString()
            val tanggal = binding.dateTransakasi.editText?.text.toString()
            val title = binding.titleTransaksi.editText?.text.toString()

            val requestFinancials = RequestFinancials(
                title = title,
                description = description,
                jumlah = amount,
                tanggal = convertDateToApiFormat(tanggal),
                tipe = type,
                usaha_id = getUsahaIdtest
            )
            authViewModel.createFinancials(requestFinancials)
            dialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setCancelable(false)
                .show()
        }


    }

    override fun onDestroy() {
        dialog?.dismiss()
        super.onDestroy()
    }


    private fun showDatePicker(context: Context, editText: EditText, padZero: (Int) -> String) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate =
                    "${padZero(selectedDay)}/${padZero(selectedMonth + 1)}/$selectedYear"
                editText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    fun showMaterialDatePicker(context: Context, editText: EditText) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setTheme(R.style.MaterialDatePickerTheme)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selection)
            editText.setText(selectedDate)
        }

        datePicker.show(
            (context as AppCompatActivity).supportFragmentManager,
            "MATERIAL_DATE_PICKER"
        )
    }

    private fun createTextWatcher(errorField: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                // Remove error message when user starts typing in the field
                errorField.error = null
            }
        }
    }


//    private fun Int.padZero(): String {
//        return if (this < 10) "0$this" else this.toString()
//    }

    private fun validateFields(): Boolean {
    val description = binding.descriptionTransaksi.editText?.text.toString()
    val tanggal = binding.dateTransakasi.editText?.text.toString()
    val title = binding.titleTransaksi.editText?.text.toString()

    var isAllFieldsFilled = true

    if (title.isEmpty()) {
        binding.titleTransaksi.error = "Field ini harus diisi"
        isAllFieldsFilled = false
    }

    if (description.isEmpty()) {
        binding.descriptionTransaksi.error = "Field ini harus diisi"
        isAllFieldsFilled = false
    }

    if (tanggal.isEmpty()) {
        binding.dateTransakasi.error = "Field ini harus diisi"
        isAllFieldsFilled = false
    } else {
        // Cek format tanggal
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        try {
            dateFormat.parse(tanggal)
        } catch (e: ParseException) {
            binding.dateTransakasi.error = "Format tanggal harus dd/MM/yyyy"
            isAllFieldsFilled = false
        }
    }

    return isAllFieldsFilled
}

    private fun updateAmountDisplay() {
        val formattedAmount = NumberFormat.getInstance(Locale("id", "ID")).format(amount)
        binding.tvCountTransaksi.setText("$formattedAmount")
    }

    fun convertDateToApiFormat(date: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate)
    }
}
