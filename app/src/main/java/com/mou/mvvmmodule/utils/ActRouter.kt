package com.mou.mvvmmodule.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

object ActRouter {

    /**
     * 普通跳转
     * @param context Context 或 Activity
     * @param clazz 目标 Activity
     * @param bundle 携带参数
     * @param flags Intent Flags
     */
    fun startActivity(
        context: Context,
        clazz: Class<out Activity>,
        bundle: Bundle? = null,
        flags: Int? = null
    ) {
        val intent = Intent(context, clazz)
        bundle?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        context.startActivity(intent)
    }

    /**
     * 带返回值跳转 (Activity Result API 推荐使用)
     */
    fun startActivityForResult(
        activity: Activity,
        clazz: Class<out Activity>,
        requestCode: Int,
        bundle: Bundle? = null
    ) {
        val intent = Intent(activity, clazz)
        bundle?.let { intent.putExtras(it) }
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * Kotlin 泛型方式跳转
     */
    inline fun <reified T : Activity> Context.startActivity(
        bundle: Bundle? = null,
        flags: Int? = null
    ) {
        val intent = Intent(this, T::class.java)
        bundle?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        startActivity(intent)
    }

    /**
     * 带返回值的泛型跳转
     */
    inline fun <reified T : Activity> Activity.startActivityForResult(
        requestCode: Int,
        bundle: Bundle? = null
    ) {
        val intent = Intent(this, T::class.java)
        bundle?.let { intent.putExtras(it) }
        startActivityForResult(intent, requestCode)
    }
}