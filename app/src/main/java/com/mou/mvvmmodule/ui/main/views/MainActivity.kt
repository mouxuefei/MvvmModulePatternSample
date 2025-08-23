package com.mou.mvvmmodule.ui.main.views

import androidx.lifecycle.Observer
import com.fortunes.commonsdk.base.BaseActivity
import com.fortunes.commonsdk.network.dealResult
import com.mou.basemvvm.helper.extens.bindDialogOrLifeCycle
import com.mou.basemvvm.helper.extens.toast
import com.mou.mvvmmodule.databinding.ActivityMainBinding
import com.mou.mvvmmodule.ui.main.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel>() {
    override val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun providerVMClass() = MainViewModel::class.java

    override fun initView() {
        binding.btn.setOnClickListener {
            mViewModel.run {
                this.getArticle().bindDialogOrLifeCycle(this@MainActivity)
                    .dealResult(this@MainActivity)
            }

        }


        binding.btnLogin.setOnClickListener {
            toast("login")
        }

        binding.btnMine.setOnClickListener {}
    }

    override fun initData() {
        mViewModel.chapterName.observe(this, Observer {
            it?.let { binding.name.text = it }
        })
        mViewModel.link.observe(this, Observer {
            it?.let { binding.desc.text = it }
        })
    }
}

