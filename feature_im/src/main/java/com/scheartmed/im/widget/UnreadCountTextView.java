package com.scheartmed.im.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;


import com.core.commonsdk.utils.ScreenUtils;
import com.scheartmed.im.R;

import androidx.appcompat.widget.AppCompatTextView;


public class UnreadCountTextView extends AppCompatTextView {

    private int mNormalSize = ScreenUtils.INSTANCE.dp2px(18.4f);
    private Paint mPaint;

    public UnreadCountTextView(Context context) {
        super(context);
        init();
    }

    public UnreadCountTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnreadCountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.c_read_dot_bg));
        mPaint.setAntiAlias(true);
        setTextColor(Color.WHITE);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.6f);
    }

    public void setPaintColor(int color) {
        if (mPaint != null) {
            mPaint.setColor(color);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getText().length() == 0) {
            // 没有字符，就在本View中心画一个小圆点
            int l = (getMeasuredWidth() - ScreenUtils.INSTANCE.dp2px(6)) / 2;
            int t = l;
            int r = getMeasuredWidth() - l;
            int b = r;
            canvas.drawOval(new RectF(l, t, r, b), mPaint);
        } else if (getText().length() == 1) {
            canvas.drawOval(new RectF(0, 0, mNormalSize, mNormalSize), mPaint);
        } else if (getText().length() > 1) {
            canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), getMeasuredHeight() / 2, getMeasuredHeight() / 2, mPaint);
        }
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mNormalSize;
        int height = mNormalSize;
        if (getText().length() > 1) {
            width = mNormalSize + ScreenUtils.INSTANCE.dp2px((getText().length() - 1) * 10);
        }
        setMeasuredDimension(width, height);
    }
}
