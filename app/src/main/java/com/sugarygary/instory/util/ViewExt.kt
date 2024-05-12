package com.sugarygary.instory.util

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


fun ImageView.glide(imageUrl: String) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 16f
    circularProgressDrawable.centerRadius = 200f
    circularProgressDrawable.start()
    Glide.with(this.rootView).load(imageUrl).apply(
        RequestOptions()
            .placeholder(
                circularProgressDrawable
            ).error(circularProgressDrawable)
    ).into(this)
}
