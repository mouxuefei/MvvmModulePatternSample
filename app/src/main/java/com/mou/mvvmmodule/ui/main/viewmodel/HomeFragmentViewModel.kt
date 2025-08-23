package com.mou.mvvmmodule.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fortunes.commonsdk.network.bean.BaseBean
import com.mou.basemvvm.helper.extens.async
import com.mou.basemvvm.mvvm.BaseVMModel
import com.mou.mvvmmodule.ui.main.bean.ArticleBean
import com.mou.mvvmmodule.ui.main.model.MainModel
import com.orhanobut.logger.Logger
import io.reactivex.Single

/**
 * @FileName: HomeFragmentViewModel.java
 * @author: villa_mou
 * @date: 08-11:46
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class HomeFragmentViewModel : BaseVMModel<MainModel>() {
    override var mModel: MainModel = MainModel()
    val chapterName = MutableLiveData<String>()
    val link = MutableLiveData<String>()

    fun getArticle(): Single<BaseBean<ArticleBean>> {
        return mModel
            .getArticle()
            .async()
            .doOnSuccess {
                chapterName.value=it.data.datas[0].chapterName
                link.postValue(it.data.datas[0].link)
            }
            .doOnError {
                Logger.d("doOnError")
            }
    }


}