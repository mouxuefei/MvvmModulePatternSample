package com.mou.basemvvm.helper.listener

import android.view.View

/***
 *
 * Created by mou on 2018/8/20.
 * view的点击事件接口类
 */

interface ClickPresenter : View.OnClickListener {
    override fun onClick(v: View)
}