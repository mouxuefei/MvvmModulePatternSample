package com.fortunes.commonsdk.binds

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.fortunes.commonsdk.view.statusview.MultipleStatusView
import com.mou.basemvvm.helper.annotation.PageStateType
import com.mou.basemvvm.helper.annotation.RefreshType
import com.mou.basemvvm.helper.listener.RefreshPresenter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


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
 * Created by mou on 2018/8/22.


 */

/**
 * imageView加载网络图片
 * app:url="@{url}"
 */
@BindingAdapter(value = ["imageUrl", "error"], requireAll = false)
fun bindUrl(imageView: ImageView, url: String?, error: Drawable?) {
    com.fortunes.commonsdk.utils.ImageUtils.load(imageView.context, url, imageView, error)
}

/**
 * MultipleStatusView的状态切换
 * app:status="@{PageStateType.LOADING}"
 */
@BindingAdapter(value = ["status"])
fun bindStatus(multipleStatusView: MultipleStatusView, @PageStateType stateType: Int?) {
    when (stateType) {
        //切换加载中的状态
        PageStateType.LOADING -> multipleStatusView.showLoading()
        //切换空布局
        PageStateType.EMPTY -> multipleStatusView.showEmpty()
        //切换错误布局
        PageStateType.ERROR -> multipleStatusView.showError()
        //切换正常布局
        PageStateType.NORMAL -> multipleStatusView.showContent()
        PageStateType.CONTENT -> multipleStatusView.showContent()
        //切换无网络布局
        PageStateType.NOWORK -> multipleStatusView.showNoNetwork()
    }
}

@BindingAdapter(value = ["onError"])
fun bindOnErrorListener(multipleStatusView: MultipleStatusView, listListener: RefreshPresenter?) {
    multipleStatusView.setOnRetryClickListener(View.OnClickListener {
        listListener?.loadData(true)
    })
}

/**
 * SmartRefreshLayout的下拉刷新上拉加载回调监听
 * app:onRefresh="@{listListener}"
 */
@BindingAdapter(value = ["onRefresh"])
fun bindOnRefresh(smartRefreshLayout: SmartRefreshLayout, listListener: RefreshPresenter?) {
    smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
        override fun onLoadMore(refreshLayout: RefreshLayout) {
            listListener?.loadData(false)
        }

        override fun onRefresh(refreshLayout: RefreshLayout) {
            smartRefreshLayout.setNoMoreData(false)
            listListener?.loadData(true)
        }

    })
}

/**
 * SmartRefreshLayout的刷新回调状态改变
 * app:refreshing="@{RefreshType.REFRESH}"
 */
@BindingAdapter(value = ["refreshing"])
fun bindRefreshing(smartRefreshLayout: SmartRefreshLayout, @RefreshType refreshType: Int?) {
    when (refreshType) {
        RefreshType.REFRESH -> smartRefreshLayout.finishRefresh()
        RefreshType.LOADMORE -> smartRefreshLayout.finishLoadMore()
        RefreshType.NOTMORE -> smartRefreshLayout.finishLoadMoreWithNoMoreData()
        RefreshType.REFRESHFAIL -> smartRefreshLayout.finishRefresh(false)
        RefreshType.LOADMOREFAIL -> smartRefreshLayout.finishLoadMore(false)
        RefreshType.NORMAL -> {
        }
    }
}
