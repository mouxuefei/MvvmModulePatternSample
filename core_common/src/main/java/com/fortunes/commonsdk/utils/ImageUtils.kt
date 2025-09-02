package com.fortunes.commonsdk.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mou.basemvvm.BaseApplication


/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *
 * Created by mou on 2018/8/22.


 * 加载图片工具类
 */

object ImageUtils {
    @SuppressLint("CheckResult")
    fun load(context: Context?, url: String?, imageView: ImageView, placeholderImg: Drawable? = null, errRes: Int = 0) {
        val options = RequestOptions()
//        val drawableCrossFadeFactory =  DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
        if (errRes != 0) {
            options.placeholder(errRes)
                    .error(errRes)
        } else if (placeholderImg != null) {
            options.placeholder(placeholderImg)
                    .error(placeholderImg)
        }
        Glide.with(context ?: BaseApplication.instance())
                .load(url)
                .apply(options)
//                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(imageView)
    }
}