package com.mou.login.mvvm.viewmodel

import com.mou.basemvvm.mvvm.BaseVMModel
import com.mou.login.mvvm.model.LoginModel

/**
 * @FileName: LoginViewModel.java
 * @author: villa_mou
 * @date: 06-16:19
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class LoginViewModel: BaseVMModel<LoginModel>() {
    override var mModel: LoginModel=LoginModel()
}