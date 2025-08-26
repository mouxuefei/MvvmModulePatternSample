package com.scheartmed.lib_im.views

import androidx.viewbinding.ViewBinding
import com.fortunes.commonsdk.base.BaseActivity
import com.scheartmed.lib_im.databinding.ActivityChatGroupBinding
import com.scheartmed.lib_im.databinding.ActivityChatP2pBinding
import com.scheartmed.lib_im.viewmodels.ChatP2PViewModel

/**
 * @FileName: ChatP2PActivity.java
 * @author: villa_mou
 * @date: 08-13:45
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ChatP2PActivity:BaseActivity<ChatP2PViewModel>() {
    override val binding: ActivityChatP2pBinding by lazy {
        ActivityChatP2pBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<ChatP2PViewModel> {
       return ChatP2PViewModel::class.java
    }

    override fun initView() {
    }

    override fun initData() {
    }
}