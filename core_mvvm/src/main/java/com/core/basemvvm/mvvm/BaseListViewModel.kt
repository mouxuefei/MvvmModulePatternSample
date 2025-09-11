package com.core.basemvvm.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.basemvvm.data.PageUiState

abstract class BaseListViewModel<T> : BaseViewModel() {

    private val _uiState = MutableLiveData(PageUiState<T>())
    val uiState: LiveData<PageUiState<T>> get() = _uiState

    private var page = 1
    private val pageSize = 20

    fun refresh() {
        page = 1
        _uiState.value = _uiState.value?.copy(isRefreshing = true)
        loadData(page, pageSize, true)
    }

    fun loadMore() {
        if (_uiState.value?.hasMore != true) return
        page++
        loadData(page, pageSize, false)
    }

    protected fun submitData(data: MutableList<T>, isRefresh: Boolean) {
        val currentList =
            if (isRefresh) mutableListOf() else _uiState.value?.list ?: mutableListOf()
        val newList = (currentList + data).toMutableList()
        _uiState.value = PageUiState(
            list = newList,
            isRefreshing = false,
            hasMore = data.size >= pageSize,
            isEmpty = newList.isEmpty()
        )
    }

    protected fun submitError(msg: String, isRefresh: Boolean) {
        _uiState.value = _uiState.value?.copy(
            isRefreshing = isRefresh,
            loadError = true,
            errorMsg = msg
        )
    }

    abstract fun loadData(page: Int, pageSize: Int, isRefresh: Boolean)
}