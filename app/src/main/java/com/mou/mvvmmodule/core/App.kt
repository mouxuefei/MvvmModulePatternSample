package com.mou.mvvmmodule.core

import com.fortunes.commonsdk.utils.SpUtil
import com.mou.basemvvm.BaseApplication


class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        SpUtil.init(this)
    }
}