package com.mou.mvvmmodule.ui.main.views.patient

import com.core.basemvvm.mvvm.EmptyViewModel
import com.core.basemvvm.base.BaseFragment
import com.mou.mvvmmodule.databinding.FragmentConversationListBinding

/**
 * @FileName: ConversationListfragment.java
 * @author: villa_mou
 * @date: 09-15:21
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ConversationListFragment : BaseFragment<EmptyViewModel>() {
    override val binding: FragmentConversationListBinding by lazy {
        FragmentConversationListBinding.inflate(layoutInflater)
    }


    override fun providerVMClass(): Class<EmptyViewModel> = EmptyViewModel::class.java

    override fun initView() {

    }

    override fun initData() {

    }
}