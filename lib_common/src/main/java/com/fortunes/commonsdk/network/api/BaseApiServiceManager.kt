package com.fortunes.commonsdk.network.api

import com.fortunes.commonsdk.bean.UserInfoBean
import com.fortunes.commonsdk.network.HttpUrlConstants
import com.fortunes.commonsdk.network.bean.BaseBean
import com.fortunes.commonsdk.network.provider.BaseNetProvider
import com.mou.basemvvm.base.BaseApplication
import com.mou.basemvvm.helper.extens.async
import com.mou.basemvvm.helper.network.NetMgr
import io.reactivex.Single

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
 * Created by mou on 2018/12/17.
 * 项目通用的接口请求类
 */
object BaseApiServiceManager {
    private val apiService by lazy {
        NetMgr.getRetrofit(
            HttpUrlConstants.getBaseUrl(),
            BaseNetProvider(BaseApplication.instance())
        ).create(BaseApiService::class.java)
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(): Single<BaseBean<UserInfoBean>> =
            apiService.queryUserInfo()
                    .async()
}