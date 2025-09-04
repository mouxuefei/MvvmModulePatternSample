package com.mou.mvvmmodule.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mou.mvvmmodule.R;

import androidx.annotation.DrawableRes;

public class BottomBarTab extends LinearLayout {
    private ImageView mIcon;
    private TextView mItem;
    private TextView mBadge;

    private int mTabPosition = -1;
    private int mGreyIcon;
    private int mColorIcon;

    private static final String SELECTED_COLOR = "#333333";
    private static final String UNSELECTED_COLOR = "#7C828C";

    public BottomBarTab(Context context, @DrawableRes int icon, @DrawableRes int colorIcon, String title) {
        super(context);
        init(context, icon, colorIcon, title);
    }

    public BottomBarTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context, int icon, int colorIcon, String title) {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_bar_tab, this, true);

        mIcon = findViewById(R.id.icon);
        mItem = findViewById(R.id.title);
        mBadge = findViewById(R.id.badge);

        mGreyIcon = icon;
        mColorIcon = colorIcon;

        mIcon.setImageResource(icon);
        mItem.setText(title);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            mIcon.setImageResource(mColorIcon);
            mItem.setTextColor(Color.parseColor(SELECTED_COLOR));
        } else {
            mIcon.setImageResource(mGreyIcon);
            mItem.setTextColor(Color.parseColor(UNSELECTED_COLOR));
        }
    }

    public void setTabPosition(int position) {
        mTabPosition = position;
        setSelected(position == 0);
    }

    public int getTabPosition() {
        return mTabPosition;
    }

    /** 设置未读数 */
    public void setUnreadCount(int count) {
        if (count <= 0) {
            mBadge.setVisibility(GONE);
        } else {
            mBadge.setVisibility(VISIBLE);
            if (count > 99) {
                mBadge.setText("99+");
            } else {
                mBadge.setText(String.valueOf(count));
            }
        }
    }

    /** 显示小红点 */
    public void showDot() {
        mBadge.setVisibility(VISIBLE);
    }

    public void hideBadge() {
        mBadge.setVisibility(GONE);
    }

}