package com.example.fintrack_bangkitcapstone2024

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack_bangkitcapstone2024.databinding.ActivityAddTransaksiBinding
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import java.text.NumberFormat
import java.util.Locale

class AddTransaksiActivity : AppCompatActivity() {
    private var amount: Long = 100000
    private lateinit var binding: ActivityAddTransaksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable Activity Transitions. Optionally enable Activity transitions in your
        // theme with <item name=”android:windowActivityTransitions”>true</item>.
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        // Set the transition name, which matches Activity A’s start view transition name, on
        // the root view.
        findViewById<View>(android.R.id.content).transitionName = "shared_element_container"

        // Attach a callback used to receive the shared elements from Activity A to be
        // used by the container transform transition.
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        // Set this Activity’s enter and return transition to a MaterialContainerTransform
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 300L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 250L
        }

        super.onCreate(savedInstanceState)
        binding = ActivityAddTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
    }}