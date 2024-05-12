package com.sugarygary.instory.ui.components


import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.sugarygary.instory.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        hint = context.getString(R.string.email)
        isSingleLine = true
        typeface = Typeface.SANS_SERIF
        doOnTextChanged { text, _, _, _ ->
            error =
                if (text.isNullOrEmpty() || !text.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
                    context.getString(R.string.email_invalid)
                } else {
                    null
                }
        }
        setAutofillHints(AUTOFILL_HINT_EMAIL_ADDRESS)
        minHeight = 48
    }
}