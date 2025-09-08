package com.mou.mvvmmodule.data

import com.core.basemvvm.BaseApplication
import com.mou.mvvmmodule.data.service.LoginService
import com.core.network.BaseUrlConstants
import com.core.network.NetMgr
import com.core.network.provider.BaseNetProvider

/**
 * @FileName: HomeApiManager.java
 * @author: villa_mou
 * @date: 08-10:52
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
object Api {
    val loginService by lazy {
        NetMgr.getRetrofit(
                BaseUrlConstants.getBaseUrl(),
                BaseNetProvider(BaseApplication.instance())
        ).create(LoginService::class.java)
    }
}