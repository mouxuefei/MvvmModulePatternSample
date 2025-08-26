package com.mou.mvvmmodule.data

import com.fortunes.commonsdk.network.BaseUrlConstants
import com.fortunes.commonsdk.network.provider.BaseNetProvider
import com.mou.basemvvm.BaseApplication
import com.fortunes.commonsdk.network.NetMgr
import com.mou.mvvmmodule.data.api.ApiService

/**
 * @FileName: HomeApiManager.java
 * @author: villa_mou
 * @date: 08-10:52
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
object HomeApiManager {
    val apiService by lazy {
        NetMgr.getRetrofit(
                BaseUrlConstants.getBaseUrl(),
                BaseNetProvider(BaseApplication.instance())
        ).create(ApiService::class.java)
    }
}