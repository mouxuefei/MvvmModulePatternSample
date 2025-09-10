package com.mou.mvvmmodule.ui.login.views

import android.app.Activity
import com.core.commonsdk.base.BaseActivity
import com.core.commonsdk.utils.ActRouter
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.PermissionLists
import com.hjq.permissions.permission.base.IPermission
import com.mou.mvvmmodule.databinding.ActivityLoginBinding
import com.mou.mvvmmodule.ui.login.viewmodel.LoginViewModel
import com.mou.mvvmmodule.ui.main.views.MainActivity

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
            XXPermissions.with(this).permissions(mutableListOf(PermissionLists.getPostNotificationsPermission(),PermissionLists.getWriteExternalStoragePermission()))
                .request(
                    OnPermissionCallback { grantedList: List<IPermission?>?, deniedList: List<IPermission?> ->
                        val allGranted = deniedList.isEmpty()
                        if (!allGranted) {
                            return@OnPermissionCallback
                        }
                        ActRouter.startActivity(this, MainActivity::class.java)
                    })

        }
    }

    override fun initData() {
    }


}