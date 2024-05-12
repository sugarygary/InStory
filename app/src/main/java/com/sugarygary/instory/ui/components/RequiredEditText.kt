package com.sugarygary.instory.ui.components


import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.sugarygary.instory.R

class RequiredEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        isSingleLine = true
        typeface = Typeface.SANS_SERIF
        doOnTextChanged { text, _, _, _ ->
            error =
                if (text.isNullOrEmpty()) {
                    context.getString(R.string.required_field)
                } else {
                    null
                }
        }
        setAutofillHints(AUTOFILL_HINT_NAME)
        minHeight = 48
    }
}