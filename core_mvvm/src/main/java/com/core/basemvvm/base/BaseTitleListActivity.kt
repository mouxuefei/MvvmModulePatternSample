//package com.core.basemvvm.base
//
//import android.view.View
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.RecyclerView
//import com.core.commonsdk.binds.bindRefreshing
//import com.core.commonsdk.binds.bindStatus
//import com.core.commonsdk.databinding.PublicActivityListBinding
//import com.core.basemvvm.helper.annotation.PageStateType
//import com.core.basemvvm.helper.listener.RefreshPresenter
//import com.core.basemvvm.mvvm.BaseViewModel
//
///**
// * @FileName: BaseTitleListActivity.java
// * @author: villa_mou
// * @date: 08-15:32
// * @version V1.0 <描述当前版本功能>
// * @desc
// */
//abstract class BaseTitleListActivity<VM : BaseViewModel> : BaseActivity<VM>(), RefreshPresenter {
//
//    override val binding: PublicActivityListBinding by lazy {
//        PublicActivityListBinding.inflate(layoutInflater)
//    }
//
//    lateinit var mRecyclerView: RecyclerView
//    override fun initView() {
//
//        mRecyclerView = binding.recyclerView
//        mViewModel.pageState.observe(this, Observer {
//            it?.let {
//                bindStatus(binding.sv, it)
//            }
//        })
//        //点击重新加载
//        binding.sv.setOnRetryClickListener(View.OnClickListener {
//            mViewModel.pageState.value = PageStateType.LOADING
//           refresh()
//        })
//
//        mViewModel.listState.observe(this, Observer {
//            it?.let { bindRefreshing(binding.srl, it) }
//        })
//        binding.srl.setOnRefreshListener {
//            refresh()
//        }
//        binding.srl.setOnLoadMoreListener {
//            loadMore()
//        }
//        binding.toolBar.setTitle(getPageTitle())
//        initCommonView()
//        startObserve()
//    }
//
//    abstract fun getPageTitle(): String
//
//    abstract fun initCommonView()
//
//    open fun startObserve() {}
//}