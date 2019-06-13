package com.mou.basemvvm.integration

import android.app.Application
import android.content.Context

/***
 *
 * Created by mou on 2018/8/20.
 * Application生命周期接口
 */

interface AppLifeCycles {

    fun attachBaseContext(base: Context)

    fun onCreate(application: Application)

    fun onTerminate(application: Application)
}