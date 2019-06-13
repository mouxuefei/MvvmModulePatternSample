package com.fortunes.commonsdk.network

import com.fortunes.commonsdk.network.bean.BaseBean
import com.mou.basemvvm.helper.network.ApiException

/**
 * @auther:MR-Cheng
 * @date: 2018/12/7
 * @description: 网络错误 状态
 * @parameter:
 */

class HttpException(val baseBean: BaseBean<Any>) : ApiException(baseBean.errorMsg)