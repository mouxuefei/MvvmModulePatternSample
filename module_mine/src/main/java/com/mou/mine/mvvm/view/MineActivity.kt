package com.mou.mine.mvvm.view

import android.util.Log
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.fortunes.commonsdk.base.BaseTitleListActivity
import com.fortunes.commonsdk.core.RouterConstants
import com.fortunes.commonsdk.network.dealResult
import com.mou.basemvvm.helper.extens.bindStatusOrLifeCycle
import com.mou.mine.mvvm.adapter.MineListAdapter
import com.mou.mine.mvvm.viewmodel.MineViewModel
import kotlin.math.log

/**
 * @FileName: LoginActivity.java
 * @author: villa_mou
 * @date: 06-16:18
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
@Route(path = RouterConstants.MINE_ACTIVITY)
class MineActivity : BaseTitleListActivity<MineViewModel>() {
    override fun getPageTitle() = "个人中心"
    override fun providerVMClass() = MineViewModel::class.java
    override fun loadData(isRefresh: Boolean) = loadVMData(isRefresh)
    override fun initData() = loadVMData(true)
    private val mAdapter by lazy { MineListAdapter() }

    override fun initCommonView() {
        with(mRecyclerView) {
            adapter = mAdapter
        }
        mAdapter.run {
            setOnItemClickListener { _, _, position: Int ->
                Log.e("villa", "position==" + position)
            }
            setOnItemChildClickListener { _, _, position: Int ->

            }
            loadMoreModule.setOnLoadMoreListener {
                Log.e("villa", "setOnLoadMoreListener")
                loadVMData(false)
            }

        }
    }


    override fun startObserve() {
        mViewModel.apply {
            mineItemLiveData.observe(this@MineActivity, {
                mAdapter.setList(it)
            })
        }
    }


    private fun loadVMData(isRefresh: Boolean) =
        mViewModel.getProjectList(isRefresh, 294)
            .bindStatusOrLifeCycle(isRefresh, viewModel = mViewModel, owner = this@MineActivity)
            .dealResult(this@MineActivity)
}