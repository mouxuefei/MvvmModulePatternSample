package com.fortunes.commonsdk.utils

import com.blankj.rxbus.RxBus

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
 * Created by mou on 2018/12/7.
 * RxBus注册监听类
 */
object RxBusUtils {

    /**
     * 取消RxBus的监听
     */
    fun cancelSubscriber(subscriber: Any) {
        RxBus.getDefault().unregister(subscriber)
    }

    private const val HOME_REFRESH = "home_refresh"
    /**
     * 发送首页刷新
     */
    fun postHomeRefresh(refresh: Boolean) {
        RxBus.getDefault().post(refresh, HOME_REFRESH)
    }
    /**
     * 监听首页刷新
     */
    fun subscribeHomeRefresh(subscriber: Any, callback: (Boolean) -> Unit) {
        RxBus.getDefault().subscribe(subscriber, HOME_REFRESH, object : RxBus.Callback<Boolean>() {
            override fun onEvent(isLoginSuccess: Boolean?) {
                isLoginSuccess?.let(callback)
            }
        })
    }
}
