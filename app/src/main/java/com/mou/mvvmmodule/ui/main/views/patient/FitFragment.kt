package com.mou.mvvmmodule.ui.main.views.patient

import androidx.viewbinding.ViewBinding
import com.core.commonsdk.base.BaseFragment
import com.mou.mvvmmodule.databinding.FragmentFitBinding
import com.mou.mvvmmodule.databinding.FragmentWorkShopBinding
import com.mou.mvvmmodule.ui.main.viewmodel.FitViewModel
import com.mou.mvvmmodule.ui.main.viewmodel.WorkShopViewModel

/**
 * @FileName: PatientFitFragment.java
 * @author: villa_mou
 * @date: 09-10:59
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class FitFragment : BaseFragment<FitViewModel>() {
    override val binding: FragmentFitBinding by lazy {
        FragmentFitBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<FitViewModel> = FitViewModel::class.java


    override fun initView() {
      
    }

    override fun initData() {
      
    }
}