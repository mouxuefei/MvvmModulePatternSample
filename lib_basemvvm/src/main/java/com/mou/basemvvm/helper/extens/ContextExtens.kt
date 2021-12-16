package com.mou.basemvvm.helper.extens

import android.content.Context
import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.annotation.ColorRes
import com.mou.basemvvm.BaseApplication


/***
 * Created by mou on 2018/12/3.
 * Context的扩展类
 */

/**
 * Context的扩展toast方法
 */
fun Context.toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    if (msg.isEmpty()) return
    Toast.makeText(BaseApplication.instance(), msg, Toast.LENGTH_SHORT).show()
}

/**
 * Context的扩展获取color资源
 */
fun Context.getCompactColor(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

/**
 * Context的扩展dp转px
 */
fun Context.dpTo2Px(dpValue: Int): Float {
    val scale = this.resources.displayMetrics.density//获得当前屏幕密度
    return dpValue * scale + 0.5f
}

