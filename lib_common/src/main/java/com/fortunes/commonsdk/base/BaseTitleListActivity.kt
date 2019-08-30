package com.fortunes.commonsdk.base

import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.View
import com.fortunes.commonsdk.R
import com.fortunes.commonsdk.binds.bindRefreshing
import com.fortunes.commonsdk.binds.bindStatus
import com.mou.basemvvm.helper.listener.RefreshPresenter
import com.mou.basemvvm.mvvm.BaseViewModel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.public_activity_list.*

/**
 * @FileName: BaseTitleListActivity.java
 * @author: villa_mou
 * @date: 08-15:32
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
abstract class BaseTitleListActivity<VM : BaseViewModel>: BaseActivity<VM>() , RefreshPresenter {
    override fun getLayoutId()= R.layout.public_activity_list
    lateinit var mRecyclerView:RecyclerView
    override fun initView() {
        mRecyclerView=recyclerView
        mViewModel.pageState.observe(this, Observer {
            it?.let {
                bindStatus(sv,it)
            }
        })
        sv.setOnRetryClickListener(View.OnClickListener {
            loadData(true)
        })
        mViewModel.listState.observe(this, Observer {
            it?.let { bindRefreshing(srl,it) }
        })
        srl.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadData(false)
            }
            override fun onRefresh(refreshLayout: RefreshLayout) {
                srl.setNoMoreData(false)
                loadData(true)
            }
        })
        toolBar.setTitle(getPageTitle())
        initCommonView()
    }

    abstract fun getPageTitle(): String

    abstract fun initCommonView()
}