package com.core.network.provider

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.core.network.ApiException
import com.core.network.BuildConfig
import com.core.network.HttpStatusConstants
import com.core.network.api.NetProvider
import com.core.network.api.RequestHandler
import com.core.network.bean.BaseBean
import com.core.network.bean.HttpException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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
            return if (request.method == "POST") {
                when (val body = request.body) {
                    is FormBody -> {
                        val jsonObject = JSONObject()
                        for (i in 0 until body.size) {
                            val name = body.name(i)
                            val value = body.value(i)
                            if (name.isNotEmpty() && value.isNotEmpty()) {
                                jsonObject.put(name, value)
                            }
                        }
                        val jsonBody = jsonObject.toString()
                            .toRequestBody("application/json; charset=utf-8".toMediaType())

                        request.newBuilder()
                            //.header("Connection", "close") // 可选
                            .post(jsonBody)
                            .build()
                    }

                    is MultipartBody -> {
                        // MultipartBody 不做处理，保持原样
                        request
                    }

                    else -> {
                        // 如果已经是 JSON 或其他 RequestBody，保持原样
                        request
                    }
                }
            } else {
                request
            }

//            return if (request.method == "POST") {
//                if (request.body is FormBody) {
//                    val mediaType: MediaType? =
//                        "application/json; charset=utf-8".toMediaTypeOrNull()
//                    val jsonObject = JSONObject()
//                    val formBody = request.body as FormBody
//                    for (index in 0 until formBody.size) {
//                        if (formBody.encodedValue(index).isNotEmpty() && formBody.encodedName(index)
//                                .isNotEmpty()
//                        ) {
//                            jsonObject.put(
//                                formBody.encodedName(index),
//                                formBody.encodedValue(index)
//                            )
//                        }
//                    }
//                    val requestBody: RequestBody = RequestBody.create(
//                        mediaType,
//                        URLDecoder.decode(jsonObject.toString(), "UTF-8")
//                    )
//                    request.newBuilder()
//                        .header("Connection", "close")
//                        .post(requestBody)
//                        .build()
//                } else {
//                    request.newBuilder()
//                        .header("Connection", "close")
//                        .post(request.body!!)
//                        .build()
//                }
//            } else {
//                request
//            }
        }

        override fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response {
            if (response.isSuccessful) {
                //这里不能使用response.body().string()来进行读取
                val source = response.body?.source()
                source?.request(Long.MAX_VALUE)
                val buffer = source?.buffer()
                val body = buffer?.clone()?.readString(Charset.forName("UTF-8"))
                val bean: BaseBean<Any> =
                    Gson().fromJson(body, object : TypeToken<BaseBean<Any>>() {}.type)
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