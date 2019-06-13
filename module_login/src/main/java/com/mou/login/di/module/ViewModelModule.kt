package com.mou.login.di.module

import android.arch.lifecycle.ViewModel
import com.mou.basemvvm.android.ViewModelKey
import com.mou.login.mvvm.viewmodel.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/***
 * 该类主要提供整个module的ViewModel的实例
 * 例如:
 *  @Binds
 *  @IntoMap
 *  @ViewModelKey(HomeViewModel::class)
 *  abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
 **/

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}