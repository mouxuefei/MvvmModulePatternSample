package com.fortunes.commonsdk.base

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.fortunes.commonsdk.R
import com.fortunes.commonsdk.binds.bindRefreshing
import com.fortunes.commonsdk.binds.bindStatus
import com.mou.basemvvm.helper.annotation.PageStateType
import com.mou.basemvvm.helper.listener.RefreshPresenter
import com.mou.basemvvm.mvvm.BaseViewModel
import kotlinx.android.synthetic.main.public_activity_list.*

/**
 * @FileName: BaseTitleListActivity.java
 * @author: villa_mou
 * @date: 08-15:32
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
abstract class BaseTitleListActivity<VM : BaseViewModel> : BaseActivity<VM>(), RefreshPresenter {
    override fun getLayoutId() = R.layout.public_activity_list
    lateinit var mRecyclerView: RecyclerView
    override fun initView() {
        mRecyclerView = recyclerView
        mViewModel.pageState.observe(this, Observer {
            it?.let {
                bindStatus(sv, it)
            }
        })
        //点击重新加载
        sv.setOnRetryClickListener(View.OnClickListener {
            mViewModel.pageState.value = PageStateType.LOADING
            loadData(false)
        })

        mViewModel.listState.observe(this, Observer {
            it?.let { bindRefreshing(srl, it) }
        })
        srl.setOnRefreshListener {
            loadData(true)
        }
        srl.setOnLoadMoreListener {
            loadData(false)
        }
        toolBar.setTitle(getPageTitle())
        initCommonView()
        startObserve()
    }

    abstract fun getPageTitle(): String

    abstract fun initCommonView()

    open fun startObserve() {}
}