package com.fortunes.commonsdk.view.statusview

/***
 * You may think you know what the following code does.
 * But you dont. Trust me.
 * Fiddle with it, and youll spend many a sleepless
 * night cursing the moment you thought youd be clever
 * enough to "optimize" the code below.
 * Now close this file and go play with something else.
 */


import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.fortunes.commonsdk.R
import java.util.*

/***
 *
 * █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 * ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 * ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 * ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 * ░     ░ ░      ░  ░
 *
 * Created by mou on 2018/11/22.


 * 通用页面状态布局
 */
class MultipleStatusView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mLoadingView: View? = null
    private var mNoNetworkView: View? = null
    private var mContentView: View? = null
    private val mEmptyViewResId: Int
    private val mErrorViewResId: Int
    private val mLoadingViewResId: Int
    private val mNoNetworkViewResId: Int
    private val mContentViewResId: Int

    /**
     * 获取当前状态
     */
    private var viewStatus: Int = 0
    private val mInflater: LayoutInflater
    private var mOnRetryClickListener: View.OnClickListener? = null

    private val mOtherIds = ArrayList<Int>()


    init {
        @SuppressLint("CustomViewStyleable") val a = context.obtainStyledAttributes(attrs, R.styleable.public_MultipleStatusView, defStyleAttr, 0)
        mEmptyViewResId = a.getResourceId(R.styleable.public_MultipleStatusView_public_emptyView, R.layout.public_status_empty_view)
        mErrorViewResId = a.getResourceId(R.styleable.public_MultipleStatusView_public_errorView, R.layout.public_status_error_view)
        mLoadingViewResId = a.getResourceId(R.styleable.public_MultipleStatusView_public_loadingView, R.layout.public_status_loading_view)
        mNoNetworkViewResId = a.getResourceId(R.styleable.public_MultipleStatusView_public_noNetworkView, R.layout.public_status_no_network_view)
        mContentViewResId = a.getResourceId(R.styleable.public_MultipleStatusView_public_contentView, NULL_RESOURCE_ID)
        a.recycle()
        mInflater = LayoutInflater.from(getContext())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        showContent()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear(mEmptyView, mLoadingView, mEmptyView, mNoNetworkView)
        mOtherIds.clear()
        if (mOnRetryClickListener != null) {
            mOnRetryClickListener = null
        }
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    fun setOnRetryClickListener(onRetryClickListener: View.OnClickListener) {
        this.mOnRetryClickListener = onRetryClickListener
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showEmpty(layoutId: Int = mEmptyViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showEmpty(mEmptyView ?: inflateView(layoutId), layoutParams)
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private fun showEmpty(view: View, layoutParams: ViewGroup.LayoutParams) {
        viewStatus = STATUS_EMPTY
        if (mEmptyView == null) {
            mEmptyView = view
            val emptyRetryView = mEmptyView!!.findViewById<View>(R.id.public_empty_retry_view)
            if (mOnRetryClickListener != null && emptyRetryView != null) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mEmptyView!!.id)
            addView(mEmptyView, 0, layoutParams)
        }
        showViewById(mEmptyView!!.id)
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showError(layoutId: Int = mErrorViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showError(mErrorView ?: inflateView(layoutId), layoutParams)
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private fun showError(view: View, layoutParams: ViewGroup.LayoutParams) {
        viewStatus = STATUS_ERROR
        if (mErrorView == null) {
            mErrorView = view
            if (mOnRetryClickListener != null && mErrorView != null) {
                mErrorView!!.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mErrorView!!.id)
            addView(mErrorView, 0, layoutParams)
        }
        showViewById(mErrorView!!.id)
    }

    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showLoading(layoutId: Int = mLoadingViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showLoading(mLoadingView ?: inflateView(layoutId), layoutParams)
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private fun showLoading(view: View, layoutParams: ViewGroup.LayoutParams) {
        viewStatus = STATUS_LOADING
        if (mLoadingView == null) {
            mLoadingView = view
            mOtherIds.add(mLoadingView!!.id)
            addView(mLoadingView, 0, layoutParams)
        }
        showViewById(mLoadingView!!.id)
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showNoNetwork(layoutId: Int = mNoNetworkViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showNoNetwork(mNoNetworkView ?: inflateView(layoutId), layoutParams)
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private fun showNoNetwork(view: View, layoutParams: ViewGroup.LayoutParams) {
        viewStatus = STATUS_NO_NETWORK
        if (mNoNetworkView == null) {
            mNoNetworkView = view
            val noNetworkRetryView = mNoNetworkView!!.findViewById<View>(R.id.public_no_network_retry_view)
            if (mOnRetryClickListener != null && noNetworkRetryView != null) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mNoNetworkView!!.id)
            addView(mNoNetworkView, 0, layoutParams)
        }
        showViewById(mNoNetworkView!!.id)
    }

    /**
     * 显示内容视图
     */
    fun showContent() {
        viewStatus = STATUS_CONTENT
        if (mContentView == null && mContentViewResId != NULL_RESOURCE_ID) {
            mContentView = mInflater.inflate(mContentViewResId, null)
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS)
        }
        showContentView()
    }

    private fun inflateView(layoutId: Int): View {
        return mInflater.inflate(layoutId, null)
    }

    private fun showViewById(viewId: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (view.id == viewId) View.VISIBLE else View.GONE
        }
    }

    private fun showContentView() {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (mOtherIds.contains(view.id)) View.GONE else View.VISIBLE
        }
    }

    private fun clear(vararg views: View?) {
        for (view in views) {
            view?.let {
                this.removeView(it)
            }
        }
    }

    companion object {
        private val DEFAULT_LAYOUT_PARAMS = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)

        const val STATUS_CONTENT = 0x00
        const val STATUS_LOADING = 0x01
        const val STATUS_EMPTY = 0x02
        const val STATUS_ERROR = 0x03
        const val STATUS_NO_NETWORK = 0x04

        private const val NULL_RESOURCE_ID = -1
    }
}