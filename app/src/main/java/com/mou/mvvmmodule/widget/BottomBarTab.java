package com.mou.mvvmmodule.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.DrawableRes;


/**
 * Created by YoKeyword on 16/6/3.
 */
public class BottomBarTab extends FrameLayout
{
    private ImageView mIcon;
    private Context mContext;
    private int mTabPosition = -1;
    private int mGreyIcon;
    private int mColorIcon;
    private TextView mItem;

    private static final String SELECTED_COLOR = "#333333";
    private static final String UNSELECTED_COLOR = "#7C828C";

    public BottomBarTab(Context context, @DrawableRes int icon, @DrawableRes int colorIcon, String title)
    {
        this(context, null, icon, colorIcon, title);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int icon, int colorIcon, String title)
    {
        this(context, attrs, 0, icon, colorIcon, title);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int defStyleAttr, int icon, int colorIcon, String title)
    {
        super(context, attrs, defStyleAttr);
        mGreyIcon = icon;
        mColorIcon = colorIcon;
        init(context, icon, title);
    }

    private void init(Context context, int icon, String title)
    {
        mContext = context;
//        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless});
//        Drawable drawable = typedArray.getDrawable(0);
////        setBackgroundDrawable(drawable);
//        typedArray.recycle();

        /*mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
        LayoutParams params = new LayoutParams(size, size);
        params.gravity = Gravity.CENTER;
        mIcon.setImageResource(icon);
        mIcon.setLayoutParams(params);*/
        //mIcon.setColorFilter(ContextCompat.getColor(context, R.color.tab_unselect));
        //addView(mIcon);

        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //itemParams.gravity = Gravity.CENTER;

        LinearLayout itemContainer = new LinearLayout(mContext);
        itemContainer.setOrientation(LinearLayout.VERTICAL);
        itemContainer.setLayoutParams(itemParams);

        mIcon = new ImageView(mContext);
        mIcon.setImageResource(icon);
//        mIcon.setBackgroundColor(Color.RED);
        LayoutParams iconParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconParams.gravity=Gravity.CENTER_HORIZONTAL;

        iconParams.height=100;
        iconParams.width=200;
        itemContainer.addView(mIcon, iconParams);

        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity=Gravity.CENTER_HORIZONTAL;
        //textParams.leftMargin=12;

        mItem = new TextView(context);

        mItem.setText(title);
        mItem.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        mItem.setTypeface(null, Typeface.BOLD);
        mItem.setTextSize(13);
        mItem.setTextColor(Color.parseColor("#999999"));
        mItem.setGravity(Gravity.CENTER_HORIZONTAL);

        itemContainer.addView(mItem, textParams );

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(itemContainer, params);
    }

    @Override
    public void setSelected(boolean selected)
    {
        super.setSelected(selected);
        if ( selected )
        {
            mIcon.setImageResource(mColorIcon);
            mItem.setTextColor(Color.parseColor(SELECTED_COLOR));
        }
        else
        {
            mIcon.setImageResource(mGreyIcon);
            mItem.setTextColor(Color.parseColor(UNSELECTED_COLOR));
        }
    }

    public void setTabPosition(int position)
    {
        mTabPosition = position;

        if (position == 0)
        {
            setSelected(true);
        }
        else
        {
            setSelected(false);
        }
    }

    public int getTabPosition()
    {
        return mTabPosition;
    }
}
