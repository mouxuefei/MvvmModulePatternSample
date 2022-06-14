package com.mou.basemvvm.widget.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window

/**
 * Created by mou on 2017/12/3.
 */

internal class AlertController(val dialog: CommonDialog, val window: Window) {

    private lateinit var mViewHelper: DialogViewHelper

    fun setViewHelper(viewHelper: DialogViewHelper) {
        this.mViewHelper = viewHelper
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    fun setText(viewId: Int, text: CharSequence) {
        mViewHelper.setText(viewId, text)
    }

    fun <T : View> getView(viewId: Int): T? {
        return mViewHelper.getView(viewId)
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    fun setOnclickListener(viewId: Int, listener: View.OnClickListener) {
        mViewHelper.setOnclickListener(viewId, listener)
    }

    fun setGone(viewId: Int, visibility: Int) {
        mViewHelper.setGone(viewId, visibility)
    }

    class AlertParams(var mContext: Context, var mThemeResId: Int) {
        // 点击空白是否能够取消  默认点击阴影不可以取消
        var mCancelable = true
        var mCancelableOnTouchOutside = true
        // dialog Cancel监听
        var mOnCancelListener: DialogInterface.OnCancelListener? = null
        // dialog Dismiss监听
        var mOnDismissListener: DialogInterface.OnDismissListener? = null
        // dialog Key监听
        var mOnKeyListener: DialogInterface.OnKeyListener? = null
        // 布局View
        var mView: View? = null
        // 布局layout id
        var mViewLayoutResId: Int = 0
        // 存放字体的修改
        var mTextArray = SparseArray<CharSequence>()
        // 存放点击事件
        var mClickArray = SparseArray<View.OnClickListener>()
        @SuppressLint("UseSparseArrays")
        //存放view的显示与隐藏
        var mVisibility = SparseArray<Int>()
        // 宽度
        var mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
        // 动画
        var mAnimations = 0
        // 位置
        var mGravity = Gravity.CENTER
        // 高度
        var mHeight = ViewGroup.LayoutParams.WRAP_CONTENT

        /**
         * 绑定和设置参数
         *
         * @param mAlert
         */
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        fun apply(mAlert: AlertController) {
            var viewHelper: DialogViewHelper? = null
            if (mViewLayoutResId != 0) {
                viewHelper = DialogViewHelper(mContext, mViewLayoutResId)
            }

            if (mView != null) {
                viewHelper = DialogViewHelper()
                viewHelper.contentView = mView
            }
            if (viewHelper == null) {
                throw IllegalArgumentException("请设置布局setContentView()")
            }
            viewHelper.contentView?.let { mAlert.dialog.setContentView(it) }

            mAlert.setViewHelper(viewHelper)

            val textArraySize = mTextArray.size()
            for (i in 0 until textArraySize) {
                mAlert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i))
            }

            val clickArraySize = mClickArray.size()
            for (i in 0 until clickArraySize) {
                mAlert.setOnclickListener(mClickArray.keyAt(i), mClickArray.valueAt(i))
            }

            val visibility = mVisibility.size()
            for (i in 0 until visibility) {
                mAlert.setGone(mVisibility.keyAt(i), mVisibility.valueAt(i))
            }

            val window = mAlert.window
            window.setGravity(mGravity)

            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations)
            }

            val params = window.attributes
            params.width = mWidth
            params.height = mHeight
            window.attributes = params
        }
    }
}
