package com.mou.login.mvvm.viewmodel

import com.mou.basemvvm.base.BaseViewModel
import com.mou.login.mvvm.model.repository.ApiService
import javax.inject.Inject

/**
 * @FileName: LoginViewModel.java
 * @author: villa_mou
 * @date: 06-16:19
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class LoginViewModel @Inject constructor(private val apiService: ApiService) : BaseViewModel() {

}