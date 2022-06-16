package com.fortunes.commonsdk.network

import com.fortunes.commonsdk.network.api.NetProvider
import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

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
 * Created by mou on 2018/8/20.


 * 网络请求封装类
 * 使用Retrofit+OkHttp
 */

object NetMgr {
    //存放网络设置的map
    private val providerMap = HashMap<String, NetProvider>()
    //存放retrofit的map
    private val retrofitMap = HashMap<String, Retrofit>()
    //存放okhttp的map
    private val clientMap = HashMap<String, OkHttpClient>()

    private const val connectTimeoutMills = 10 * 1000L
    private const val readTimeoutMills = 10 * 1000L

    var commonProvider: NetProvider? = null
        private set

    /**
     * 获取retrofit实例
     */
    @JvmOverloads
    fun getRetrofit(baseUrl: String, netProvider: NetProvider? = null): Retrofit {
        var provider = netProvider
        if (empty(baseUrl)) {
            throw IllegalStateException("baseUrl can not be null")
        }
        if (retrofitMap[baseUrl] != null) {
            return retrofitMap[baseUrl]!!
        }

        if (provider == null) {
            provider = providerMap[baseUrl]
            if (provider == null) {
                provider = commonProvider
            }
        }

        checkProvider(provider)

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory())
                .create()

        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(baseUrl, provider!!))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit = builder.build()
        retrofitMap[baseUrl] = retrofit
        providerMap[baseUrl] = provider
        return retrofit
    }

    /**
     * 获取OkHttpClient实例
     */
    private fun getClient(baseUrl: String, provider: NetProvider): OkHttpClient {
        if (empty(baseUrl)) {
            throw IllegalStateException("baseUrl can not be null")
        }

        if (clientMap[baseUrl] != null) {
            return clientMap[baseUrl]!!
        }

        checkProvider(provider)

        val builder = OkHttpClient.Builder()

        builder.connectTimeout(if (provider.configConnectTimeoutSecs() != 0L)
            provider.configConnectTimeoutSecs()
        else
            connectTimeoutMills, TimeUnit.SECONDS)

        builder.readTimeout(if (provider.configReadTimeoutSecs() != 0L)
            provider.configReadTimeoutSecs()
        else
            readTimeoutMills, TimeUnit.SECONDS)

        builder.writeTimeout(if (provider.configWriteTimeoutSecs() != 0L)
            provider.configWriteTimeoutSecs()
        else
            readTimeoutMills, TimeUnit.SECONDS)

        val cookieJar = provider.configCookie()
        if (cookieJar != null) {
            builder.cookieJar(cookieJar)
        }

        provider.configHttps(builder)

        val handler = provider.configHandler()
        builder.addInterceptor(NetInterceptor(handler))

        val interceptors = provider.configInterceptors()
        if (!empty(interceptors)) {
            interceptors!!.forEach {
                builder.addInterceptor(it)
            }
        }

        if (provider.configLogEnable()) {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Logger.d(it)
            })
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        //https验证
        builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
        builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier())
        val client = builder.build()
        clientMap[baseUrl] = client
        providerMap[baseUrl] = provider
        return client
    }


    /**
     * 判断baseUrl是否为空
     */
    private fun empty(baseUrl: String?): Boolean {
        return baseUrl == null || baseUrl.isEmpty()
    }

    /**
     * 判断拦截器是否为空
     */
    private fun empty(interceptors: Array<Interceptor>?): Boolean {
        return interceptors == null || interceptors.isEmpty()
    }

    /**
     * 判断是否有拦截器
     */
    private fun checkProvider(provider: NetProvider?) {
        if (provider == null) {
            throw IllegalStateException("must register provider first")
        }
    }

    /**
     * 获取retrofit实例的map集合
     */
    fun getRetrofitMap(): Map<String, Retrofit> {
        return retrofitMap
    }

    /**
     * 获取OkHttpClient实例的map集合
     */
    fun getClientMap(): Map<String, OkHttpClient> {
        return clientMap
    }

    /**
     * 获取具体网络请求Service的接口实例
     */
    operator fun <S> get(baseUrl: String, service: Class<S>): S {
        return getRetrofit(baseUrl).create(service)
    }

    /**
     * 注册网络请求拦截器
     */
    fun registerProvider(provider: NetProvider) {
        commonProvider = provider
    }

    /**
     * 注册网络请求拦截器
     */
    fun registerProvider(baseUrl: String, provider: NetProvider) {
        providerMap[baseUrl] = provider
    }

    /**
     * 清除map集合
     */
    fun clearCache() {
        retrofitMap.clear()
        clientMap.clear()
    }
}