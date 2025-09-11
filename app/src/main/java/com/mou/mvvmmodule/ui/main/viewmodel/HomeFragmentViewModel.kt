package com.mou.mvvmmodule.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.basemvvm.mvvm.BaseViewModel
import com.mou.mvvmmodule.data.Api
import com.mou.mvvmmodule.data.model.User
import com.orhanobut.logger.Logger

/**
 * @FileName: HomeFragmentViewModel.java
 * @author: villa_mou
 * @date: 08-11:46
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class HomeFragmentViewModel : BaseViewModel() {
    private val _chapterName = MutableLiveData<String>()
    val chapterName: LiveData<String> get() = _chapterName


    private val _link = MutableLiveData<String>()
    val link: LiveData<String> get() = _link


    private val _users = MutableLiveData<MutableList<User>>()
    val users: LiveData<MutableList<User>> get() = _users


    fun loadUsers() {
        // 模拟网络/本地数据
        val list = mutableListOf(
            User(1, "张三", 20),
            User(2, "李四", 25),
            User(3, "王五", 30)
        )
        _users.value = list
    }

    fun addUser(user: User) {
        val current = _users.value ?: mutableListOf()
        current.add(user)
        _users.value = current
    }

    fun loadUser() {
        launchWithLoading({
            Api.loginService.getArticle()
        }, {
            _chapterName.value = it.data.datas[0].chapterName
            _link.postValue(it.data.datas[0].link)
        }, {
        })
    }


}