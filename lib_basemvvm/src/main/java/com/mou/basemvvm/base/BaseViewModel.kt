package com.mou.basemvvm.base

import android.arch.lifecycle.ViewModel
import com.mou.basemvvm.helper.annotation.PageStateType
import com.mou.basemvvm.helper.annotation.RefreshType
import com.mou.basemvvm.helper.extens.ObservableItemField
import timber.log.Timber

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
    @PageStateType
    val pageState = ObservableItemField<Int>()
    //刷新/加载更多状态
    @RefreshType
    val listState = ObservableItemField<Int>()

    init {
        pageState.set(PageStateType.NORMAL)
        listState.set(RefreshType.NORMAL)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("${javaClass.simpleName}:onCleared()")
    }
}