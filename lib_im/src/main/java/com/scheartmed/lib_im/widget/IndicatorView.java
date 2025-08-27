package com.scheartmed.lib_im.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.scheartmed.lib_im.R;


public class IndicatorView extends LinearLayout {
    private int indicatorColor = Color.rgb(0, 0, 0);
    private int indicatorColorSelected = Color.rgb(0, 0, 0);
    private int indicatorNormalWidth = 0;
    private int indicatorSelectedWidth = 0;
    private int indicatorHeight = 0;
    Paint paint = new Paint();
    private int indicatorCount = 0;
    private int currentIndicator = 0;


    public IndicatorView(Context context) {
        this(context, null, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IndicatorView);
            indicatorColor = typedArray.getColor(R.styleable.IndicatorView_indicatorColor, Color.rgb(0, 0, 0));
            indicatorColorSelected = typedArray.getColor(R.styleable.IndicatorView_indicatorColorSelected, Color.rgb(0, 0, 0));
            indicatorNormalWidth = dip2Px(typedArray.getInt(R.styleable.IndicatorView_indicatorNormalWidth, 0));
            indicatorSelectedWidth = dip2Px(typedArray.getInt(R.styleable.IndicatorView_indicatorSelectedWidth, 0));
            indicatorHeight = dip2Px(typedArray.getInt(R.styleable.IndicatorView_indicatorHeight, 0));
            typedArray.recycle();
        }
        paint.setAntiAlias(true);
        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int totalWidth = (indicatorSelectedWidth + dip2Px(4)) + (indicatorNormalWidth + dip2Px(4)) * (indicatorCount - 1);
        if (indicatorCount > 0) {
            for (int i = 0; i < indicatorCount; i++) {
                int left;
                int right;
                if (i == currentIndicator) {
                    left = (viewWidth - totalWidth) / 2 + (i * (indicatorNormalWidth + dip2Px(4)));
                    right = left + indicatorSelectedWidth;
                    paint.setColor(indicatorColorSelected);
                } else {
                    if (i < currentIndicator) {
                        left = (viewWidth - totalWidth) / 2 + (i * (indicatorNormalWidth + dip2Px(4)));
                    } else {
                        left = (viewWidth - totalWidth) / 2 + ((i - 1) * (indicatorNormalWidth + dip2Px(4)) + indicatorSelectedWidth + dip2Px(4));

                    }
                    right = left + indicatorNormalWidth;
                    paint.setColor(indicatorColor);
                }
                int top = (viewHeight - indicatorHeight) / 2;
                int bottom = top + indicatorHeight;
                @SuppressLint("DrawAllocation") RectF rectF = new RectF(left, top, right, bottom);
                canvas.drawRoundRect(rectF, dip2Px(2), dip2Px(2), paint);
            }
        }
    }

    public void setIndicatorCount(int indicatorCount) {
        this.indicatorCount = indicatorCount;
    }

    public void setCurrentIndicator(int currentIndicator) {
        this.currentIndicator = currentIndicator;
        invalidate();
    }


    public int dip2Px(int dip) {
        float density = getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }
}
