package com.baymax104.chatapp.utils.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/6/16 18:17
 *@Version 1
 */
object ImageAdapter {

    class ImageTarget(view: ImageView) : ImageViewTarget<Bitmap>(view) {
        override fun setResource(resource: Bitmap?) {
            if (resource == null) return
            val imgWidth = ConvertUtils.dp2px(resource.width.toFloat())
            val imgHeight = ConvertUtils.dp2px(resource.height.toFloat())
            val scaleWidth = view.width
            val scaleHeight = scaleWidth * imgHeight / imgWidth
            val bm = ImageUtils.scale(resource, scaleWidth, scaleHeight)
            val params = view.layoutParams
            params.height = scaleHeight
            params.width = scaleWidth
            view.layoutParams = params
            view.setImageBitmap(bm)
        }
    }

    @JvmStatic
    @BindingAdapter("img_url", "img_rounded", "img_fix", "img_size", requireAll = false)
    fun ImageView.img(url: String?, rounded: Boolean, fix: Boolean, size: Int?) {
        if (url == null) {
            Glide.with(this).asBitmap().into(this)
            return
        }

        var options = RequestOptions().skipMemoryCache(true)
        if (rounded) {
            options = options.transform(CenterCrop(), RoundedCorners(ConvertUtils.dp2px(10f)))
        }
        if (size != null) {
            options = options.override(ConvertUtils.dp2px(size.toFloat()))
        }

        val request = Glide.with(this).asBitmap().load(url).apply(options)
        if (fix) {
            request.into(this)
        } else {
            request.into(ImageTarget(this))
        }
    }

    @JvmStatic
    @BindingAdapter("img_res")
    fun ImageView.imgRes(drawable: Drawable) {
        Glide.with(this)
            .load(drawable)
            .into(this)
    }
}