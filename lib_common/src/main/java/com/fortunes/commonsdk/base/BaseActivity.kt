package com.fortunes.commonsdk.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mou.basemvvm.mvvm.BaseViewModel
import com.mou.basemvvm.mvvm.IActivity
import com.mou.basemvvm.mvvm.IView
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

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity(), IView, IActivity {
    lateinit var mViewModel: VM
    abstract fun providerVMClass(): Class<VM>?
    private val progressDialog: LoadDialog by lazy {
        LoadDialog.create(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BackgroundLibrary.inject(this)
        setContentView(getLayoutId())
        super.onCreate(savedInstanceState)
        initVM()
        initView()
        initData()
    }

    private fun initVM() {
        providerVMClass()?.let {
            mViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(it)
        }

    }

    override fun showLoading(message: String) {
        progressDialog.setMessage(message)
        progressDialog.show()
    }

    override fun hideLoading() {
        progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
    }
}