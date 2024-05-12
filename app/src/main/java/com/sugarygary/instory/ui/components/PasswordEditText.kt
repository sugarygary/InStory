package com.sugarygary.instory.ui.components


import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.util.AttributeSet
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.sugarygary.instory.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        hint = context.getString(R.string.password)
        isSingleLine = true
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        typeface = Typeface.SANS_SERIF
        doOnTextChanged { text, _, _, _ ->
            error =
                if (text.isNullOrEmpty() || text.length < 8) {
                    context.getString(R.string.password_invalid)
                } else {
                    null
                }
        }
        setAutofillHints(AUTOFILL_HINT_PASSWORD)
        minHeight = 48
    }
}