package com.fintech.homework.network

import android.widget.ImageView

interface GlideProvider {
    fun loadImage(imageUri: String, imageView: ImageView, isCircle: Boolean)
}