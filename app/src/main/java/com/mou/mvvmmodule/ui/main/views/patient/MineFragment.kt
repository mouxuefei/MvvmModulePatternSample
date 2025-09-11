package com.mou.mvvmmodule.ui.main.views.patient

import com.core.basemvvm.base.BaseFragment
import com.mou.mvvmmodule.databinding.FragmentMineBinding
import com.mou.mvvmmodule.ui.main.viewmodel.MineViewModel

/**
 * @FileName: mineFragment.java
 * @author: villa_mou
 * @date: 09-10:57
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class MineFragment : BaseFragment<MineViewModel>() {
    override val binding: FragmentMineBinding by lazy {
        FragmentMineBinding.inflate(layoutInflater)
    }
    override fun providerVMClass(): Class<MineViewModel> = MineViewModel::class.java


    override fun initView() {
      
    }

    override fun initData() {
      
    }
}