package com.delitx.furnituremaster.ui.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.delitx.furnituremaster.GlideApp
import com.google.firebase.storage.FirebaseStorage

object ImageUtils {
    private val mStorageReference = FirebaseStorage.getInstance()

    @Composable
    fun loadPicture(
        url: String,
        imageHeight: Int = 1,
        imageWidth: Int = 1,
        @DrawableRes defaultImage: Int
    ): MutableState<Bitmap?> {
        val bitmapState: MutableState<Bitmap?> = remember(url) { mutableStateOf(null) }
        if (bitmapState.value == null) {
            val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.color = MaterialTheme.colors.background.toArgb()
            canvas.drawRect(0f, 0f, 100f, 100f, paint)
            bitmapState.value = bitmap
        }
        if (url.isNotEmpty()) {
            GlideApp.with(LocalContext.current)
                .asBitmap()
                .run {
                    if (imageHeight != 1 || imageWidth != 1) {
                        return@run override(imageWidth, imageHeight)
                    } else {
                        return@run this
                    }
                }
                .load(mStorageReference.getReference(url))
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        bitmapState.value = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
        return bitmapState
    }

    @Composable
    fun loadDrawable(
        @DrawableRes id: Int
    ): MutableState<Bitmap?> {
        val bitmapState: MutableState<Bitmap?> = remember(id) { mutableStateOf(null) }
        GlideApp.with(LocalContext.current)
            .asBitmap()
            .load(id)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmapState.value = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        return bitmapState
    }

    fun loadPicture(url: String, imageView: ImageView, @DrawableRes defaultImage: Int) {
        GlideApp.with(imageView.context)
            .asBitmap()
            .load(mStorageReference.getReference(url))
            .placeholder(defaultImage)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}
