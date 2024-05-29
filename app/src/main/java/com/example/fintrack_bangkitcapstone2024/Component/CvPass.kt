package com.example.fintrack_bangkitcapstone2024.Component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.fintrack_bangkitcapstone2024.R


class CvPass @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    var isPasswordValid = false

    private var clearButtonImage: Drawable = ContextCompat.getDrawable(context, R.drawable.baseline_close_24)!!

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        transformationMethod = PasswordTransformationMethod.getInstance()


        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                isPasswordValid = validatePassword(s.toString())
                error = if (isPasswordValid) null else context.getString(R.string.PassMust8C)
                if (s.isNotEmpty()) showClearButton() else setButtonDrawables()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun validatePassword(password: String) = password.length >= 8

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun showClearButton() = setButtonDrawables(endOfTheText = clearButtonImage)



    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonWidth = clearButtonImage.intrinsicWidth
            val isClearButtonClicked = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                event.x < clearButtonWidth + paddingStart
            } else {
                event.x > width - paddingEnd - clearButtonWidth
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        showClearButton()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        text?.clear()
                        setButtonDrawables()
                        return true
                    }
                }
                return true
            }
        }
        return false
    }
}
