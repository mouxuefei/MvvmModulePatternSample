package com.fortunes.commonsdk.view.photoviewer

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v4.app.Fragment

abstract class BaseLazyFragment : Fragment() {
    /**
     * 懒加载过
     */
    private var isLazyLoaded: Boolean = false

    private var isPrepared: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isPrepared = true
        //只有Fragment onCreateView好了，
        //另外这里调用一次lazyLoad(）
        lazyLoad()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyLoad()
    }

    /**
     * 调用懒加载
     */
    private fun lazyLoad() {
        if (userVisibleHint && isPrepared && !isLazyLoaded) {
            onLazyLoad()
            isLazyLoaded = true
        }
    }

    @UiThread
    abstract fun onLazyLoad()
}