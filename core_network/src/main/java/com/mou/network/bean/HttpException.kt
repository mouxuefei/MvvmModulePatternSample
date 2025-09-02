package com.mou.network.bean

import com.mou.network.ApiException


/**
 * @auther:MR-Cheng
 * @date: 2018/12/7
 * @description: 网络错误 状态
 * @parameter:
 */

class HttpException(val baseBean: BaseBean<Any>) : ApiException(baseBean.errorMsg)