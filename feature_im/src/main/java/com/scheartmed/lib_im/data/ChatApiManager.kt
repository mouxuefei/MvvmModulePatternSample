package com.scheartmed.lib_im.data

import com.mou.basemvvm.BaseApplication
import com.mou.network.BaseUrlConstants
import com.mou.network.NetMgr
import com.mou.network.provider.BaseNetProvider
import com.scheartmed.lib_im.data.api.ChatService

/**
 * @FileName: HomeApiManager.java
 * @author: villa_mou
 * @date: 08-10:52
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
object ChatApiManager {
    val apiService by lazy {
        NetMgr.getRetrofit(
                BaseUrlConstants.getBaseUrl(),
                BaseNetProvider(BaseApplication.instance())
        ).create(ChatService::class.java)
    }
}