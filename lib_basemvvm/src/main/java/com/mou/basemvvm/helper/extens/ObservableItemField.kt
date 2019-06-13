package com.mou.basemvvm.helper.extens

import android.arch.lifecycle.MutableLiveData

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
 * Created by mou on 2018/12/6.
 */
class ObservableItemField<T> : MutableLiveData<T>() {

    fun set(value: T) {
        setValue(value)
    }

    fun get(): T? {
        return super.getValue()
    }
}