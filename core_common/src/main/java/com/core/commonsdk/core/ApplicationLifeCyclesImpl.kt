package com.core.commonsdk.core

import android.app.Application
import android.content.Context
import com.core.basemvvm.BaseApplication.Companion.instance
import com.core.basemvvm.integration.AppLifeCycles
import com.core.commonsdk.R
import com.core.commonsdk.utils.MyLogger
import com.core.commonsdk.utils.log.YDLoggerSave
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.io.File


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
//        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
//            .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
//            .methodCount(0) // (Optional) How many method line to show. Default 2
//            .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
//            .tag("villa") // (Optional) Global tag for every log. Default PRETTY_LOGGER
//            .build()
//        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
//            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return true
//            }
//        })

        initLog()

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.public_backgroundColor)//全局设置主题颜色
            layout.setEnableFooterFollowWhenNoMoreData(true)//是否在全部加载结束之后Footer跟随内容
            ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f).setFinishDuration(100)
        }
    }

    private fun initLog() {
        val diskPath = instance().getExternalFilesDir(null)
            ?.absolutePath
        val path = diskPath + File.separatorChar + "hrt_log"
        YDLoggerSave.ddLog().setLogSavePath(path)
        YDLoggerSave.ddLog().writeToFileEnable(instance())
        MyLogger.getLogger().start()
    }

    override fun onTerminate(application: Application) {
        Logger.i("${application.javaClass.simpleName} onCreate")
    }
}