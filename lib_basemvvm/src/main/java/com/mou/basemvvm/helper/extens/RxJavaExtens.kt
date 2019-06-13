package com.mou.basemvvm.helper.extens

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.mou.basemvvm.base.BaseViewModel
import com.mou.basemvvm.base.IView
import com.mou.basemvvm.helper.annotation.PageStateType
import com.mou.basemvvm.helper.annotation.RefreshType
import com.mou.basemvvm.helper.network.EmptyException
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.SingleSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
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
fun <T> Single<T>.bindStatusOrLifeCycle(isRefresh: Boolean = true, viewModel: BaseViewModel, owner: LifecycleOwner): SingleSubscribeProxy<T> =
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
        this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))


/**
 * Observable扩展绑定生命周期
 */
fun <T> Observable<T>.bindLifeCycle(owner: LifecycleOwner): ObservableSubscribeProxy<T> =
        this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))

/**
 * Single扩展函数绑定刷新/状态布局
 */
private fun <T> Single<T>.bindStatus(isRefresh: Boolean = true, viewModel: BaseViewModel): Single<T> {
    return viewModel.run {
        this@bindStatus
                .doOnSubscribe {
                    //注册前先判断是否显示加载loading
                    if (pageState.get() != PageStateType.CONTENT)
                        pageState.set(PageStateType.LOADING)
                }
                .doOnSuccess {
                    //成功
                    if (pageState.get() != PageStateType.CONTENT)
                        pageState.set(PageStateType.CONTENT)
                    //给列表设置是刷新还是加载更多
                    if (isRefresh) {
                        listState.set(RefreshType.REFRESH)
                    } else {
                        listState.set(RefreshType.LOADMORE)
                    }
                }
                .doOnError {
                    //页面状态
                    if (pageState.get() != PageStateType.CONTENT) {
                        if (it is EmptyException) {
                            pageState.set(PageStateType.EMPTY)
                        } else if (it is UnknownHostException || it is ConnectException) {
                            pageState.set(PageStateType.NOWORK)
                        } else {
                            pageState.set(PageStateType.ERROR)
                        }
                    }
                    //smartrefreshLayout状态
                    if (isRefresh) {
                        listState.set(RefreshType.REFRESHFAIL)
                    } else {
                        if (it is EmptyException) {
                            listState.set(RefreshType.NOTMORE)
                        } else {
                            listState.set(RefreshType.LOADMOREFAIL)
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
            Timber.i("showLoading")
            view.showLoading()
        }.doFinally {
            Timber.i("hideLoading")
            view.hideLoading()
        }