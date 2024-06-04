package com.example.fintrack_bangkitcapstone2024

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.TransitionManager
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityCreateBusinessBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFade


class CreateBusinessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBusinessBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnCreateBusines.setOnClickListener{
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