package com.core.basemvvm.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.basemvvm.helper.annotation.PageStateType
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
 * ViewModel的父类
 */

abstract class BaseViewModel : ViewModel() {
//    //页面状态
    @PageStateType
    val pageState = MutableLiveData<Int>()
//
//    //刷新/加载更多状态
//    @RefreshType
//    val listState = MutableLiveData<Int>()


    /**
     * 全局弹窗
     */
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingDialogState: LiveData<LoadingState> get() = _loadingState

    private var requestCount = 0

    init {
        pageState.value = PageStateType.NORMAL
//        listState.value = RefreshType.NORMAL
    }


    /**
     * 带 Loading 的协程封装
     */
    protected fun <T> launchWithLoading(
        block: suspend CoroutineScope.() -> T,
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        message: String? = "加载中...", // 默认文案
    ) {
        viewModelScope.launch {
            try {
                showLoading(message)
                val result = block()
                onSuccess(result)
            } catch (e: Exception) {
                hideLoading()
                onError(e)
            } finally {
                hideLoading()
            }
        }
    }

    /**
     * 无 Loading 的协程封装
     */
    protected fun <T> launchNormal(
        block: suspend CoroutineScope.() -> T,
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        viewModelScope.launch {
            try {
                val result = block()
                onSuccess(result)
            } catch (e: Exception) {
                onError(e)
            } finally {
            }
        }
    }

    private fun showLoading(message: String?) {
//        requestCount++
        _loadingState.value = LoadingState(true, message)
    }

    private fun hideLoading() {
//        requestCount--
//        if (requestCount <= 0) {
            _loadingState.value = LoadingState(false, null)
//            requestCount = 0
//        }
    }

    override fun onCleared() {
        super.onCleared()
        _loadingState.value = LoadingState(false, null)
        Logger.i("${javaClass.simpleName}:onCleared()")
    }
}