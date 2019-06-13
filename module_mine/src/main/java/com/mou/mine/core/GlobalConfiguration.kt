package com.mou.mine.core

import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager
import com.mou.basemvvm.integration.AppLifeCycles
import com.mou.basemvvm.integration.ConfigModule

/***
 * 该类是整个module可以对整个App的Application/Activity/Fragment的生命周期进行逻辑注入
 * 例如我们平时的第三方代码就可以在这里去进行实现
 **/

class GlobalConfiguration : ConfigModule {
    override fun injectAppLifecycle(context: Context, lifeCycles: MutableList<AppLifeCycles>) {
    }

    override fun injectActivityLifecycle(context: Context, lifeCycles: MutableList<Application.ActivityLifecycleCallbacks>) {
    }

    override fun injectFragmentLifecycle(context: Context, lifeCycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>) {
    }
}
