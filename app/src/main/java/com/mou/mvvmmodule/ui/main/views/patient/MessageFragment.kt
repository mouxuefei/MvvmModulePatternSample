package com.mou.mvvmmodule.ui.main.views.patient

import androidx.fragment.app.Fragment
import com.core.basemvvm.base.BaseFragment
import com.mou.mvvmmodule.R
import com.mou.mvvmmodule.databinding.FragmentMessageBinding
import com.mou.mvvmmodule.ui.main.viewmodel.MessageViewModel
import com.scheartmed.im.views.fragment.ChatFragment


/**
 * @FileName: PatientMessageFragement.java
 * @author: villa_mou
 * @date: 09-10:59
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class MessageFragment : BaseFragment<MessageViewModel>() {
    private val TYPE_LOGIN = "login"
    private val TYPE_MAIN = "main"

    private var currentType = TYPE_LOGIN // 默认显示登录页

    override val binding: FragmentMessageBinding by lazy {
        FragmentMessageBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<MessageViewModel> = MessageViewModel::class.java


    override fun initView() {
        showFragment(currentType)
    }

    override fun initData() {

    }

    private fun showFragment(type: String) {
        val target: Fragment = if (TYPE_LOGIN == type) {
            ChatFragment()
        } else {
            ConversationListFragment()
        }
        getChildFragmentManager()
            .beginTransaction()
            .replace(R.id.childFragmentContainer, target)
            .commit()
        currentType = type
    }

    // 登录成功时调用
    fun onLoginSuccess() {
        showFragment(TYPE_MAIN)
    }
}