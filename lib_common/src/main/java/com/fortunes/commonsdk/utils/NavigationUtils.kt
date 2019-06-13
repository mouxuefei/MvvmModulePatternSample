package com.fortunes.commonsdk.utils

import com.alibaba.android.arouter.launcher.ARouter
import com.fortunes.commonsdk.core.RouterConstants

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
 * Created by mou on 2018/9/19.
 * 关于所有ARouter路由跳转的类
 */
object NavigationUtils {

    /**
     * 去往登录页面
     */
    fun goLoginActivity() {
        ARouter.getInstance().build(RouterConstants.LOGIN_ACTIVITY).navigation()
    }

    /**
     * 去往首页
     */
    fun goMainActivity() {
        ARouter.getInstance().build(RouterConstants.MAIN_ACTIVITY).navigation()
    }

    /**
     *去个人中心
     */
    fun goMineActivity(){
        ARouter.getInstance().build(RouterConstants.MINE_ACTIVITY).navigation()
    }


    /**
     * 去往WebView页面
     * url:加载的网址
     * title:加载的标题
     */
    const val WEB_URL = "url"
    const val WEB_TITLE = "title"
    fun goWebActivity(url: String, title: String) {
        ARouter.getInstance().build(RouterConstants.WEB_ACTIVITY)
                .withString(WEB_URL, url)
                .withString(WEB_TITLE, title)
                .navigation()
    }

}