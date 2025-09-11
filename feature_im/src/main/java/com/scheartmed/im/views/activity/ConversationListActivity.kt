package com.scheartmed.im.views.activity

import com.core.basemvvm.base.BaseActivity
import com.scheartmed.im.databinding.ActivityConversationListBinding
import com.scheartmed.im.viewmodels.ConversationListViewModel

/**
 * @FileName: ConversationListActivity.java
 * @author: villa_mou
 * @date: 08-13:45
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ConversationListActivity : BaseActivity<ConversationListViewModel>() {
    override val binding: ActivityConversationListBinding by lazy {
        ActivityConversationListBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<ConversationListViewModel> =
        ConversationListViewModel::class.java

    override fun initView() {
    }

    override fun initData() {
    }
}