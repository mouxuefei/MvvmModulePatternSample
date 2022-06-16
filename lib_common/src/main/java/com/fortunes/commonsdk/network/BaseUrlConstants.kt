package com.fortunes.commonsdk.network


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

object BaseUrlConstants {
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
    fun getBaseUrl(): String = DEV_BASE_URL
}