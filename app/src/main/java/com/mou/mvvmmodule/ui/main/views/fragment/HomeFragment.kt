package com.mou.mvvmmodule.ui.main.views.fragment

import android.os.Bundle
import com.core.commonsdk.base.BaseFragment
import com.core.commonsdk.utils.ActRouter
import com.mou.mvvmmodule.databinding.FragmentHomeBinding
import com.mou.mvvmmodule.ui.main.viewmodel.HomeFragmentViewModel
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.v2.V2NIMError
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginListener
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginService
import com.netease.nimlib.sdk.v2.auth.enums.V2NIMLoginClientChange
import com.netease.nimlib.sdk.v2.auth.enums.V2NIMLoginStatus
import com.netease.nimlib.sdk.v2.auth.model.V2NIMKickedOfflineDetail
import com.netease.nimlib.sdk.v2.auth.model.V2NIMLoginClient
import com.orhanobut.logger.Logger
import com.scheartmed.lib_im.views.activity.ChatP2PActivity
import com.scheartmed.lib_im.views.activity.ConversationListActivity


/**
 * @FileName: HomeFragment.java
 * @author: villa_mou
 * @date: 08-11:46
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class HomeFragment : BaseFragment<HomeFragmentViewModel>() {
    override val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }


    override fun providerVMClass(): Class<HomeFragmentViewModel>? =
        HomeFragmentViewModel::class.java

    override fun initView() {
        binding.btn.setOnClickListener {
            mViewModel.run {
                this.loadUser()
            }

        }


        binding.btnLogin.setOnClickListener {
            ActRouter.startActivity(mContext, ConversationListActivity::class.java)
        }
        binding.btnMine.setOnClickListener {
            NIMClient.getService(V2NIMLoginService::class.java).login("test001", "123456", null,
                {
                    // TODO
                    Logger.e("success==")
                    val bundle = Bundle()
                    bundle.putString("account", "test002")
                    ActRouter.startActivity(mContext, ChatP2PActivity::class.java, bundle)
                }
            ) { error ->
                val code = error.code
                val desc = error.desc
                // TODO
                Logger.e("error==" + desc)
            }

        }

        binding.btnMine2.setOnClickListener {
            NIMClient.getService(V2NIMLoginService::class.java).login("test002", "123456", null,
                {
                    // TODO
                    Logger.e("success==")
                    val bundle = Bundle()
                    bundle.putString("account", "test001")
                    ActRouter.startActivity(mContext, ChatP2PActivity::class.java, bundle)
                }
            ) { error ->
                val code = error.code
                val desc = error.desc
                // TODO
                Logger.e("error==" + desc)
            }

        }

        NIMClient.getService(V2NIMLoginService::class.java)
            .addLoginListener(listener)

    }

    private val listener = object : V2NIMLoginListener {
        override fun onLoginStatus(status: V2NIMLoginStatus) {

            // Handle login status
            Logger.e("onLoginStatus==" + status.name)
            Logger.e("onLoginStatus value==" + status.value)
        }

        override fun onLoginFailed(error: V2NIMError) {
            // Handle login error
            Logger.e(error.desc)
        }

        override fun onKickedOffline(detail: V2NIMKickedOfflineDetail) {
            // Handle kicked offline detail
            Logger.e("onKickedOffline")
        }

        override fun onLoginClientChanged(
            change: V2NIMLoginClientChange,
            clients: List<V2NIMLoginClient>
        ) {
            Logger.e("onLoginClientChanged")
            // Handle login client change
        }
    }

    override fun initData() {


        mViewModel.chapterName.observe(this) {
            it?.let { binding.name.text = it }
        }
        mViewModel.link.observe(this) {
            it?.let { binding.desc.text = it }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NIMClient.getService(V2NIMLoginService::class.java).removeLoginListener(listener)
    }
}