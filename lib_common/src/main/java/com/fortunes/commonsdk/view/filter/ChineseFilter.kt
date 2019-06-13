package com.fortunes.commonsdk.view.filter

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern


/**
 * @auther:MR-Cheng
 * @date: 2018/12/24
 * @description: 限制输入框不能输入特殊符号
 * @parameter:
 */

class ChineseFilter : InputFilter{
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {

            val regex =  "[`~!@#_$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）— +|{}【】‘；：”“’。，、？]"
            val p = Pattern.compile(regex)
            val m = p.matcher(source.toString())
        return if (m.find()) "" else null

    }

}