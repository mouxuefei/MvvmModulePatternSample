package com.mou.mvvmmodule.ui.main.views.patient

import androidx.viewbinding.ViewBinding
import com.core.commonsdk.base.BaseFragment
import com.mou.mvvmmodule.databinding.FragmentFitBinding
import com.mou.mvvmmodule.databinding.FragmentMessageBinding
import com.mou.mvvmmodule.ui.main.viewmodel.FitViewModel
import com.mou.mvvmmodule.ui.main.viewmodel.MessageViewModel

/**
 * @FileName: PatientMessageFragement.java
 * @author: villa_mou
 * @date: 09-10:59
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class MessageFragment : BaseFragment<MessageViewModel>() {
    override val binding: FragmentMessageBinding by lazy {
        FragmentMessageBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<MessageViewModel> = MessageViewModel::class.java


    override fun initView() {
      
    }

    override fun initData() {
      
    }
}