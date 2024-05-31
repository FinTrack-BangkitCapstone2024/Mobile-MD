package com.example.fintrack_bangkitcapstone2024

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityCreateBusinessBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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
    private fun showDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("IS YOUR BUSINESS NEW?")
            .setMessage("Has your business been running for some time and has financial records?")
//            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
//                // Respond to neutral button press
//            }
            .setNegativeButton("No") { dialog, which ->
                startActivity(Intent(this@CreateBusinessActivity, MainActivity::class.java))
            }
            .setPositiveButton("Yes") { dialog, which ->
                intent = Intent(this, ImportActivity::class.java)
                startActivity(intent)
            }
            .show()
    }
}