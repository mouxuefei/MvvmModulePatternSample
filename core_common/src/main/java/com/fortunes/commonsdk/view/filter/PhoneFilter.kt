package com.fortunes.commonsdk.view.filter

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

/**
 * @auther:MR-Cheng
 * @date: 2018/12/24
 * @description:限制输入框只能输入1开头的数字
 * @parameter:
 */

class PhoneFilter : InputFilter{
    override fun filter(source: CharSequence, p1: Int, p2: Int, p3: Spanned, p4: Int, p5: Int): CharSequence? {
        val regex =  "[1]"
        val p = Pattern.compile(regex)
        return if (source.isNotEmpty() && p4 == 0){
            val s = source.toString().substring(0,1)
            val m = p.matcher(s)
            if (m.matches()) null else ""
        }else{
            null
        }
    }

}