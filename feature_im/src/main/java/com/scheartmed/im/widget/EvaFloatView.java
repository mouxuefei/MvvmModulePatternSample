package com.scheartmed.im.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;


public class EvaFloatView extends FrameLayout {
    private static final String TAG = "ScrollFloatinigButton";
    private float mX;
    private float mY;
    private int mParentWidth;
    private int mParentHeight;
    private boolean mScrollEnable = true;
    private int mScrollTop;
    private int mRight;
    private int mScrollBottom;
    private boolean hasScroll;
    boolean isScroll = false;
    private boolean isAdsorb = true;//是否自动吸附到两边

    public EvaFloatView(Context context) {
        this(context, null);
    }

    public EvaFloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EvaFloatView(Context context, AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //防止布局重置时重置ScrollFloatinigView的位置
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) getParent()).addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (hasScroll && mScrollBottom != 0)
                        layout(getLeft(), mScrollTop, getRight(), mScrollBottom);
                }
            });
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getParent() instanceof ViewGroup) {
            mParentWidth = ((ViewGroup) getParent()).getWidth();
            mParentHeight = ((ViewGroup) getParent()).getHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScrollEnable) return super.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = ev.getX();
                mY = ev.getY();
                super.onTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                float y = ev.getY();
                y = y - mY;
                if ( Math.abs(scaledTouchSlop) < Math.abs(y)) {
                    isScroll = true;
                }
                if (isScroll) {
                    mScrollTop = (int) (getY() + y);
                    mScrollBottom = (int) (getY() + getHeight() + y);
                    if (mScrollTop < 0 || mScrollBottom > mParentHeight) {
                        mScrollTop = (int) getY();
                        mScrollBottom = (int) getY() + getHeight();
                    }
                    layout(getLeft(), mScrollTop, getRight(), mScrollBottom);
                    hasScroll = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isScroll) {
                    isScroll = false;
                    setPressed(false);//重置点击状态
                    if (isAdsorb) {//判断是否开启吸附
                        //获取屏幕中间值
                        int mind = getScreenWidth() / 2;
                        //获取控件宽度的中间值
                        int viewWithMind = getWidth() / 2;
                        layout(getLeft(), mScrollTop, getRight(), mScrollBottom);
                    }
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isAdsorb() {
        return isAdsorb;
    }

    public void setAdsorb(boolean adsorb) {
        isAdsorb = adsorb;
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
