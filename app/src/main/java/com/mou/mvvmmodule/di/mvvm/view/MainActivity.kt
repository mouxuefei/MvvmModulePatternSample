package com.mou.mvvmmodule.di.mvvm.view

import android.arch.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.fortunes.commonsdk.base.BaseActivity
import com.fortunes.commonsdk.core.RouterConstants
import com.fortunes.commonsdk.network.dealResult
import com.fortunes.commonsdk.utils.NavigationUtils
import com.mou.basemvvm.helper.extens.bindDialogOrLifeCycle
import com.mou.mvvmmodule.R
import com.mou.mvvmmodule.di.mvvm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = RouterConstants.MAIN_ACTIVITY)
class MainActivity : BaseActivity<MainViewModel>() {
    override fun providerVMClass()=MainViewModel::class.java
    override fun getLayoutId() = R.layout.activity_main
    override fun initView() {
        btn.setOnClickListener {
            mViewModel.run {
                    this.getArticle()
                    .bindDialogOrLifeCycle(this@MainActivity)
                    .dealResult(this@MainActivity)
            }

        }
        btn_login.setOnClickListener {
            NavigationUtils.goLoginActivity()
        }

        btn_mine.setOnClickListener {
            NavigationUtils.goMineActivity()
        }
    }

    override fun initData() {
        mViewModel.chapterName.observe(this, Observer{
           it?.let { name.text=it }
        })
        mViewModel.link.observe(this, Observer{
            it?.let { desc.text=it }
        })
    }
}

