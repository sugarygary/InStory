package com.sugarygary.instory.util

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan

fun createSpannableString(username: String, description: String): SpannableStringBuilder {
    val spannableBuilder = SpannableStringBuilder()
    val boldSpan = StyleSpan(Typeface.BOLD)
    spannableBuilder.append(username)
    spannableBuilder.setSpan(
        boldSpan,
        0,
        username.length,
        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableBuilder.append("   ")
    spannableBuilder.append(description)
    return spannableBuilder
}