package com.core.basemvvm.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.core.basemvvm.mvvm.BaseViewModel
import com.core.basemvvm.mvvm.IActivity
import com.core.basemvvm.mvvm.IView
import com.core.basemvvm.widget.LoadDialog

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
    // 子类提供 ViewBinding
    protected abstract val binding: ViewBinding

    abstract fun providerVMClass(): Class<VM>?
    private val progressDialog: LoadDialog by lazy {
        LoadDialog.create(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        initVM()
        initView()
        initData()
        initLoadingObserver()
    }

    private fun initLoadingObserver() {
        mViewModel.loadingDialogState.observe(this) { state ->
            if (state.isLoading) {
                showLoading(state.message)
            } else {
                hideLoading()
            }
        }
    }

    private fun initVM() {
        providerVMClass()?.let {
            mViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(it)
        }

    }

    override fun showLoading(message: String?) {
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