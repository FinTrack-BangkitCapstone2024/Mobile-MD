package com.example.fintrack_bangkitcapstone2024

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityCreateBusinessBinding

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

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_layout)
        dialog.show()
    }


}