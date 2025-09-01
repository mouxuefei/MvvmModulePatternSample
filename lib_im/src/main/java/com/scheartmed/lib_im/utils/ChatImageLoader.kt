package com.scheartmed.lib_im.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.LruCache
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.fortunes.commonsdk.utils.ScreenUtils
import com.scheartmed.lib_im.R


object ChatImageLoader {


    // 缓存图片宽高比例，避免重复计算
    private val sizeCache: LruCache<String, Pair<Int, Int>> = LruCache(100)

    /**
     * 加载普通图片（等比例 + 圆角）
     */
    fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        maxWidth: Int,
        radiusDp: Int = 8
    ) {
        // 清空旧图片，避免 RecyclerView 复用错位
        Glide.with(context).clear(imageView)
        imageView.setImageDrawable(null)

        val cachedRatio = sizeCache.get(url)
        if (cachedRatio != null) {
            setImageViewSizeByRatio(imageView, cachedRatio.first, cachedRatio.second)
            Glide.with(context)
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.default_img_failed)
                        .transform(RoundedCorners(dp2px(context, radiusDp)))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                )
                .into(imageView)
        } else {
            // 未缓存比例，第一次加载
            Glide.with(context)
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.default_img_failed)
                        .transform(RoundedCorners(dp2px(context, radiusDp)))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                )
                .listener(object : RequestListener<Drawable> {


                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean = false

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let {
                            val w = it.intrinsicWidth
                            val h = it.intrinsicHeight
                            if (w > 0 && h > 0) {
                                if (w > h) {
                                    var scaledH: Int =
                                        ScreenUtils.dp2px(100f)
                                    var scaledW = w * scaledH / h
                                    if (scaledW > maxWidth) {
                                        scaledW = maxWidth
                                        scaledH = h * scaledW / w
                                    }
                                    sizeCache.put(url, Pair(scaledW, scaledH))
                                    setImageViewSizeByRatio(imageView, scaledW, scaledH)
                                } else {
                                    val scaledW: Int =
                                        ScreenUtils.dp2px(100f)
                                    val scaledH = h * scaledW / w //计算出按比缩放后的宽度
                                    sizeCache.put(url, Pair(scaledW, scaledH))
                                    setImageViewSizeByRatio(imageView, scaledW, scaledH)
                                }

                            }


                        }
                        return false // Glide 继续设置图片
                    }
                })
                .into(imageView)
        }
    }

    /**
     * 加载 GIF 图片
     */
    fun loadGif(
        context: Context,
        url: String,
        imageView: ImageView,
        maxWidth: Int,
    ) {
        // 1. 清空旧图
        Glide.with(context).clear(imageView)
        imageView.setImageDrawable(null)
        val cachedRatio = sizeCache.get(url)

        if (cachedRatio != null) {
            setImageViewSizeByRatio(imageView, cachedRatio.first, cachedRatio.second)
            Glide.with(context)
                .asGif()
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.default_img_failed)
//                        .transform(RoundedCorners(dp2px(context, radiusDp)))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                )
                .into(imageView)
        } else {
            Glide.with(context)
                .asGif()
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.default_img_failed)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                )
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: GifDrawable,
                        model: Any,
                        target: Target<GifDrawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        // 如果还没有缓存比例，可以缓存 GIF 尺寸比例
                        resource?.let {
                            val w = it.intrinsicWidth
                            val h = it.intrinsicHeight
                            if (w > 0 && h > 0) {
                                if (w > h) {
                                    var scaledH: Int =
                                        ScreenUtils.dp2px(120f)
                                    var scaledW = w * scaledH / h
                                    if (scaledW > maxWidth) {
                                        scaledW = maxWidth
                                        scaledH = h * scaledW / w
                                    }
                                    sizeCache.put(url, Pair(scaledW, scaledH))
                                    setImageViewSizeByRatio(imageView, scaledW, scaledH)
                                } else {
                                    val scaledW: Int =
                                        ScreenUtils.dp2px(120f)
                                    val scaledH = h * scaledW / w //计算出按比缩放后的宽度
                                    sizeCache.put(url, Pair(scaledW, scaledH))
                                    setImageViewSizeByRatio(imageView, scaledW, scaledH)
                                }
                            }
                        }
                        return false // Glide 继续设置图片
                    }
                })
                .into(imageView)
        }


    }

    /**
     * 根据比例动态设置 ImageView 尺寸
     */
    private fun setImageViewSizeByRatio(
        imageView: ImageView,
        width: Int,
        height: Int,
    ) {
        val lp = imageView.layoutParams
        lp.width = width
        lp.height = height
        imageView.layoutParams = lp
    }

    private fun dp2px(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density + 0.5f).toInt()
    }
}