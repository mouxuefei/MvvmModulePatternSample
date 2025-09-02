package com.scheartmed.im.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class CustomIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var count = 0               // 总点数
    private var currentPosition = 0     // 当前选中点
    private var dotRadius = 10f         // 点半径
    private var dotGap = 20f            // 点间距

    @ColorInt
    private var selectedColor = 0xFFFFFFFF.toInt()
    @ColorInt
    private var normalColor = 0x80FFFFFF.toInt()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun setCount(count: Int) {
        this.count = count
        requestLayout()
        invalidate()
    }

    fun setCurrentPosition(position: Int) {
        this.currentPosition = position.coerceIn(0, count - 1)
        invalidate()
    }

    fun setDotRadius(radius: Float) {
        this.dotRadius = radius
        requestLayout()
        invalidate()
    }

    fun setDotGap(gap: Float) {
        this.dotGap = gap
        requestLayout()
        invalidate()
    }

    fun setSelectedColor(@ColorInt color: Int) {
        this.selectedColor = color
        invalidate()
    }

    fun setNormalColor(@ColorInt color: Int) {
        this.normalColor = color
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = ((dotRadius * 2) * count + dotGap * (count - 1)).toInt() + paddingLeft + paddingRight
        val height = (dotRadius * 2).toInt() + paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (count <= 0) return
        canvas ?: return

        val startX = paddingLeft + dotRadius
        val centerY = paddingTop + dotRadius

        for (i in 0 until count) {
            paint.color = if (i == currentPosition) selectedColor else normalColor
            val cx = startX + i * (2 * dotRadius + dotGap)
            canvas.drawCircle(cx, centerY, dotRadius, paint)
        }
    }
}