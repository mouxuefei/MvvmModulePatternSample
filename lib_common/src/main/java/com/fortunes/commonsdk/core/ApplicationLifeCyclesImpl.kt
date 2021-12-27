package com.fortunes.commonsdk.core

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.fortunes.commonsdk.BuildConfig
import com.mou.basemvvm.integration.AppLifeCycles
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


/***
 * You may think you know what the following code does.
 * But you dont. Trust me.
 * Fiddle with it, and youll spend many a sleepless
 * night cursing the moment you thought youd be clever
 * enough to "optimize" the code below.
 * Now close this file and go play with something else.
 */
/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *
 * Created by mou on 2018/9/11.


 */
class ApplicationLifeCyclesImpl : AppLifeCycles {
    override fun attachBaseContext(base: Context) {
        Logger.i("Application attachBaseContext")
    }

    override fun onCreate(application: Application) {
        if (BuildConfig.DEBUG) {//debug模式下初始化
            ARouter.openLog()     // 打印日志
        }
        ARouter.openDebug()
        ARouter.init(application) // 尽可能早,推荐在Application中初始化
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    override fun onTerminate(application: Application) {
        Logger.i("${application.javaClass.simpleName} onCreate")
    }
}