package com.mou.login.mvvm.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.fortunes.commonsdk.core.RouterConstants
import com.mou.basemvvm.base.BaseActivity
import com.mou.login.R
import com.mou.login.databinding.LoginActivityLoginBinding
import com.mou.login.mvvm.viewmodel.LoginViewModel

/**
 * @FileName: LoginActivity.java
 * @author: villa_mou
 * @date: 06-16:18
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
@Route(path = RouterConstants.LOGIN_ACTIVITY)
class LoginActivity: BaseActivity<LoginActivityLoginBinding>() {

    private val mViewModel by lazy {
        createVM<LoginViewModel>()
    }
    override fun getLayoutId()= R.layout.login_activity_login

    override fun initView() {
    }

    override fun initData() {
    }
}