package com.mou.mvvmmodule.di.module

import android.app.Application
import com.fortunes.commonsdk.network.HttpUrlConstants
import com.fortunes.commonsdk.network.provider.BaseNetProvider
import com.mou.basemvvm.helper.network.NetMgr
import com.mou.mvvmmodule.di.mvvm.model.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module(includes = [
    ActivityModule::class,
    ViewModelModule::class])
class AppModule {
    @Provides
    @Singleton
    fun provideService(application: Application) = NetMgr.getRetrofit(HttpUrlConstants.getBaseUrl(),
        BaseNetProvider(application)
    ).create(
        ApiService::class.java)!!
}