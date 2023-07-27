package com.unixtrong.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable

/**
 * 一个圆形的滑块
 */
class ThumbDrawable : Drawable() {

    private val lineWidth = 10F

    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }

    private val foregroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = lineWidth
        color = Color.parseColor("#D1D0D6")
    }

    override fun draw(canvas: Canvas) {
        // 获取圆心坐标
        val cx = bounds.exactCenterX()
        val cy = bounds.exactCenterY()

        // 获取圆的半径（取宽高中的较小值，保证圆能够完整绘制）
        val radius = bounds.width().coerceAtMost(bounds.height()) / 2f

        // 绘制圆形
        canvas.drawCircle(cx, cy, radius, backgroundPaint)

        // 绘制三条居中横线
        canvas.drawLine(
            cx - radius / 2F,
            cy,
            cx + radius / 2F,
            cy,
            foregroundPaint
        )
        canvas.drawLine(
            cx - radius / 2F,
            cy - lineWidth * 3,
            cx + radius / 2F,
            cy - lineWidth * 3,
            foregroundPaint
        )
        canvas.drawLine(
            cx - radius / 2F,
            cy + lineWidth * 3,
            cx + radius / 2F,
            cy + lineWidth * 3,
            foregroundPaint
        )
    }

    override fun setAlpha(alpha: Int) {
        // 不需要实现
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // 不需要实现
    }

    @Deprecated("Deprecated in Java", ReplaceWith("android.graphics.PixelFormat.OPAQUE", "android"))
    override fun getOpacity(): Int {
        // 当 Drawable 是一个纯色时，建议返回 PixelFormat.OPAQUE
        return android.graphics.PixelFormat.OPAQUE
    }
}