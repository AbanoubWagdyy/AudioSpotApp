package com.audiospotapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object ImageUtils {

    private val TAG = ImageUtils::class.java.simpleName

    fun setImageFromUrlIntoImageViewUsingGlide(
        url: String?,
        context: Context?,
        imageView: ImageView,
        withPreLoadingImage: Boolean
    ) {
        setImageFromUrlIntoImageViewUsingGlide(url, context, imageView)
    }

    fun setImageFromUrlIntoImageViewUsingGlide(
        url: String?,
        context: Context?,
        imageView: ImageView
    ) {
        if (url == null || url.isEmpty() || context == null) {
            return
        }
        Glide.with(context)
            .load(url)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .centerCrop()
                    .dontTransform()
            )
            .into(imageView)
    }

    fun setImageBase64IntoImageView(base64: String, parentView: View, targetImageViewID: Int) {
        val decodedbytes = Base64.decode(base64, 0)
        val bpm = BitmapFactory.decodeByteArray(decodedbytes, 0, decodedbytes.size)
        (parentView.findViewById<View>(targetImageViewID) as ImageView).setImageBitmap(bpm)

        return
    }

    fun setImageBase64IntoImageView(base64: String, targetImageView: ImageView) {
        val decodedbytes = Base64.decode(base64, 0)
        val bpm = BitmapFactory.decodeByteArray(decodedbytes, 0, decodedbytes.size)
        targetImageView.setImageBitmap(bpm)

        return
    }
}