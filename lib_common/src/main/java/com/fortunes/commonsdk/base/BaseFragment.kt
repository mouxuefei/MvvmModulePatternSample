package com.fortunes.commonsdk.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mou.basemvvm.mvvm.IActivity
import com.mou.basemvvm.mvvm.IView
import com.mou.basemvvm.widget.LoadDialog


/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *
 * Created by mou on 2018/8/20.
 * Fragment的父类
 */

abstract class BaseFragment<VM : ViewModel> : Fragment(), IView, IActivity {
    //上下文
    protected lateinit var mContext: Context
    lateinit var mViewModel: VM

    //数据是否加载标识
    private var isDataInitiated = false

    //view是否加载标识
    private var isViewInitiated = false

    //fragment是否显示
    private var isVisibleToUser = false

    private val progressDialog: LoadDialog by lazy {
        LoadDialog.create(mContext)
    }

    /**
     * 是否懒加载
     * true:是
     * false:不(默认)
     */
    protected open fun lazyLoad() = false

    /**
     * 是否fragment显示的时候都重新加载数据
     */
    protected open fun reLoad() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = activity ?: throw Exception("activity is null")
        initVM()
        initView()
        //判断是否懒加载
        if (lazyLoad()) {
            //将view加载的标识设置为true
            isViewInitiated = true
            prepareData()
        } else {
            initData()
        }
    }

    /**
     * fragment是否显示当前界面
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        prepareData()
    }

    /**
     * 懒加载的方法
     */
    private fun prepareData() {
        //通过判断各种标识去进行数据加载
        if (isVisibleToUser && isViewInitiated && !isDataInitiated) {
            initData()
            if (reLoad()) return
            isDataInitiated = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), null, false)
    }

    abstract fun providerVMClass(): Class<VM>?
    private fun initVM() {
        providerVMClass()?.let {
            activity?.application?.let { app ->
                ViewModelProvider.AndroidViewModelFactory.getInstance(
                    app
                ).create(it)
            }
        }
    }


    /**
     * 显示loading框
     */
    override fun showLoading(message: String) {
        progressDialog.setMessage(message)
        progressDialog.show()
    }

    /**
     * 隐藏loading框
     */
    override fun hideLoading() {
        progressDialog.dismiss()
    }


    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
    }
}