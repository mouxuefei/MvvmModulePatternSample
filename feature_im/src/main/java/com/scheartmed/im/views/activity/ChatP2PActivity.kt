package com.scheartmed.im.views.activity

import com.core.basemvvm.base.BaseActivity
import com.scheartmed.im.R
import com.scheartmed.im.databinding.ActivityChatP2pBinding
import com.scheartmed.im.viewmodels.ChatViewModel
import com.scheartmed.im.views.fragment.ChatP2PFragment


/**
 * @FileName: ChatP2PActivity.java
 * @author: villa_mou
 * @date: 08-13:45
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ChatP2PActivity : BaseActivity<ChatViewModel>() {
    override val binding: ActivityChatP2pBinding by lazy {
        ActivityChatP2pBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<ChatViewModel> = ChatViewModel::class.java



    override fun initView() {
        initTitleBar()

    }

    override fun initData() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragContainer, ChatP2PFragment.newInstance("test002"))
            .commit()
    }

    private fun initTitleBar() {
        val chatId = intent.extras?.getString("account")
        binding.titleBar.setTitle(chatId ?: "")
    }

}