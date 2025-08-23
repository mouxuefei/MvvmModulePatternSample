package com.mou.mvvmmodule.ui.main.views

import androidx.viewbinding.ViewBinding
import com.fortunes.commonsdk.base.BaseFragment
import com.fortunes.commonsdk.network.dealResult
import com.mou.basemvvm.helper.extens.bindDialogOrLifeCycle
import com.mou.basemvvm.helper.extens.toast
import com.mou.basemvvm.mvvm.BaseVMModel
import com.mou.mvvmmodule.databinding.FragmentHomeBinding
import com.mou.mvvmmodule.ui.main.viewmodel.HomeFragmentViewModel

/**
 * @FileName: HomeFragment.java
 * @author: villa_mou
 * @date: 08-11:46
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class HomeFragment : BaseFragment<HomeFragmentViewModel>() {
    override val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }


    override fun providerVMClass(): Class<HomeFragmentViewModel>? =
        HomeFragmentViewModel::class.java

    override fun initView() {
        binding.btn.setOnClickListener {
            mViewModel.run {
                this.getArticle().bindDialogOrLifeCycle(this@HomeFragment)
                    .dealResult(mContext)
            }

        }


        binding.btnLogin.setOnClickListener {

        }

        binding.btnMine.setOnClickListener {}
    }

    override fun initData() {


        mViewModel.chapterName.observe(this) {
            it?.let { binding.name.text = it }
        }
        mViewModel.link.observe(this) {
            it?.let { binding.desc.text = it }
        }
    }
}