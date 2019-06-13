package com.fortunes.commonsdk.view.toolbar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.fortunes.commonsdk.R

/**
 * 通用的标题栏ToolBar
 */

class MyToolBarLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var backIcon: Int = R.mipmap.public_back_icon
    private var toolTitle: String? = null
    private var toolRight: String? = null
    private var iconVisible: Boolean = true

    private var titleTv: TextView
    private var ivBack: ImageView
    private var rlBack: RelativeLayout
    private var rlRight: RelativeLayout
    private var tvRight: TextView

    init {
        @SuppressLint("CustomViewStyleable")
        val array = context.obtainStyledAttributes(attrs, R.styleable.public_toolbar)
        backIcon = array.getResourceId(R.styleable.public_toolbar_public_toolbar_res, R.mipmap.public_back_icon)
        toolTitle = array.getString(R.styleable.public_toolbar_public_toolbar_title)
        toolRight = array.getString(R.styleable.public_toolbar_public_toolbar_tv_right)
        iconVisible = array.getBoolean(R.styleable.public_toolbar_public_toolbar_img, true)
        array.recycle()
        val view = LayoutInflater.from(context).inflate(R.layout.public_layout_toolbar, this, false)
        addView(view, 0)
        ivBack = view.findViewById(R.id.public_iv_back)
        rlBack = view.findViewById(R.id.public_rl_back)
        rlRight = view.findViewById(R.id.public_rl_right)
        tvRight = view.findViewById(R.id.public_tv_right)
        ivBack.setImageResource(backIcon)
        ivBack.visibility = if (iconVisible) View.VISIBLE else View.GONE
        rlBack.visibility = if (iconVisible) View.VISIBLE else View.GONE
        titleTv = view.findViewById(R.id.public_toolbar_title)
        titleTv.text = toolTitle
        tvRight.text = toolRight

    }

    fun setOnRightClickListner(click: (() -> Unit)? = null) {
        rlRight.setOnClickListener {
            click?.invoke()
        }
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String) {
        titleTv.text = title
    }

    fun setTvRightVisible(visibility: Boolean) {
        if (visibility) rlRight.visibility = View.VISIBLE else rlRight.visibility = View.GONE
    }


    /**
     * 设置左边图片
     */
    fun leftIconRes(res: Int) {
        ivBack.setImageResource(res)
    }

}