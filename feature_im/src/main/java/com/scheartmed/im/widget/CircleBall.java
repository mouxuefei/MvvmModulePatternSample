package com.scheartmed.im.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleBall extends View {
    Paint paint;

    public CircleBall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        paint.setStyle(Paint.Style.FILL);
        int radius = Math.min(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.drawCircle(radius, radius, radius, paint);
    }

    public void setBallColor(String color) {
        paint.setColor(Color.parseColor(color));
        invalidate();
    }
}