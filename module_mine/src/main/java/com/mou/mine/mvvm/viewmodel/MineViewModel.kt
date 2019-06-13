package com.mou.mine.mvvm.viewmodel

import com.fortunes.commonsdk.network.bean.BaseBean
import com.guoyang.recyclerviewbindingadapter.observable.ObservableAdapterList
import com.mou.basemvvm.base.BaseViewModel
import com.mou.basemvvm.helper.extens.ObservableItemField
import com.mou.basemvvm.helper.extens.async
import com.mou.basemvvm.helper.network.EmptyException
import com.mou.mine.mvvm.model.data.MineBean
import com.mou.mine.mvvm.model.data.SubData
import com.mou.mine.mvvm.model.repository.ApiService
import io.reactivex.Single
import javax.inject.Inject

/**
 * @FileName: MineViewModel.java
 * @author: villa_mou
 * @date: 06-16:19
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class MineViewModel @Inject constructor(private val apiService: ApiService) : BaseViewModel() {
    private var page = 1
    val observableList = ObservableAdapterList<MineItemViewModel>()
    fun getProjectList(isRefresh: Boolean, cid: Int): Single<BaseBean<MineBean>> {
        return apiService
            .getProjectList(
                if (isRefresh) {
                    page = 1
                    page
                } else page, cid
            )
            .async()
            .doOnSuccess {
                if (it.data.datas.isNotEmpty()) {
                    val list = mutableListOf<MineItemViewModel>()
                    it.data.datas.forEach { orderBean: SubData ->
                        list.add(MineItemViewModel(orderBean))
                    }
                    page++
                    if (isRefresh) {
                        observableList.setNewData(list)
                    } else {
                        observableList.addAll(list)
                    }
                } else {
                    throw EmptyException()
                }
            }
    }
}

class MineItemViewModel(bean: SubData) {
    val chapterName = ObservableItemField<String>()
    val desc = ObservableItemField<String>()
    val data = bean

    init {
        chapterName.set(bean.chapterName)
        desc.set(bean.desc)
    }
}