package com.mou.mine.core

import com.mou.basemvvm.base.BaseApplication
import com.mou.mine.di.component.DaggerAppComponent

/***
 * 该类是module单独运行时候需要的Application类，这里我们不需要实现第三方的实例逻辑，只需要注入AppComponent就好
 * 注入代码已经用模版实现，所以这个类不用动
 **/

class App : BaseApplication() {
    override fun injectComponent() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }
}
