package com.scheartmed.lib_im.widget.morelayout;

import androidx.annotation.DrawableRes;

/**
 * @version V1.0 <描述当前版本功能>
 * @FileName: MoreLayoutItemBean.java
 * @author: villa_mou
 * @date: 10-14:30
 * @desc
 */
public class MoreLayoutItemBean {

    public MoreLayoutItemBean(String title, int resId) {
        this.title = title;
        this.resId = resId;
    }
    public String title;
    @DrawableRes
    public int resId;
}
