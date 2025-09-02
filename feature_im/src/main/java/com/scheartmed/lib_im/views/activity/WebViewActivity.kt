package com.scheartmed.lib_im.views.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.fortunes.commonsdk.base.BaseActivity
import com.mou.basemvvm.mvvm.BaseViewModel
import com.mou.basemvvm.mvvm.EmptyViewModel
import com.scheartmed.lib_im.databinding.ActivityWebviewBinding
import com.scheartmed.lib_im.widget.WebViewPool

/**
 * @FileName: WebViewActivity.java
 * @author: villa_mou
 * @date: 09-14:42
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class WebViewActivity : BaseActivity<EmptyViewModel>() {

    override val binding: ActivityWebviewBinding by lazy {
        ActivityWebviewBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<EmptyViewModel>? =EmptyViewModel::class.java

    private var webView: WebView? = null

    override fun initView() {
        webView = WebViewPool.getInstance(this).getWebView()
        webView?.clearHistory()
        binding.webViewContainer.addView(webView)
        initWebView()
        val url = intent.getStringExtra("url") ?: ""
        webView?.loadUrl(url)
    }

    override fun initData() {

    }

    private fun initWebView() {
        webView?.apply {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return false
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    // TODO 可以加 loading 进度条
                }
            }

            // 添加 JS 接口：H5 可以调用 Android.showToast("xxx")
            addJavascriptInterface(JSBridge(this@WebViewActivity), "Android")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.let {
            (it.parent as? ViewGroup)?.removeView(it)
        }
    }



    // 原生与 H5互调桥梁
    class JSBridge(private val context: Context) {
        @JavascriptInterface
        fun showToast(msg: String) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 原生调用 H5 方法
    fun callJsFunction(jsCode: String) {
        webView?.evaluateJavascript(jsCode, null)
    }
}