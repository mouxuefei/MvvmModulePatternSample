package com.mou.basemvvm.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.mou.basemvvm.helper.listener.ClickPresenter
import com.mou.basemvvm.widget.LoadDialog
import com.noober.background.BackgroundLibrary

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
 * Activity的父类
 */

abstract class BaseActivity<B : ViewDataBinding,VM: ViewModel> : AppCompatActivity(), IView, IActivity, ClickPresenter {
    protected lateinit var mBinding: B
    lateinit var mViewModel: VM

    private val progressDialog: LoadDialog by lazy {
        LoadDialog.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BackgroundLibrary.inject(this)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        super.onCreate(savedInstanceState)
        mBinding.lifecycleOwner = this
        initVM()
        initView()
        initData()
    }
    abstract fun providerVMClass(): Class<VM>?
    private fun initVM() {
        providerVMClass()?.let {
            mViewModel = ViewModelProviders.of(this).get(it)
        }
    }

    override fun onClick(v: View) {

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