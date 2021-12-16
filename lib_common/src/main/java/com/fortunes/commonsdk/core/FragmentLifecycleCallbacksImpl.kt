package com.fortunes.commonsdk.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber


/***
 *
 * Created by mou on 2018/9/11.
 */
class FragmentLifecycleCallbacksImpl : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        Timber.i("${f.javaClass.simpleName} onFragmentCreated")
    }
}