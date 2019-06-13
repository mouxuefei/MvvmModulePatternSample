package com.mou.mvvmmodule.di.module

import com.mou.mvvmmodule.di.mvvm.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector



@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}