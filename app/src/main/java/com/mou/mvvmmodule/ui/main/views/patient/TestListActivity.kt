package com.mou.mvvmmodule.ui.main.views.patient

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.core.basemvvm.mvvm.EmptyViewModel
import com.core.basemvvm.base.BaseActivity
import com.core.basemvvm.base.BaseListActivity
import com.mou.mvvmmodule.data.model.User
import com.mou.mvvmmodule.databinding.ActivityUserListBinding
import com.mou.mvvmmodule.ui.main.viewmodel.HomeFragmentViewModel
import com.mou.mvvmmodule.ui.main.viewmodel.UserListViewModel
import com.mou.mvvmmodule.ui.main.views.adapter.UserAdapter

/**
 * @FileName: TestListActivity.java
 * @author: villa_mou
 * @date: 09-19:09
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class TestListActivity : BaseListActivity<User, UserListViewModel>() {
    override fun provideAdapter(): BaseQuickAdapter<User, out BaseViewHolder> {
        return UserAdapter()
    }

    override fun providerVMClass(): Class<UserListViewModel> = UserListViewModel::class.java
    override fun initData() {
        mViewModel.refresh()
    }

    override val binding: ActivityUserListBinding by lazy {
        ActivityUserListBinding.inflate(layoutInflater)
    }
}