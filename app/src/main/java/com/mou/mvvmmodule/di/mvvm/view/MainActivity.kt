package com.mou.mvvmmodule.di.mvvm.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.fortunes.commonsdk.core.RouterConstants
import com.fortunes.commonsdk.network.onHttpSubscribeNoToast
import com.fortunes.commonsdk.utils.NavigationUtils
import com.mou.basemvvm.base.BaseActivity
import com.mou.basemvvm.helper.extens.bindDialogOrLifeCycle
import com.mou.basemvvm.helper.extens.toast
import com.mou.mvvmmodule.R
import com.mou.mvvmmodule.databinding.ActivityMainBinding
import com.mou.mvvmmodule.di.mvvm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


@Route(path = RouterConstants.MAIN_ACTIVITY)
class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {
    override fun providerVMClass()=MainViewModel::class.java
    override fun getLayoutId() = R.layout.activity_main
    override fun initView() {
        //设置viewModel
        mBinding.apply {
            vm=mViewModel
        }
        btn.setOnClickListener {
            mViewModel
                .getArticle()
                .bindDialogOrLifeCycle(this)
                .onHttpSubscribeNoToast(this) {
                    toast("成功")

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
    }
}

