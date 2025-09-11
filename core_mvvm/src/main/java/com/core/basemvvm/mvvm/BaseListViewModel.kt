package com.core.basemvvm.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

enum class ListStatus {
    LOADING, SUCCESS, EMPTY, ERROR, NO_NETWORK_ERROR, LOAD_MORE_ERROR
}

abstract class BaseListViewModel<T> : BaseViewModel() {

    private val _items = MutableLiveData<List<T>>()
    val items: LiveData<List<T>> get() = _items

    private val _status = MutableLiveData<ListStatus>()
    val status: LiveData<ListStatus> get() = _status

    private var currentPage = 1
    private var isRefreshing = false

    fun refresh() {
        isRefreshing = true
        currentPage = 1
        _status.value = ListStatus.LOADING
        loadData(currentPage)
    }

    fun loadMore() {
        isRefreshing = false
        loadData(currentPage + 1)
    }

    private fun setData(page: Int, newList: List<T>) {
        if (page == 1) {
            if (newList.isEmpty()) {
                _status.value = ListStatus.EMPTY
                _items.value = emptyList()
            } else {
                _status.value = ListStatus.SUCCESS
                _items.value = newList
            }
        } else {
            val oldList = _items.value?.toMutableList() ?: mutableListOf()
            oldList.addAll(newList)
            _items.value = oldList
            _status.value = ListStatus.SUCCESS
        }
        currentPage = page
    }

    protected fun onSuccess(page: Int, data: List<T>) {
        setData(page, data)
    }

    protected fun onError() {
        if (isRefreshing || currentPage == 1) {
            _status.value = ListStatus.ERROR
        } else {
            _status.value = ListStatus.LOAD_MORE_ERROR
        }
    }

    /**
     * 子类实现具体的数据加载逻辑
     */
    protected abstract fun loadData(page: Int)
}