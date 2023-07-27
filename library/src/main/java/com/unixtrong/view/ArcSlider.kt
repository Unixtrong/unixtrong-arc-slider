package com.unixtrong.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.util.Log
import android.view.View


class ArcSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint().apply {
        this.style = Paint.Style.FILL_AND_STROKE
        this.color = Color.BLUE //颜色
    }

    private val barPath = Path()

    private var width: Float = 0F

    private var height: Float = 0F

    private val thumbRadius = 100F

    private val startCircleRectF = RectF()

    private val endCircleRectF = RectF()

    private val innerCircleRectF = RectF()

    private val outerCircleRectF = RectF()

    private val barStartColor = Color.parseColor("#BA39EC")

    private val barEndColor = Color.parseColor("#5FBDFD")

    private val thumbDrawable: ThumbDrawable = ThumbDrawable()

    private val thumbRect = Rect()

    private val slidePathMeasure: PathMeasure = PathMeasure()

    private val tempPos: FloatArray = FloatArray(2)

    private val tempTan: FloatArray = FloatArray(2)

    private var thumbX = 0F

    private var thumbY = 0F

    private val slidePath = Path()

    var progress: Float = 0.5F
        set(value) {
            field = value
            computeThumbPos(value)
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pd("onSizeChanged, w: $w, h: $h")
        width = w.toFloat()
        height = h.toFloat()
        startCircleRectF.set(
            0F,
            height - thumbRadius * 2,
            thumbRadius * 2,
            height
        )
        endCircleRectF.set(
            width - thumbRadius * 2,
            0F,
            width,
            thumbRadius * 2
        )
        innerCircleRectF.set(
            thumbRadius * 2,
            thumbRadius * 2,
            width * 2 - thumbRadius * 4,
            height * 2 - thumbRadius * 4
        )
        outerCircleRectF.set(
            0F,
            0F,
            width * 2F - thumbRadius * 2F,
            height * 2F - thumbRadius * 2F
        )
        barPaint.shader = SweepGradient(
            width,
            height,
            intArrayOf(barStartColor, barStartColor, barEndColor, barEndColor),
            floatArrayOf(0F, 0.5F, 0.75F, 1F)
        )
        slidePath.reset()
        slidePath.addArc(
            outerCircleRectF.left + thumbRadius,
            outerCircleRectF.top + thumbRadius,
            outerCircleRectF.right - thumbRadius,
            outerCircleRectF.bottom - thumbRadius,
            180F, 90F
        )
        slidePathMeasure.setPath(slidePath, false)
        computeThumbPos(progress)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        width.takeIf { it != 0F } ?: return
        height.takeIf { it != 0F } ?: return
        canvas.drawColor(Color.YELLOW)
        drawSlideBar(canvas)
        drawThumb(canvas)
    }

    private fun drawSlideBar(canvas: Canvas) {
        barPath.moveTo(0F, height - thumbRadius)
        barPath.arcTo(startCircleRectF, 180F, -180F)
        barPath.arcTo(innerCircleRectF, 180F, 90F)
        barPath.arcTo(endCircleRectF, 90F, -180F)
        barPath.arcTo(outerCircleRectF, -90F, -90F)
        barPath.close()
        canvas.drawPath(barPath, barPaint)
    }

    private fun drawThumb(canvas: Canvas) {
        thumbRect.set(
            (thumbX - thumbRadius).toInt(),
            (thumbY - thumbRadius).toInt(),
            (thumbX + thumbRadius).toInt(),
            (thumbY + thumbRadius).toInt()
        )
        pd("drawThumb, rect: $thumbRect")

        // 设置 ThumbDrawable 的边界
        thumbDrawable.bounds = thumbRect

        // 绘制以 ThumbDrawable
        canvas.save()
        canvas.rotate(90F * progress, thumbRect.exactCenterX(), thumbRect.exactCenterY())
        thumbDrawable.draw(canvas)
        canvas.restore()
    }

    /**
     * 计算拖动块应该显示的位置
     */
    private fun computeThumbPos(progress: Float) {
        var present = progress
        if (present < 0) present = 0f
        if (present > 1) present = 1f
        val distance: Float = slidePathMeasure.length * present
        slidePathMeasure.getPosTan(distance, tempPos, tempTan)
        thumbX = tempPos[0]
        thumbY = tempPos[1]
    }


    private fun pd(msg: String) {
        Log.d("ArcSlider", msg)
    }
}