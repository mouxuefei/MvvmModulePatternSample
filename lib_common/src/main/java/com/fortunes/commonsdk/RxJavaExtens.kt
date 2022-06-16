package com.mou.basemvvm.helper.extens

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.mou.basemvvm.helper.annotation.PageStateType
import com.mou.basemvvm.helper.annotation.RefreshType
import com.fortunes.commonsdk.network.bean.NoMoreDataException
import com.fortunes.commonsdk.network.bean.EmptyException
import com.mou.basemvvm.mvvm.BaseViewModel
import com.mou.basemvvm.mvvm.IView
import com.orhanobut.logger.Logger
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.SingleSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/***
 * You may think you know what the following code does.
 * But you dont. Trust me.
 * Fiddle with it, and youll spend many a sleepless
 * night cursing the moment you thought youd be clever
 * enough to "optimize" the code below.
 * Now close this file and go play with something else.
 */
/***
 *
 * Created by mou on 2018/12/3.
 * RxJava的扩展类
 */

/**
 * Single扩展的异步请求
 */
fun <T> Single<T>.async(withDelay: Long = 0): Single<T> =
    this.subscribeOn(Schedulers.io())
        .delay(withDelay, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())

/**
 * Observable扩展的异步请求
 */
fun <T> Observable<T>.async(withDelay: Long = 0): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .delay(withDelay, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())

/**
 * Single扩展函数绑定刷新/状态布局/生命周期
 */
fun <T> Single<T>.bindStatusOrLifeCycle(
    isRefresh: Boolean = true,
    viewModel: BaseViewModel,
    owner: LifecycleOwner
): SingleSubscribeProxy<T> =
    this.bindStatus(isRefresh, viewModel)
        .bindLifeCycle(owner)

/**
 * Single扩展函数绑定加载dialog/生命周期
 */
fun <T> Single<T>.bindDialogOrLifeCycle(view: IView): SingleSubscribeProxy<T> =
    this.bindDialog(view)
        .bindLifeCycle(view)

/**
 * Observable扩展函数绑定加载dialog/生命周期
 */
fun <T> Observable<T>.bindDialogOrLifeCycle(view: IView): ObservableSubscribeProxy<T> =
    this.bindDialog(view)
        .bindLifeCycle(view)


/**
 * Single扩展绑定生命周期
 */
fun <T> Single<T>.bindLifeCycle(owner: LifecycleOwner): SingleSubscribeProxy<T> =
    this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                owner,
                Lifecycle.Event.ON_DESTROY
            )
        )
    )


/**
 * Observable扩展绑定生命周期
 */
fun <T> Observable<T>.bindLifeCycle(owner: LifecycleOwner): ObservableSubscribeProxy<T> =
    this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                owner,
                Lifecycle.Event.ON_DESTROY
            )
        )
    )

/**
 * Single扩展函数绑定刷新/状态布局
 */
private fun <T> Single<T>.bindStatus(
    isRefresh: Boolean = true,
    viewModel: BaseViewModel
): Single<T> {
    return viewModel.run {
        this@bindStatus
            .doOnSubscribe {
                //注册前先判断是否显示加载loading
                if (pageState.value != PageStateType.CONTENT)
                    pageState.postValue(PageStateType.LOADING)
            }
            .doOnSuccess {
                //成功
                if (pageState.value != PageStateType.CONTENT)
                    pageState.postValue(PageStateType.CONTENT)
                //给列表设置是刷新还是加载更多
                if (isRefresh) {
                    listState.postValue(RefreshType.REFRESH)
                } else {
                    listState.postValue(RefreshType.LOADMORE)
                }
            }
            .doOnError {
                //页面状态
                if (pageState.value != PageStateType.CONTENT) {
                    if (it is EmptyException) {
                        pageState.postValue(PageStateType.EMPTY)
                    } else if (it is UnknownHostException || it is ConnectException) {
                        pageState.postValue(PageStateType.NOWORK)
                    } else {
                        pageState.postValue(PageStateType.ERROR)
                    }
                }
                // smartrefreshLayout状态
                if (isRefresh) {
                    listState.postValue(RefreshType.REFRESHFAIL)
                } else {
                    if (it is EmptyException || it is NoMoreDataException) {
                        listState.postValue(RefreshType.NOTMORE)
                    } else {
                        listState.postValue(RefreshType.LOADMOREFAIL)
                    }
                }
            }
    }
}

/**
 * Single扩展函数绑定加载dialog
 */
private fun <T> Single<T>.bindDialog(view: IView): Single<T> =
    this.doOnSubscribe {
        view.showLoading()
    }.doFinally {
        view.hideLoading()
    }

/**
 * Observable扩展函数绑定加载dialog
 */
private fun <T> Observable<T>.bindDialog(view: IView): Observable<T> =
    this.doOnSubscribe {
        Logger.i("showLoading")
        view.showLoading()
    }.doFinally {
        Logger.i("hideLoading")
        view.hideLoading()
    }