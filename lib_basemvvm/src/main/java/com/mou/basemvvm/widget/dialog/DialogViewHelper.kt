package com.mou.basemvvm.widget.dialog

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import java.lang.ref.WeakReference

/**
 * Created by mou on 2017/12/3.
 */

internal class DialogViewHelper() {
    var contentView: View? = null
    private val mViews: SparseArray<WeakReference<View>> = SparseArray()

    constructor(context: Context, layoutResId: Int) : this() {
        contentView = LayoutInflater.from(context).inflate(layoutResId, null)
    }


    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    fun setText(viewId: Int, text: CharSequence) {
        val tv = getView<TextView>(viewId)
        if (tv != null) {
            tv.text = text
        }
    }


    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(viewId: Int): T? {
        val viewReference = mViews.get(viewId)
        var view: View? = null
        if (viewReference != null) {
            view = viewReference.get()
        }

        if (view == null) {
            view = contentView!!.findViewById(viewId)
            if (view != null) {
                mViews.put(viewId, WeakReference(view))
            }
        }
        return view as T?
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    fun setOnclickListener(viewId: Int, listener: View.OnClickListener) {
        val view = getView<View>(viewId)
        view?.setOnClickListener(listener)
    }

    fun setGone(viewId: Int, visibility: Int) {
        val view = getView<View>(viewId)
        if (view != null) {
            view.visibility = visibility
        }
    }
}
