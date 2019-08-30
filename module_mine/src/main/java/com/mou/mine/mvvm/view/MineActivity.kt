package com.mou.mine.mvvm.view

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.fortunes.commonsdk.base.BaseTitleListActivity
import com.fortunes.commonsdk.core.RouterConstants
import com.fortunes.commonsdk.network.dealResult
import com.guoyang.recyclerviewbindingadapter.ItemClickPresenter
import com.guoyang.recyclerviewbindingadapter.adapter.SingleTypeAdapter
import com.mou.basemvvm.helper.extens.bindStatusOrLifeCycle
import com.mou.basemvvm.helper.extens.toast
import com.mou.mine.R
import com.mou.mine.mvvm.viewmodel.MineItemViewModel
import com.mou.mine.mvvm.viewmodel.MineViewModel

/**
 * @FileName: LoginActivity.java
 * @author: villa_mou
 * @date: 06-16:18
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
@Route(path = RouterConstants.MINE_ACTIVITY)
class MineActivity : BaseTitleListActivity<MineViewModel>(), ItemClickPresenter<MineItemViewModel> {
    override fun getPageTitle()="个人中心"
    override fun providerVMClass() = MineViewModel::class.java
    override fun loadData(isRefresh: Boolean) = loadVMData(isRefresh)
    override fun initData() = loadVMData(true)
    private val mAdapter by lazy {
        SingleTypeAdapter(this, R.layout.mine_my_item_order, mViewModel.observableList)
                .apply { this.itemPresenter = this@MineActivity }
    }

    override fun initCommonView() {
        with(mRecyclerView) {
            this.adapter = mAdapter
        }
    }

    override fun onItemClick(v: View, position: Int, item: MineItemViewModel) {
        toast("position=" + position)
    }

    private fun loadVMData(isRefresh: Boolean) =
            mViewModel.getProjectList(isRefresh, 294)
                    .bindStatusOrLifeCycle(isRefresh, viewModel = mViewModel, owner = this@MineActivity)
                    .dealResult(this@MineActivity)
}