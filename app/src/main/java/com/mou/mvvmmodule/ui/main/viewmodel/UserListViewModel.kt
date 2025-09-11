package com.mou.mvvmmodule.ui.main.viewmodel

import com.core.basemvvm.mvvm.BaseListViewModel
import com.mou.mvvmmodule.data.model.User

class UserListViewModel : BaseListViewModel<User>() {
    override fun loadData(page: Int) {
        // 模拟网络请求
        try {
            val mockData = if (page <= 3) {
                List(10) { User("用户${(page - 1) * 10 + it}") }
            } else {
                emptyList()
            }
            onError()
            onSuccess(page, mockData)
        } catch (e: Exception) {
            onError()
        }
    }
}