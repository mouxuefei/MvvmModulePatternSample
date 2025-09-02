package com.scheartmed.im.widget.morelayout;

import androidx.annotation.DrawableRes;

/**
 * @version V1.0 <描述当前版本功能>
 * @FileName: MoreLayoutItemBean.java
 * @author: villa_mou
 * @date: 10-14:30
 * @desc
 */
public class MoreLayoutItemBean {

    public MoreLayoutItemBean(String key, int resId) {
        this.key = key;
        this.resId = resId;
    }
    public String key;
    @DrawableRes
    public int resId;
}
