package com.example.mercadogil.extensions

import android.widget.ImageView
import coil.load
import com.example.mercadogil.R

fun ImageView.tryToLoadImage(imageUrl: String? = null) {
    load(imageUrl) {
        fallback(R.drawable.cart_image)
        error(R.drawable.image_error)
        placeholder(R.drawable.placeholder)
    }
}