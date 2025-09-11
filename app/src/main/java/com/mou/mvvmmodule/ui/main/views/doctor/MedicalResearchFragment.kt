package com.mou.mvvmmodule.ui.main.views.doctor

import com.core.basemvvm.base.BaseFragment
import com.mou.mvvmmodule.databinding.FragmentMedicalResearchBinding
import com.mou.mvvmmodule.ui.main.viewmodel.MedicalResearchViewModel

/**
 * @FileName: MedicalResearchFragment.java
 * @author: villa_mou
 * @date: 09-11:07
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class MedicalResearchFragment: BaseFragment<MedicalResearchViewModel>() {
    override val binding: FragmentMedicalResearchBinding by lazy {
        FragmentMedicalResearchBinding.inflate(layoutInflater)
    }


    override fun providerVMClass(): Class<MedicalResearchViewModel> = MedicalResearchViewModel::class.java

    override fun initView() {

    }

    override fun initData() {

    }
}