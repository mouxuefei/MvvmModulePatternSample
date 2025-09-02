package com.mou.basemvvm.mvvm

/**
 * @FileName: LoadingState.java
 * @author: villa_mou
 * @date: 08-08:26
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
data class LoadingState(
    val isLoading: Boolean,
    val message: String? = null
)