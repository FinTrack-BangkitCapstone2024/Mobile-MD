package com.example.fintrack_bangkitcapstone2024.ui.Activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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


        binding.btnCreateBusines.setOnClickListener{
            val requestUsaha = RequestUsaha(
                nama = binding.cvNameBusiness.text.toString(),
                userId = userViewModel.getUserId().value.toString(),
                lokasi = binding.cvLocationBusiness.text.toString(),
                jenis = binding.cvTypeBusiness.text.toString(),

            )
            viewModel.createUsaha(requestUsaha)
            showDialog()
        }
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
            startActivity(Intent(this@CreateBusinessActivity, MainActivity::class.java))
        }
        .setPositiveButton("Yes") { dialog, which ->
            intent = Intent(this, ImportActivity::class.java)
            startActivity(intent)
        }
        .create()

    dialog.setOnShowListener {
        TransitionManager.beginDelayedTransition(dialog.window?.decorView as ViewGroup, materialFade)
    }

    dialog.show()
}
}