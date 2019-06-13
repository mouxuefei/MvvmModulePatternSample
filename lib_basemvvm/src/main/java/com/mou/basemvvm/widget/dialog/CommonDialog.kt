package com.mou.basemvvm.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

import com.mou.easymvvm.R


/**
 * Created by mou on 2017/12/3.
 */

/**
 * CommonDialog使用说明
 * new CommonDialog
 * //构建dilaog，第一个参数上下文，第二个参数dialog样式(不传的话默认样式)
 * .Builder(this)
 * //设置dialog的layout，可以传view，也可以传layoutId
 * .setContentView()
 * //设置layout布局中textview的文字，第一个参数view的id，第二个参数为具体内容(可以多次调用)
 * .setText()
 * //设置layout布局中view的点击事件，第一个参数view的id，第二个参数为OnclickListener(可以多次调用)
 * .setOnClickListener()
 * //设置layout布局的宽高，默认为warp_parent
 * .setWidthAndHeight()
 * //设置layout布局的宽度充满屏幕，默认不充满屏幕
 * .fullWidth()
 * //设置layout布局的显示于屏幕底部，默认显示在中(参数为boolean值：是否显示从底部弹出动画)
 * .formBottom()
 * //设置layout布局的显示位置，默认在屏幕中间
 * .forGravity()
 * //设置layout是否添加默认显示动画，默认无动画
 * .addDefaultAnimation()
 * //设置layout的显示动画
 * .setAnimations()
 * //设置dialog取消时的监听事件
 * .setOnCancelListener()
 * //设置dialog消失时的监听事件
 * .setOnDismissListener()
 * //设置dialog显示时点击返回键的处理
 * .setOnKeyListener()
 * //创建dialog
 * .create()
 * //显示dialog
 * .show();
 */

class CommonDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private val mAlert: AlertController = AlertController(this, window!!)

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    fun setText(viewId: Int, text: CharSequence): CommonDialog {
        mAlert.setText(viewId, text)
        return this
    }

    fun <T : View> getView(viewId: Int): T? {
        return mAlert.getView(viewId)
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    fun setOnclickListener(viewId: Int, listener: View.OnClickListener): CommonDialog {
        mAlert.setOnclickListener(viewId, listener)
        return this
    }


    class Builder @JvmOverloads constructor(context: Context, themeResId: Int = R.style.dialog) {

        private val P: AlertController.AlertParams = AlertController.AlertParams(context, themeResId)

        fun setContentView(view: View): Builder {
            P.mView = view
            P.mViewLayoutResId = 0
            return this
        }

        // 设置布局内容的layoutId
        fun setContentView(layoutId: Int): Builder {
            P.mView = null
            P.mViewLayoutResId = layoutId
            return this
        }

        // 设置文本
        fun setText(viewId: Int, text: CharSequence): Builder {
            P.mTextArray.put(viewId, text)
            return this
        }

        // 设置点击事件
        fun setOnClickListener(view: Int, listener: View.OnClickListener): Builder {
            P.mClickArray.put(view, listener)
            return this
        }

        fun setVisibility(view: Int, visibility: Int): Builder {
            P.mVisibility.put(view, visibility)
            return this
        }

        // 是否充满屏幕宽度
        fun fullWidth(): Builder {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        fun fullHeight(): Builder {
            P.mHeight = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        /**
         * 从底部弹出
         *
         * @param isAnimation 是否有动画
         * @return
         */
        fun formBottom(isAnimation: Boolean): Builder {
            if (isAnimation) {
                P.mAnimations = R.style.dialog_from_bottom_anim
            }
            P.mGravity = Gravity.BOTTOM
            return this
        }

        /**
         * 设置dialog在屏幕中显示的位置
         *
         * @param gravity
         * @return
         */
        fun forGravity(gravity: Int): Builder {
            P.mGravity = gravity
            return this
        }

        /**
         * 设置Dialog的宽高
         *
         * @param width
         * @param height
         * @return
         */
        fun setWidthAndHeight(width: Int, height: Int): Builder {
            P.mWidth = width
            P.mHeight = height
            return this
        }

        /**
         * 添加默认动画
         *
         * @return
         */
        fun addDefaultAnimation(): Builder {
            P.mAnimations = R.style.dialog_scale_anim
            return this
        }

        /**
         * 设置动画
         *
         * @param styleAnimation
         * @return
         */
        fun setAnimations(styleAnimation: Int): Builder {
            P.mAnimations = styleAnimation
            return this
        }

        /**
         * 点击返回键是否可以消失
         */
        fun setCancelable(cancelable: Boolean): Builder {
            P.mCancelable = cancelable
            return this
        }

        /**
         * 点击空白处是否可以取消
         */
        fun setCancelableOnTouchOutside(cancelableOnTouchOutside: Boolean): Builder {
            P.mCancelableOnTouchOutside = cancelableOnTouchOutside
            return this
        }

        /**
         * 取消监听事件
         */
        fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): Builder {
            P.mOnCancelListener = onCancelListener
            return this
        }

        /**
         * 消失监听事件
         */
        fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): Builder {
            P.mOnDismissListener = onDismissListener
            return this
        }

        /**
         * 点击返回键监听事件
         */
        fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener): Builder {
            P.mOnKeyListener = onKeyListener
            return this
        }

        /**
         * 创建dialog
         *
         * @return
         */
        fun create(): CommonDialog {
            val dialog = CommonDialog(P.mContext, P.mThemeResId)
            P.apply(dialog.mAlert)
            dialog.setCancelable(P.mCancelable)
            dialog.setCanceledOnTouchOutside(P.mCancelableOnTouchOutside)
            dialog.setOnCancelListener(P.mOnCancelListener)
            dialog.setOnDismissListener(P.mOnDismissListener)
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener)
            }
            return dialog
        }

        /**
         * 显示dialog
         *
         * @return
         */
        fun show(): CommonDialog {
            val dialog = create()
            dialog.show()
            return dialog
        }
    }
}
