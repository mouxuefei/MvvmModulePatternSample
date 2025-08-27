package com.scheartmed.lib_im.widget.emoji;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scheartmed.lib_im.R;

import java.util.List;

import androidx.annotation.Nullable;

public class EmojiAdapter extends BaseQuickAdapter<EmojiBean, BaseViewHolder> {


    public EmojiAdapter(@Nullable List<EmojiBean> data, int index, int pageSize) {
        super(R.layout.item_emoji, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmojiBean item) {
        //判断是否为最后一个item
//        if (item.getId() == 0) {
//            helper.setBackgroundResource(R.id.et_emoji, R.mipmap.rc_icon_emoji_delete);
//        } else {
//            helper.setText(R.id.et_emoji, item.getUnicodeInt());
//        }


    }


}
