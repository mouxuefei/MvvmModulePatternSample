package com.fortunes.commonsdk.utils

import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.blankj.utilcode.util.KeyboardUtils

/**
 * @auther:MR-Cheng
 * @date: 2018/11/22
 * @description:
 * @parameter:隐藏键盘工具类
 */
object HideKeyBoardUtil {

    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false
    }


    fun hideKeyboard(v: View?, event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (isShouldHideKeyboard(v, event)) {
                v?.let { KeyboardUtils.hideSoftInput(it) }
            }
        }
    }

}
