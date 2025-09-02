package com.core.commonsdk.utils

import android.content.res.Resources

object ScreenUtils {

    val displayMetrics by lazy { Resources.getSystem().displayMetrics }

    val screenWidth: Int get() = displayMetrics.widthPixels
    val screenHeight: Int get() = displayMetrics.heightPixels

    fun dp2px(dp: Float): Int = (dp * displayMetrics.density + 0.5f).toInt()
    fun px2dp(px: Float): Int = (px / displayMetrics.density + 0.5f).toInt()

    fun sp2px(sp: Float): Int = (sp * displayMetrics.scaledDensity + 0.5f).toInt()
    fun px2sp(px: Float): Int = (px / displayMetrics.scaledDensity + 0.5f).toInt()
}