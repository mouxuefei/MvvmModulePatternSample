package com.mou.mine.di.component

import android.app.Application
import com.mou.basemvvm.android.FactoryModule
import com.mou.mine.core.App
import com.mou.mine.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/***
 * 该类是整个module的dagger需要注入的Conponent
 * 相对应的注入实例注入到对应的Module.class中去就好
 * Activity/Fragment/ViewModel的实例全在AppModule中include，方便主程序注入
 * 这个类也不用动
 **/
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    FactoryModule::class,
    AppModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: App){

    }
}