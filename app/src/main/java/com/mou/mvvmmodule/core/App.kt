package com.mou.mvvmmodule.core

import com.mou.basemvvm.BaseApplication
import com.mou.mvvmmodule.utils.SpUtil


class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        SpUtil.init(this)
    }
}