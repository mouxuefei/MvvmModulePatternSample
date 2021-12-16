package com.mou.basemvvm.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mou.basemvvm.helper.listener.ClickPresenter
import com.mou.basemvvm.widget.LoadDialog
import com.noober.background.BackgroundLibrary
import dagger.Lazy
import dagger.android.AndroidInjection
import javax.inject.Inject

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

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity(), IView, IActivity, ClickPresenter {
    protected lateinit var mBinding: B

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>

    private val progressDialog: LoadDialog by lazy {
        LoadDialog.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BackgroundLibrary.inject(this)
        AndroidInjection.inject(this)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        super.onCreate(savedInstanceState)
        mBinding.lifecycleOwner = this
        initView()
        initData()
    }

     inline fun < reified T : ViewModel> createVM(): T = ViewModelProviders.of(this, factory.get()).get(T::class.java)


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