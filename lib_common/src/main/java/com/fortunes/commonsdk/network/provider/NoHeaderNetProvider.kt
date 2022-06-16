package com.fortunes.commonsdk.network.provider

import android.content.Context
import com.fortunes.commonsdk.BuildConfig
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.fortunes.commonsdk.network.api.NetProvider
import com.fortunes.commonsdk.network.api.RequestHandler
import okhttp3.*

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

class NoHeaderNetProvider(private val context: Context) : NetProvider {
    companion object {
        const val CONNECT_TIME_OUT: Long = 30
        const val READ_TIME_OUT: Long = 30
        const val WRITE_TIME_OUT: Long = 30
    }

    override fun configInterceptors(): Array<Interceptor>? = null

    override fun configHttps(builder: OkHttpClient.Builder) {
    }

    override fun configCookie(): CookieJar? =
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
            return request
        }

        override fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response {
            return response
        }
    }
}