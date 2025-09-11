package com.core.basemvvm.base

import com.core.basemvvm.mvvm.BaseListViewModel
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.core.basemvvm.R
import com.core.basemvvm.mvvm.ListStatus
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

abstract class BaseListActivity<T, VM : BaseListViewModel<T>> : BaseActivity<VM>() {

    lateinit var recyclerView: RecyclerView
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var container: FrameLayout
    protected abstract fun provideAdapter(): BaseQuickAdapter<T, out BaseViewHolder>

    private val adapter by lazy { provideAdapter() }
    private lateinit var emptyView: View
    private lateinit var errorView: View
    private lateinit var noNetworkErrorView: View

    override fun initView() {
        recyclerView = binding.root.findViewById(R.id.recyclerView)
        refreshLayout = binding.root.findViewById(R.id.refreshLayout)
        container = binding.root.findViewById(R.id.container)
        setupRecyclerView()
        setupRefreshLayout()
        setupStateView()
        observeData()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener(OnRefreshListener {
            mViewModel.refresh()
        })
        refreshLayout.setOnLoadMoreListener(OnLoadMoreListener {
            mViewModel.loadMore()
        })
    }

    private fun setupStateView() {
        emptyView = layoutInflater.inflate(R.layout.layout_status_empty_view, container, false)
        errorView = layoutInflater.inflate(R.layout.layout_status_error_view, container, false)
        noNetworkErrorView =
            layoutInflater.inflate(R.layout.layout_status_no_network_view, container, false)

        errorView.setOnClickListener {
            mViewModel.refresh()
        }

        //TODO;
        noNetworkErrorView.setOnClickListener {
            mViewModel.refresh()
        }
    }

    private fun observeData() {
        mViewModel.items.observe(this, Observer { list ->
            adapter.setList(list)
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
        })

        mViewModel.status.observe(this, Observer { status ->
            when (status) {
                ListStatus.LOADING -> {
                    // 可以加 loading 动画
                }

                ListStatus.SUCCESS -> {
                    adapter.isUseEmpty = false // 清除空/错误界面
                }

                ListStatus.EMPTY -> {
                    adapter.setEmptyView(emptyView)
                    refreshLayout.finishRefresh()
                }

                ListStatus.ERROR -> {
                    adapter.setEmptyView(errorView)
                    refreshLayout.finishRefresh(false)
                }

                ListStatus.NO_NETWORK_ERROR -> {
                    adapter.setEmptyView(noNetworkErrorView)
                    refreshLayout.finishRefresh(false)
                }

                else -> {

                }
            }
        })
    }
}