package com.fortunes.commonsdk.base

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fortunes.commonsdk.R
import com.fortunes.commonsdk.binds.bindRefreshing
import com.fortunes.commonsdk.binds.bindStatus
import com.fortunes.commonsdk.databinding.PublicActivityListBinding
import com.fortunes.commonsdk.view.statusview.MultipleStatusView
import com.fortunes.commonsdk.view.toolbar.MyToolBarLayout
import com.mou.basemvvm.helper.annotation.PageStateType
import com.mou.basemvvm.helper.listener.RefreshPresenter
import com.mou.basemvvm.mvvm.BaseViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @FileName: BaseTitleListActivity.java
 * @author: villa_mou
 * @date: 08-15:32
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
abstract class BaseTitleListActivity<VM : BaseViewModel> : BaseActivity<VM>(), RefreshPresenter {

    override val binding: PublicActivityListBinding by lazy {
        PublicActivityListBinding.inflate(layoutInflater)
    }

    lateinit var mRecyclerView: RecyclerView
    override fun initView() {

        mRecyclerView = binding.recyclerView
        mViewModel.pageState.observe(this, Observer {
            it?.let {
                bindStatus(binding.sv, it)
            }
        })
        //点击重新加载
        binding.sv.setOnRetryClickListener(View.OnClickListener {
            mViewModel.pageState.value = PageStateType.LOADING
            loadData(false)
        })

        mViewModel.listState.observe(this, Observer {
            it?.let { bindRefreshing(binding.srl, it) }
        })
        binding.srl.setOnRefreshListener {
            loadData(true)
        }
        binding.srl.setOnLoadMoreListener {
            loadData(false)
        }
        binding.toolBar.setTitle(getPageTitle())
        initCommonView()
        startObserve()
    }

    abstract fun getPageTitle(): String

    abstract fun initCommonView()

    open fun startObserve() {}
}