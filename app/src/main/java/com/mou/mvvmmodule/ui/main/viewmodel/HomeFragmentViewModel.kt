package com.mou.mvvmmodule.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.core.basemvvm.mvvm.BaseViewModel
import com.mou.mvvmmodule.data.Api
import com.orhanobut.logger.Logger

/**
 * @FileName: HomeFragmentViewModel.java
 * @author: villa_mou
 * @date: 08-11:46
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class HomeFragmentViewModel : BaseViewModel() {
    val chapterName = MutableLiveData<String>()
    val link = MutableLiveData<String>()


    fun loadUser() {
        launchWithLoading({
            Api.loginService.getArticle()
        }, {
            chapterName.value = it.data.datas[0].chapterName
            link.postValue(it.data.datas[0].link)
        },{
        })
    }


}