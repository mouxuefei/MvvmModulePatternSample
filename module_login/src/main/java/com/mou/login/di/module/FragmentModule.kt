package com.mou.login.di.module

import dagger.Module

/***
 * 该类提供整个module中fragment的实例(这里需要注意所拥有该Fragment的Activity必须实现HasSupportFragmentInjector接口)
 * 例如:
 *   @ContributesAndroidInjector
 *   abstract fun contributeXXFragment(): XXFragment
 */

@Module
abstract class FragmentModule {
}