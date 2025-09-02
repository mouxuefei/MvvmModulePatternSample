package com.scheartmed.im.widget

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView

class WebViewPool private constructor(context: Context) {
    private val applicationContext = context.applicationContext
    private var webView: WebView? = null

    companion object {
        @Volatile private var instance: WebViewPool? = null
        fun getInstance(context: Context): WebViewPool {
            return instance ?: synchronized(this) {
                instance ?: WebViewPool(context).also { instance = it }
            }
        }
    }

    fun preCreateWebView() {
        if (webView == null) {
            webView = WebView(applicationContext).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.setSupportZoom(false)
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.allowFileAccess = true
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    fun getWebView(): WebView {
        if (webView == null) preCreateWebView()
        return webView!!
    }

    fun releaseWebView(container: ViewGroup) {
        webView?.let {
            container.removeView(it)
            it.stopLoading()
            it.clearHistory()
            it.clearCache(true)
            it.removeAllViews()
            it.destroy()
        }
        webView = null
    }
}