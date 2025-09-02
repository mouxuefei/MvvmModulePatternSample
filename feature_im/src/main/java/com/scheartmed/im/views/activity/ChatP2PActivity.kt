package com.scheartmed.im.views.activity

import com.core.commonsdk.base.BaseActivity
import com.scheartmed.im.databinding.ActivityChatP2pBinding
import com.scheartmed.im.viewmodels.ChatP2PViewModel


/**
 * @FileName: ChatP2PActivity.java
 * @author: villa_mou
 * @date: 08-13:45
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ChatP2PActivity : BaseActivity<ChatP2PViewModel>() {
    override val binding: ActivityChatP2pBinding by lazy {
        ActivityChatP2pBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<ChatP2PViewModel> = ChatP2PViewModel::class.java



    override fun initView() {
        initTitleBar()

    }

    override fun initData() {

    }

    private fun initTitleBar() {
        val chatId = intent.extras?.getString("account")
        binding.titleBar.setTitle(chatId ?: "")
    }

}