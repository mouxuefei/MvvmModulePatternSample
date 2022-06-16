package com.fortunes.commonsdk.network

import java.io.IOException

/***
 *
 * Created by mou on 2018/8/20.
 * 网络请求错误异常类
 */

open class ApiException(message: String) : IOException(message)