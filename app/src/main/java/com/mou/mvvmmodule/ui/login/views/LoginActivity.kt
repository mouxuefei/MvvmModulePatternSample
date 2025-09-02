package com.mou.mvvmmodule.ui.login.views

import androidx.viewbinding.ViewBinding
import com.fortunes.commonsdk.base.BaseActivity
import com.mou.mvvmmodule.databinding.ActivityLoginBinding
import com.mou.mvvmmodule.ui.login.viewmodel.LoginViewModel

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

    }

    override fun initData() {
    }


}