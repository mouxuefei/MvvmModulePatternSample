package com.scheartmed.im.widget.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.scheartmed.im.R;

import java.util.ArrayList;

public class IndicatorView extends LinearLayout {

    private Context mContext;
    private ArrayList<ImageView> mImageViews;
    private Bitmap mBtnSelect;
    private Bitmap mBtnNormal;
    private int mMaxHeight;
    private int mMaxWidth;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setOrientation(HORIZONTAL);
        mMaxWidth = mMaxHeight = dip2px(16);
        mBtnSelect = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_point_select);
        mBtnNormal = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_point_nomal);
    }

    public void init(int count) {

        mImageViews = new ArrayList<>();
        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            RelativeLayout rl = new RelativeLayout(mContext);
            LayoutParams params = new LayoutParams(mMaxWidth, mMaxHeight);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            ImageView imageView = new ImageView(mContext);
            if (i == 0) {
                imageView.setImageBitmap(mBtnSelect);
                rl.addView(imageView, layoutParams);
            } else {
                imageView.setImageBitmap(mBtnNormal);
                rl.addView(imageView, layoutParams);
            }
            this.addView(rl, params);
            mImageViews.add(imageView);
        }
    }

    public void setIndicatorCount(int count) {
        if (mImageViews == null || count > mImageViews.size()) {
            return;
        }
        for (int i = 0; i < mImageViews.size(); i++) {
            if (i >= count) {
                mImageViews.get(i).setVisibility(GONE);
                ((View) mImageViews.get(i).getParent()).setVisibility(GONE);
            } else {
                mImageViews.get(i).setVisibility(VISIBLE);
                ((View) mImageViews.get(i).getParent()).setVisibility(VISIBLE);
            }
        }
    }



    public void playBy(int startPosition, int nextPosition) {

        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }

        if (startPosition < 0) {
            startPosition = nextPosition = 0;
        }

        final ImageView imageViewStrat = mImageViews.get(startPosition);
        final ImageView imageViewNext = mImageViews.get(nextPosition);
        imageViewStrat.setImageBitmap(mBtnNormal);
        imageViewNext.setImageBitmap(mBtnSelect);

    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
