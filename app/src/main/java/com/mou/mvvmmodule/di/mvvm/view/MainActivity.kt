package com.mou.mvvmmodule.di.mvvm.view

import androidx.lifecycle.Observer
import com.fortunes.commonsdk.base.BaseActivity
import com.fortunes.commonsdk.network.dealResult
import com.mou.basemvvm.helper.extens.bindDialogOrLifeCycle
import com.mou.mvvmmodule.R
import com.mou.mvvmmodule.di.mvvm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

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
        }

        btn_mine.setOnClickListener {
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

