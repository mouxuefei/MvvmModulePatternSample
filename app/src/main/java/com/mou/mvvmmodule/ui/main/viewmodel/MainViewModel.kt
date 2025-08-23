package com.mou.mvvmmodule.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fortunes.commonsdk.network.bean.BaseBean
import com.mou.basemvvm.helper.extens.async
import com.mou.basemvvm.mvvm.BaseVMModel
import com.mou.mvvmmodule.ui.main.bean.ArticleBean
import com.mou.mvvmmodule.ui.main.model.MainModel
import com.orhanobut.logger.Logger
import io.reactivex.Single


class MainViewModel : BaseVMModel<MainModel>(){
    override var mModel: MainModel = MainModel()

}

