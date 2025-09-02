package com.mou.basemvvm.helper.listener

/***
 *
 * Created by mou on 2018/8/20.
 * 下拉刷新/上拉加载的接口类
 */

interface RefreshPresenter {
    /**
     * 加载数据
     * isRefresh:true 下拉刷新,false 上拉加载更多
     */
    fun loadData(isRefresh: Boolean)
}