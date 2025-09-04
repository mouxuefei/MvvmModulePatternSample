package com.mou.mvvmmodule.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

public class BottomBarTab extends FrameLayout {
    private ImageView mIcon;
    private Context mContext;
    private int mTabPosition = -1;
    private int mGreyIcon;
    private int mColorIcon;
    private TextView mItem;

    // === 新增：未读徽标 ===
    private TextView mBadge;

    private static final String SELECTED_COLOR = "#333333";
    private static final String UNSELECTED_COLOR = "#7C828C";
    private static final String BADGE_RED = "#FF3B30"; // iOS风红；可自行改

    public BottomBarTab(Context context, @DrawableRes int icon, @DrawableRes int colorIcon, String title) {
        this(context, null, icon, colorIcon, title);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int icon, int colorIcon, String title) {
        this(context, attrs, 0, icon, colorIcon, title);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int defStyleAttr, int icon, int colorIcon, String title) {
        super(context, attrs, defStyleAttr);
        mGreyIcon = icon;
        mColorIcon = colorIcon;
        init(context, icon, title);
    }

    private void init(Context context, int icon, String title) {
        mContext = context;
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout itemContainer = new LinearLayout(mContext);
        itemContainer.setOrientation(LinearLayout.VERTICAL);
        itemContainer.setLayoutParams(itemParams);

        mIcon = new ImageView(mContext);
        mIcon.setImageResource(icon);
        LayoutParams iconParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconParams.gravity = Gravity.CENTER_HORIZONTAL;

        iconParams.height = 100;
        iconParams.width = 200;
        itemContainer.addView(mIcon, iconParams);

        LayoutParams textParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_HORIZONTAL;

        mItem = new TextView(context);
        mItem.setText(title);
        mItem.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        mItem.setTypeface(null, Typeface.BOLD);
        mItem.setTextSize(13);
        mItem.setTextColor(Color.parseColor("#999999"));
        mItem.setGravity(Gravity.CENTER_HORIZONTAL);
        itemContainer.addView(mItem, textParams);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(itemContainer, params);

        // ==== 新增：创建右上角徽标 ====
        mBadge = new TextView(context);
        mBadge.setVisibility(GONE);
        mBadge.setTextColor(Color.WHITE);
        mBadge.setTypeface(Typeface.DEFAULT_BOLD);
        mBadge.setTextSize(10);
        mBadge.setGravity(Gravity.CENTER);

        int badgeH = dp2px(18);
        int minW = dp2px(18);
        int hp = dp2px(4); // 水平内边距让两位数/三位数更好看
        mBadge.setMinHeight(badgeH);
        mBadge.setMinWidth(minW);
        mBadge.setPadding(hp, 0, hp, 0);
        mBadge.setSingleLine(true);
        mBadge.setEllipsize(TextUtils.TruncateAt.END);
        mBadge.setBackground(createBadgeBg(badgeH / 2f)); // 圆角 = 高度一半

        LayoutParams badgeLp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, badgeH);
        badgeLp.gravity = Gravity.END | Gravity.TOP;
        badgeLp.setMargins(0, dp2px(4), dp2px(4), 0); // 距离右上角一点间距
        addView(mBadge, badgeLp);
    }

    // 创建红色圆角背景
    private Drawable createBadgeBg(float radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(Color.parseColor(BADGE_RED));
        d.setCornerRadius(radius);
        return d;
    }

    private int dp2px(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
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

    // ==== 新增API：设置未读数 ====

    /**
     * 设置未读数；<=0 隐藏；>99 显示 "99+"
     */
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

    /** 仅展示小红点（不显示数字） */
    public void showDot() {
        mBadge.setVisibility(VISIBLE);
        mBadge.setText(""); // 空文本 -> 小圆点
        // 让小红点更小一些
        ViewGroup.LayoutParams lp = mBadge.getLayoutParams();
        if (lp != null) {
            lp.height = dp2px(10);
            mBadge.setMinWidth(dp2px(10));
            mBadge.setMinHeight(dp2px(10));
            mBadge.setPadding(0, 0, 0, 0);
            mBadge.setBackground(createBadgeBg(dp2px(5)));
            mBadge.setLayoutParams(lp);
        }
    }

    /** 隐藏徽标（数字/小红点都隐藏） */
    public void hideBadge() {
        mBadge.setVisibility(GONE);
    }
}