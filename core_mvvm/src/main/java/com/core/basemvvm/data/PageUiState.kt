package com.core.basemvvm.data

data class PageUiState<T>(
    val list: MutableList<T> = mutableListOf(),
    val isRefreshing: Boolean = false,//刷新
    val hasMore: Boolean = true,
    val isEmpty: Boolean = false,
    val errorMsg: String? = null,
    val loadError: Boolean = false,
)