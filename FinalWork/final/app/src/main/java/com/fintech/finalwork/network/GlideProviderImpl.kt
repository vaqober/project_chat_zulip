package com.fintech.finalwork.network

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fintech.finalwork.R

class GlideProviderImpl : GlideProvider {
    override fun loadImage(imageUri: String, imageView: ImageView, isCircle: Boolean) {

        var glide = Glide.with(imageView).asBitmap()

        glide = if (isCircle) {
            glide.circleCrop()
        } else {
            glide.centerCrop()
        }
        glide
            .load(imageUri)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .fallback(R.drawable.ic_profile)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(imageView)
            .waitForLayout()
    }
}