package com.mou.mine.mvvm.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mou.mine.R
import com.mou.mine.mvvm.bean.SubData

/**
 * @FileName: MineListAdapter.java
 * @author: villa_mou
 * @date: 12-14:21
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class MineListAdapter : BaseQuickAdapter<SubData, BaseViewHolder>(R.layout.mine_my_item_order),
    LoadMoreModule {
    override fun convert(helper: BaseViewHolder, item: SubData) {
        helper.getView<TextView>(R.id.item_name).text = item.author
        helper.getView<TextView>(R.id.item_desc).text = item.title
    }
}