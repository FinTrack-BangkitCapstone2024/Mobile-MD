package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityCreateBusinessBinding
import com.example.fintrack_bangkitcapstone2024.request.RequestUsaha
import com.example.fintrack_bangkitcapstone2024.ui.Activity.auth.dataStore
import com.example.fintrack_bangkitcapstone2024.viewModel.AuthViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.UserPreferences
import com.example.fintrack_bangkitcapstone2024.viewModel.UserViewModel
import com.example.fintrack_bangkitcapstone2024.viewModel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFade


class CreateBusinessActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCreateBusinessBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = UserPreferences.getInstance(dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]


        if (viewModel.isError) {
            Toast.makeText(this, viewModel.message.value, Toast.LENGTH_SHORT).show()
        } else {
            userViewModel.getUserId().observe(this) { userId ->
                binding.btnCreateBusines.setOnClickListener {
                    val requestUsaha = RequestUsaha(
                        nama = binding.cvNameBusiness.text.toString(),
                        userId = userId,
                        lokasi = binding.cvLocationBusiness.text.toString(),
                        jenis = binding.cvTypeBusiness.text.toString(),
                    )
                    viewModel.createUsaha(requestUsaha)
                    viewModel.isErrorBusines.observe(this, Observer { isError ->
                        if (!isError) {
                            showDialog()
                        }
                    })
                }
            }
        }



        userViewModel.getUserId().observe(this) { userId ->
            Log.d("CreateBusinessActivity", "User ID: $userId")
        }

        userViewModel.getUsahaId().observe(this) { usahaId ->
            Log.d("CreateBusinessActivity", "Usaha ID: $usahaId")
        }


        viewModel.usahaId.observe(this, Observer { usahaId ->
            Toast.makeText(this, "Usaha ID: $usahaId", Toast.LENGTH_LONG).show()
            Log.d("Response", "Usaha ID: $usahaId") // Menambahkan log

            userViewModel.saveUsahaId(usahaId)
        })

        viewModel.message.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d("Response", "Message: $message") // Menambahkan log
        })


    }

    // buatkan funtion dialog yanng  didalamnnya memanggil dialog layout_custom_dialog dan menetapkannya  di tengah layar ActivityCreateBusinessBinding
    private fun showDialog() {
        val materialFade = MaterialFade().apply {
            duration = 150L
        }
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("IS YOUR BUSINESS NEW?")
            .setMessage("Has your business been running for some time and has financial records?")
            .setNegativeButton("No") { dialog, which ->
                startActivity(Intent(this, MainActivity::class.java))
            }
            .setPositiveButton("Yes") { dialog, which ->
                intent = Intent(this, ImportActivity::class.java)
                startActivity(intent)
            }
            .create()

        dialog.setOnShowListener {
            TransitionManager.beginDelayedTransition(
                dialog.window?.decorView as ViewGroup,
                materialFade
            )
        }

        dialog.show()
    }
}