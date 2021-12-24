package com.mou.mvvmmodule.di.mvvm.viewmodel

import com.fortunes.commonsdk.network.bean.BaseBean
import com.mou.basemvvm.helper.extens.ObservableItemField
import com.mou.basemvvm.helper.extens.async
import com.mou.basemvvm.mvvm.BaseVMModel
import com.mou.mvvmmodule.di.mvvm.bean.ArticleBean
import com.mou.mvvmmodule.di.mvvm.model.MainModel
import io.reactivex.Single
import timber.log.Timber


class MainViewModel : BaseVMModel<MainModel>(){
    override var mModel: MainModel=MainModel()
    val chapterName = ObservableItemField<String>()
    val link = ObservableItemField<String>()

    fun getArticle(): Single<BaseBean<ArticleBean>> {
        return mModel
            .getArticle()
            .async()
            .doOnSuccess {
                chapterName.set(it.data.datas[0].chapterName)
                link.set(it.data.datas[0].link)
            }
            .doOnError {
                Timber.d("doOnError")
            }
    }
}

