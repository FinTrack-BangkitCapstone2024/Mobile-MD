package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.adapter.ItemTransaksiAdapter
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityMainBinding
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.ui.Activity.profile.ProfileActivity
import com.example.fintrack_bangkitcapstone2024.utils.CurrencyUtil.toRupiah
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UsahaViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    // make binding
    private lateinit var binding: ActivityMainBinding

    private val authViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private val preferences by lazy { UserPreferences.getInstance(dataStore) }
    private val userViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]
    }

    private var dialog: Dialog? = null

    private var getUsahaIdtest: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                binding.overlay.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                binding.overlay.visibility = View.GONE
                binding.progressIndicator.visibility = View.GONE
            }
        })

        binding.btnToInventory.setOnClickListener {
            Toast.makeText(this, "Inventory Coming Soon", Toast.LENGTH_SHORT).show()
        }

        userViewModel.getUsahaId().observe(this) { usahaId ->
            if (usahaId.isEmpty()) {
                showDialogToChooseUsaha()

            } else {
                // Lanjutkan dengan id usaha yang sudah ada
                getUsahaIdtest = usahaId
                Log.d("Response", "Usaha ID: $getUsahaIdtest")
                authViewModel.getFinancialData(getUsahaIdtest)
                authViewModel.getUsahaById(getUsahaIdtest)
            }
        }

        // Observasi LiveData usahaResponse untuk mendapatkan data usaha
        authViewModel.usahaResponse.observe(this, Observer { responseUsaha ->
            responseUsaha?.let {
                binding.nameBusiness.text = it.data.nama
                binding.profileUsaha.text = it.data.nama.first().uppercase().toString()
                binding.tvBalance.text = it.data.balance.toRupiah()
                binding.pengeluaran.text = it.data.totalPengeluaran.toRupiah().replace(",00", "")
                binding.pemasukan.text = it.data.totalPemasukan.toRupiah().replace(",00", "")

                Log.d("MainActivity", "Response: $responseUsaha")
            }
        })



        authViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        authViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                binding.overlay.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                binding.overlay.visibility = View.GONE
                binding.progressIndicator.visibility = View.GONE
            }
        })

        authViewModel.financialData.observe(this, Observer { response ->
            Log.d("MainActivity", "Response: $response")
            response?.data?.let {
                val adapter = ItemTransaksiAdapter(it.toMutableList())
                binding.rvTransaksi.layoutManager = LinearLayoutManager(this)
                binding.rvTransaksi.adapter = adapter
            }
        })

        binding.hideImeId.setOnClickListener {
            if (binding.tvBalance.transformationMethod == null) {
                binding.tvBalance.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.hideImeId.setImageResource(R.drawable.ic_eye)
            } else {
                binding.tvBalance.transformationMethod = null
                binding.hideImeId.setImageResource(R.drawable.ic_hide)
            }
        }

        binding.addInput.setOnClickListener {
            startActivity(Intent(this, AddTransaksiActivity::class.java))
        }

        binding.provfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnToReport.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
        binding.moveToReport.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
        binding.btnToForecasting.setOnClickListener {
            startActivity(Intent(this, ForcastingActivity::class.java))
        }

    }

    private fun showDialogToChooseUsaha() {
        // Check if the dialog is already showing
        if (dialog?.isShowing == true) {
            return
        }

        // Initialize usahaViewModel if the dialog needs to be shown
        val usahaViewModel = UsahaViewModel()

        userViewModel.getUserId().observe(this, Observer { userId ->
            usahaViewModel.getListUsahaByUserId(userId)
        })

        usahaViewModel.listUsaha.observe(this, Observer { response ->
            if (response != null && !isFinishing) {
                binding.overlay.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.VISIBLE
                // If the response is not null, create an AlertDialog with a list of Usaha
                val usahaNames = response.data.usaha.map { it.nama }.toTypedArray()
                var checkedItem = 0 // This will be the index of the selected radio button

                val dialog = AlertDialog.Builder(this)
                    .setTitle("Choose Usaha")
                    .setSingleChoiceItems(usahaNames, checkedItem) { _, which ->
                        // Update the index of the checked item when a radio button is clicked
                        checkedItem = which
                    }
                    .setPositiveButton("OK") { _, _ ->
                        // Save the ID of the selected Usaha when the OK button is clicked
                        val chosenUsahaId = response.data.usaha[checkedItem].id
                        userViewModel.saveUsahaId(chosenUsahaId)
                        Toast.makeText(this, "Usaha berhasil Digantikan menjadi ${response.data.usaha[checkedItem].nama}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    }
                    .create()

                dialog.show()
                binding.overlay.visibility = View.GONE
                binding.progressIndicator.visibility = View.GONE

            }
        })
    }

}
