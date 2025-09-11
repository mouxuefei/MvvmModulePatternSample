package com.scheartmed.im.views.activity

import com.core.basemvvm.base.BaseActivity
import com.scheartmed.im.R
import com.scheartmed.im.databinding.ActivityChatGroupBinding
import com.scheartmed.im.viewmodels.ChatGroupViewModel
import com.scheartmed.im.views.fragment.ChatFragment

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
    override fun providerVMClass(): Class<ChatGroupViewModel> = ChatGroupViewModel::class.java

    override fun initView() {
        initTitleBar()

    }

    override fun initData() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragContainer, ChatFragment.newInstance("48256897322"))
            .commit()
    }

    private fun initTitleBar() {
        val chatId = intent.extras?.getString("account")
        binding.titleBar.setTitle(chatId ?: "")
    }

}