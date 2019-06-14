package com.fortunes.commonsdk.network

import com.fortunes.commonsdk.BuildConfig

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
 * Created by mou on 2018/8/22.
 */

object HttpUrlConstants {
    //开发环境
    private const val DEV_BASE_URL = "https://wanandroid.com/"
    //测试环境
    private const val TEST_BASE_URL = "https://wanandroid.com/"
    //正式环境
    private const val RELIASE_BASE_URL = "https://wanandroid.com/"

    /**
     * 根据不同的打包命令自动查找对应的base_url
     * 1,开发环境打包  gradlew clean assembleVersionDevDebug或者gradlew clean assembleVersionDevRealease
     * 2,测试环境打包  gradlew clean assembleVersionTestDebug或者gradlew clean assembleVersionTestRealease
     * 3,正式环境打包 gradlew clean assembleVersionOnlineRelease
     */
    fun getBaseUrl(): String = if (BuildConfig.VERSION_ONLINE) RELIASE_BASE_URL else{
            if (BuildConfig.VERSION_TEST) TEST_BASE_URL  else DEV_BASE_URL
        }
}