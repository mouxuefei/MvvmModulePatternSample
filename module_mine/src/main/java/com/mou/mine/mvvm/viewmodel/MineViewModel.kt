package com.mou.mine.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fortunes.commonsdk.network.bean.BaseBean
import com.mou.basemvvm.mvvm.BaseVMModel
import com.mou.basemvvm.helper.extens.async
import com.mou.basemvvm.helper.network.EmptyException
import com.mou.basemvvm.helper.network.NoMoreDataException
import com.mou.mine.mvvm.bean.MineBean
import com.mou.mine.mvvm.bean.SubData
import com.mou.mine.mvvm.model.MineModel
import io.reactivex.Single

/**
 * @FileName: MineViewModel.java
 * @author: villa_mou
 * @date: 06-16:19
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class MineViewModel : BaseVMModel<MineModel>() {
    override var mModel: MineModel = MineModel()

    /**
     * MutableList的setValue()只能在主线程中调用，postValue()可以在任何线程中调用。
     */
    val mineItemLiveData: MutableLiveData<MutableList<SubData>> = MutableLiveData()
    val isMoreData: MutableLiveData<Boolean> = MutableLiveData(false)
    private var page = 1
    private val pageSize = 15
    fun getProjectList(
        isRefresh: Boolean,
        cid: Int
    ): Single<BaseBean<MineBean>> {
        val currentPage = if (isRefresh) 1 else page
        Log.e("villa", "page===" + currentPage)
        return mModel
            .getProjectList(currentPage, cid)
            .async()
            .doOnSuccess {
                if (it.data.datas.isNotEmpty()) {
                    page++
                    val liveData = it.data.datas
                    if (isRefresh) {
                        mineItemLiveData.value = liveData
                    } else {
                        val oldData = mineItemLiveData.value
                        val newData = oldData ?: mutableListOf()
                        newData.addAll(liveData)
                        mineItemLiveData.postValue(newData)
                        if (liveData.size < pageSize) {
                            throw NoMoreDataException()
                        }
                    }
                } else {
                    //判断是真的空还是加载更多为空
                    if (page == 1) throw EmptyException() else throw NoMoreDataException()
                }
            }
    }
}

