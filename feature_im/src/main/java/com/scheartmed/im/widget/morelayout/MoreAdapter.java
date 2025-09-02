package com.scheartmed.im.widget.morelayout;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scheartmed.im.R;

import java.util.List;

import androidx.annotation.Nullable;

public class MoreAdapter extends BaseQuickAdapter<MoreLayoutItemBean, BaseViewHolder> {


    public MoreAdapter(@Nullable List<MoreLayoutItemBean> data, int index, int pageSize) {
        super(R.layout.item_more, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MoreLayoutItemBean item) {
        helper.setImageResource(R.id.item_more_icon, item.resId);
    }


}
