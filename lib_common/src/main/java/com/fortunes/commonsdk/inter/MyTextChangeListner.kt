package com.fortunes.commonsdk.inter

import android.text.Editable
import android.text.TextWatcher

/**
 * @FileName: MyTextChangeListner.java
 * @author: villa_mou
 * @date: 04-11:14
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
abstract class MyTextChangeListner: TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
    override fun afterTextChanged(s: Editable?) {
    }
}