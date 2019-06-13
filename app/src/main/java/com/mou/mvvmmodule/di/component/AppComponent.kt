package com.mou.mvvmmodule.di.component

import android.app.Application
import com.mou.basemvvm.android.FactoryModule
import com.mou.mvvmmodule.core.App
import com.mou.mvvmmodule.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        FactoryModule::class,
        AppModule::class,//app模块
        com.mou.login.di.module.AppModule::class,//添加登录模块组件
        com.mou.mine.di.module.AppModule::class//添加个人中心模块
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: App)
}