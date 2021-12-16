package com.mou.basemvvm.android

import androidx.lifecycle.ViewModelProvider
import com.mou.basemvvm.base.ViewModelFactory
import dagger.Binds
import dagger.Module


@Module
abstract class FactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}