package com.mou.mvvmmodule.core

import com.mou.basemvvm.base.BaseApplication
import com.mou.mvvmmodule.di.component.DaggerAppComponent


class App : BaseApplication() {
    override fun injectComponent() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }
}