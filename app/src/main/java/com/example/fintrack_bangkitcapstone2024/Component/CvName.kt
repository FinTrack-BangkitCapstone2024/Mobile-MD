package com.example.fintrack_bangkitcapstone2024.Component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.fintrack_bangkitcapstone2024.R


class CvName @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener, View.OnFocusChangeListener {

    var isNameValid = false
    private var clearButtonImage: Drawable

    init {
        clearButtonImage =
            ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable
        setOnTouchListener(this)

        inputType = InputType.TYPE_CLASS_TEXT



        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else setButtonDrawables()
            }

            override fun afterTextChanged(s: Editable) {
                validateName()

            }
        })
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

    }


    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
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


    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            validateName()
        }
    }

    private fun validateName() {
        isNameValid = text.toString().trim().isNotEmpty()
        error = if (!isNameValid) {
            resources.getString(R.string.emtyName)
        } else {
            null
        }
    }
}