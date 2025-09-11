package com.core.basemvvm.helper.listener

/***
 *
 * Created by mou on 2018/8/20.
 * 下拉刷新/上拉加载的接口类
 */

interface RefreshPresenter {

    fun refresh()

    fun loadMore()
}