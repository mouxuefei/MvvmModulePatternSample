//package com.mou.mvvmmodule.ui.main.views.patient
//
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.core.basemvvm.mvvm.EmptyViewModel
//import com.core.basemvvm.base.BaseActivity
//import com.core.commonsdk.base.BaseTitleListActivity
//import com.mou.mvvmmodule.ui.main.viewmodel.HomeFragmentViewModel
//import com.mou.mvvmmodule.ui.main.views.adapter.UserAdapter
//
///**
// * @FileName: TestListActivity.java
// * @author: villa_mou
// * @date: 09-19:09
// * @version V1.0 <描述当前版本功能>
// * @desc
// */
//class TestListActivity : BaseTitleListActivity<HomeFragmentViewModel>() {
//    override fun getPageTitle(): String = "列表"
//    private val adapter by lazy { UserAdapter() }
//    override fun initCommonView() {
//        mRecyclerView.layoutManager = LinearLayoutManager(this)
//        mRecyclerView.adapter = adapter
//
//    }
//
//    override fun providerVMClass(): Class<HomeFragmentViewModel> = HomeFragmentViewModel::class.java
//    override fun initData() {
//        mViewModel.users.observe(this) { list ->
//            adapter.setList(list)  // BRVAH 提供的方法
//        }
//    }
//
//    override fun loadData(isRefresh: Boolean) {
//        if (isRefresh) {
//            mViewModel.loadUsers()
//        } else {
//
//        }
//    }
//}