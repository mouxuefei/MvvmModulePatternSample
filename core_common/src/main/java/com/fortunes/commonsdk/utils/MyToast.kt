package com.fortunes.commonsdk.utils

import android.widget.Toast
import com.mou.basemvvm.BaseApplication

/**
 * 解决小米吐司带app名称问题
 */
object MyToast {
    private var toast: Toast? = null
    fun showToast(desc: String) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.instance(), null, Toast.LENGTH_SHORT)
            toast?.setText(desc)
        } else {
            toast?.setText(desc)
            toast?.duration = Toast.LENGTH_SHORT
        }
        toast?.show()
    }
}