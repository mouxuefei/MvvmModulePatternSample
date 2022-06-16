package com.fortunes.commonsdk.network

import com.fortunes.commonsdk.network.api.RequestHandler
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/***
 * Created by mou on 2018/8/20.
 * 网络拦截器
 */

class NetInterceptor(private val handler: RequestHandler?) : Interceptor {
    /**
     * 网络请求拦截方法
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (handler != null) {
            request = handler.onBeforeRequest(request, chain)
        }
        val response = chain.proceed(request)
        if (handler != null) {
            return handler.onAfterRequest(response, chain)
        }

        return response
    }

}