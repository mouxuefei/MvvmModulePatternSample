package com.mou.basemvvm.mvvm

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mou.basemvvm.helper.annotation.PageStateType
import com.mou.basemvvm.helper.annotation.RefreshType
import com.orhanobut.logger.Logger

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
    //页面状态
    @SuppressLint("SupportAnnotationUsage")
    @PageStateType
    val pageState = MutableLiveData<Int>()
    //刷新/加载更多状态
    @SuppressLint("SupportAnnotationUsage")
    @RefreshType
    val listState = MutableLiveData<Int>()

    init {
        pageState.value=PageStateType.NORMAL
        listState.value=RefreshType.NORMAL
    }

    override fun onCleared() {
        super.onCleared()
        Logger.i("${javaClass.simpleName}:onCleared()")
    }
}