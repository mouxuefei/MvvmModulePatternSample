package com.fortunes.commonsdk.network.provider

import android.content.Context
import com.fortunes.commonsdk.BuildConfig
import com.fortunes.commonsdk.network.bean.HttpException
import com.fortunes.commonsdk.network.HttpStatusConstants
import com.fortunes.commonsdk.network.bean.BaseBean
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.fortunes.commonsdk.network.ApiException
import com.fortunes.commonsdk.network.api.NetProvider
import com.fortunes.commonsdk.network.api.RequestHandler
import okhttp3.*
import org.json.JSONObject
import java.net.URLDecoder
import java.nio.charset.Charset


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

open class BaseNetProvider(private val context: Context) : NetProvider {
    companion object {
        const val CONNECT_TIME_OUT: Long = 30
        const val READ_TIME_OUT: Long = 30
        const val WRITE_TIME_OUT: Long = 30
    }

    override fun configInterceptors(): Array<Interceptor>? = null

    override fun configHttps(builder: OkHttpClient.Builder) {
    }

    override fun configCookie(): PersistentCookieJar =
            PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

    override fun configHandler(): RequestHandler = HeaderHandler()

    override fun configConnectTimeoutSecs(): Long =
        CONNECT_TIME_OUT

    override fun configReadTimeoutSecs(): Long =
        READ_TIME_OUT

    override fun configWriteTimeoutSecs(): Long =
        WRITE_TIME_OUT

    override fun configLogEnable(): Boolean = BuildConfig.DEBUG

    inner class HeaderHandler : RequestHandler {
        override fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request {
            return if (request.method() == "POST") {
                if (request.body() is FormBody) {
                    val mediaType: MediaType? = MediaType.parse("application/json; charset=utf-8")
                    val jsonObject = JSONObject()
                    val formBody = request.body() as FormBody
                    for (index in 0 until formBody.size()) {
                        if (formBody.encodedValue(index).isNotEmpty() && formBody.encodedName(index).isNotEmpty()) {
                            jsonObject.put(formBody.encodedName(index), formBody.encodedValue(index))
                        }
                    }
                    val requestBody: RequestBody = RequestBody.create(mediaType, URLDecoder.decode(jsonObject.toString(), "UTF-8"))
                    request.newBuilder()
                            .header("Connection","close")
                            .post(requestBody)
                            .build()
                } else {
                    request.newBuilder()
                            .header("Connection","close")
                            .post(request.body())
                            .build()
                }
            } else {
                request
            }
        }

        override fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response {
            if (response.isSuccessful) {
                //这里不能使用response.body().string()来进行读取
                val source = response.body()?.source()
                source?.request(Long.MAX_VALUE)
                val buffer = source?.buffer()
                val body = buffer?.clone()?.readString(Charset.forName("UTF-8"))
                val bean: BaseBean<Any> = Gson().fromJson(body, object : TypeToken<BaseBean<Any>>() {}.type)
//                if (bean.errorCode != HttpStatusConstants.SUCCESS){
//                    throw HttpException(bean) // 外部异常，系统维护中
//                }
                if (bean.errorCode == HttpStatusConstants.SUCCESS) {
                    return response
                } else {
                    throw HttpException(bean)
                }
            } else {
                throw ApiException("服务器内部错误!")
            }
        }
    }
}