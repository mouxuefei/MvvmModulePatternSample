package com.mou.mvvmmodule.ui.main.views.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mou.mvvmmodule.R
import com.mou.mvvmmodule.data.model.User

class UserAdapter : BaseQuickAdapter<User, BaseViewHolder>(R.layout.item_user) {

    override fun convert(holder: BaseViewHolder, item: User) {
        holder.setText(R.id.tvName, item.name)
    }
}