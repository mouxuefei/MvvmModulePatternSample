package com.mou.mvvmmodule.di.module

import android.arch.lifecycle.ViewModel
import com.mou.basemvvm.android.ViewModelKey
import com.mou.mvvmmodule.di.mvvm.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap



@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}