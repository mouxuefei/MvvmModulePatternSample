package com.mou.mine.di.module

import android.app.Application
import com.fortunes.commonsdk.network.HttpUrlConstants
import com.fortunes.commonsdk.network.provider.BaseNetProvider
import com.mou.basemvvm.helper.network.NetMgr
import com.mou.mine.mvvm.model.repository.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/***
 * 该类提供整个module需要的实例，比如网络请求，数据库等等(记得加上@Provide，@Single注解)
 * includes的module提供Activity/Fragment/ViewModel的实例
 **/

@Module(includes = [
    ActivityModule::class,
    FragmentModule::class,
    ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideService(application: Application) = NetMgr.getRetrofit(HttpUrlConstants.getBaseUrl(), BaseNetProvider(application)).create(
        ApiService::class.java)
}