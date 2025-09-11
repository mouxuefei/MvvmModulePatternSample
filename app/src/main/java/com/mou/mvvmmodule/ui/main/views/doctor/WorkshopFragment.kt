package com.mou.mvvmmodule.ui.main.views.doctor

import com.core.basemvvm.base.BaseFragment
import com.mou.mvvmmodule.databinding.FragmentWorkShopBinding
import com.mou.mvvmmodule.ui.main.viewmodel.WorkShopViewModel

/**
 * @FileName: WorkshopFragment.java
 * @author: villa_mou
 * @date: 09-11:08
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class WorkshopFragment : BaseFragment<WorkShopViewModel>() {
    override val binding: FragmentWorkShopBinding by lazy {
        FragmentWorkShopBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<WorkShopViewModel> = WorkShopViewModel::class.java


    override fun initView() {
      
    }

    override fun initData() {
      
    }
}