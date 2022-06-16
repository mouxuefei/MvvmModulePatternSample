package com.fortunes.commonsdk.network

import android.content.Context
import com.fortunes.commonsdk.network.bean.EmptyException
import com.fortunes.commonsdk.network.bean.NoMoreDataException
import com.google.gson.JsonParseException
import com.mou.basemvvm.helper.extens.toast
import com.mou.easymvvm.BuildConfig
import com.uber.autodispose.SingleSubscribeProxy
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @auther:MR-Cheng
 * @date: 2018/12/5
 * @description:
 * @parameter:
 */

/**
 * 关于Http请求结果的统一处理类
 */
fun <T> SingleSubscribeProxy<T>.dealResult(
    context: Context,
    subErr: ((Throwable) -> Unit)? = null,
    success: ((T) -> Unit)? = null
) {
    this.subscribe({
        success?.invoke(it)
    }, {
        //处理登录过期,或者强制升级操作
//        if (it is HttpException) {
//            when (it.baseBean.errorCode) {
//                HttpStatusConstants.RESULT_STATE_UPDATE -> {
////                    UpdateDialog(context, true, it.baseBean.sysData.version).showDialog()
//                }
//            }
//            when (it.baseBean.code) {
//                HttpStatusConstants.CODE_S0018 -> {
//                    LoginUtils.clearUserInfo()
//                    EasySharedPreferences.load(FingerAndGestureInfo::class.java).clearAllData()
//                    NavigationUtils.goLoginActivity()
//                }
//            }
//        }
        context.toastHttpFail(it)
        subErr?.invoke(it)
    })
}

fun <T> SingleSubscribeProxy<T>.dealResultNoToast(
    context: Context,
    subErr: ((Throwable) -> Unit)? = null,
    success: ((T) -> Unit)? = null
) {
    this.subscribe({
        success?.invoke(it)
    }, {
//        if (it is HttpException) {
//            when (it.baseBean.state) {
//                HttpStatusConstants.RESULT_STATE_UPDATE -> {
////                    UpdateDialog(context, true, it.baseBean.sysData.version).showDialog()
//                }
//            }
//            when (it.baseBean.errorCode) {
//                HttpStatusConstants.CODE_S0018 -> {
//                    LoginUtils.clearUserInfo()
//                    EasySharedPreferences.load(FingerAndGestureInfo::class.java).clearAllData()
//                    NavigationUtils.goLoginActivity()
//                }
//            }
//        }
        if (it is ApiException) {
            it.message?.let {
                context.toast(it)
            }
        }
        subErr?.invoke(it)
    })
}


/**
 * 网络失败失败的toast
 */
fun Context.toastHttpFail(error: Throwable?) {
    error?.let { throwable ->
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace()
        }
        if (throwable is ApiException) {
            throwable.message?.let { toast(it) }
        } else if (throwable is SocketTimeoutException) {
            toast("网络连接超时")
        } else if (throwable is UnknownHostException || throwable is ConnectException) {
            toast("网络未连接")
        } else if (throwable is JSONException || throwable is JsonParseException) {
            toast("数据解析错误")
        } else if (throwable is NoMoreDataException || throwable is EmptyException) {
            //TODO:
        } else {
            toast("网络连接失败")
        }
    }
}