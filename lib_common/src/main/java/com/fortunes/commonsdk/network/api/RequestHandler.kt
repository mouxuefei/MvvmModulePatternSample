package com.fortunes.commonsdk.network.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/***
 *
 * Created by mou on 2018/8/20.
 * 网络请求拦截器
 */

interface RequestHandler {
    /**
     * 发起请求拦截方法
     */
    fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request

    /**
     * 请求结果拦截方法
     */
    @Throws(IOException::class)
    fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response
}