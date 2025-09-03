package com.mou.mvvmmodule.ui.login.views

import com.core.commonsdk.base.BaseActivity
import com.core.commonsdk.utils.ActRouter
import com.google.zxing.integration.android.IntentIntegrator
import com.mou.mvvmmodule.databinding.ActivityLoginBinding
import com.mou.mvvmmodule.ui.login.viewmodel.LoginViewModel
import com.mou.mvvmmodule.ui.main.views.MainActivity
import com.mou.mvvmmodule.ui.main.views.QRScanActivity

/**
 * @FileName: LoginActivity.java
 * @author: villa_mou
 * @date: 08-10:55
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class LoginActivity : BaseActivity<LoginViewModel>() {
    override val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun providerVMClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun initView() {
        binding.btnLogin.setOnClickListener {
            ActRouter.startActivity(this, QRScanActivity::class.java)
            // 在 Activity 中

        }
    }

    override fun initData() {
    }


}