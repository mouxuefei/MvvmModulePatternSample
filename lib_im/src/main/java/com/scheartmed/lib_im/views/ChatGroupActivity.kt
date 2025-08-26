package com.scheartmed.lib_im.views

import androidx.viewbinding.ViewBinding
import com.fortunes.commonsdk.base.BaseActivity
import com.scheartmed.lib_im.databinding.ActivityChatGroupBinding
import com.scheartmed.lib_im.viewmodels.ChatGroupViewModel

/**
 * @FileName: ChatGroupActivity.java
 * @author: villa_mou
 * @date: 08-13:45
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ChatGroupActivity : BaseActivity<ChatGroupViewModel>() {
    override val binding: ActivityChatGroupBinding by lazy {
        ActivityChatGroupBinding.inflate(layoutInflater)
    }


    override fun providerVMClass(): Class<ChatGroupViewModel>? {
        return ChatGroupViewModel::class.java
    }

    override fun initView() {
    }

    override fun initData() {
    }
}