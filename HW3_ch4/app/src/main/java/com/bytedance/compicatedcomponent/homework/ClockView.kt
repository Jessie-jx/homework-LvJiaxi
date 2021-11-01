package com.bytedance.compicatedcomponent.homework

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.beans.PropertyChangeSupport
import java.lang.Exception
import java.util.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 *  author : neo
 *  time   : 2021/10/25
 *  desc   :
 */
class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val FULL_ANGLE = 360

        private const val CUSTOM_ALPHA = 140
        private const val FULL_ALPHA = 255

        private const val POINTER_TYPE_SECOND = 2
        private const val POINTER_TYPE_MINUTES = 1
        private const val POINTER_TYPE_HOURS = 0

        private const val DEFAULT_PRIMARY_COLOR: Int = Color.WHITE
        private const val DEFAULT_SECONDARY_COLOR: Int = Color.LTGRAY

        private const val DEFAULT_DEGREE_STROKE_WIDTH = 0.012f
        private const val THICK_DEGREE_STROKE_WIDTH = 0.020f
        private const val THIN_DEGREE_STROKE_WIDTH = 0.008f

        private const val RIGHT_ANGLE = 90

        private const val UNIT_DEGREE = (6 * Math.PI / 180).toFloat() // 一个小格的度数
    }

    private var panelRadius = 200.0f // 表盘半径

    private var hourPointerLength = 0f // 指针长度

    private var minutePointerLength = 0f
    private var secondPointerLength = 0f

    private var resultWidth = 0
    private var resultHeight = 0
    private var centerX: Int = 0
    private var centerY: Int = 0
    private var radius: Int = 0

    private var degreesColor = 0

    private val needlePaint: Paint
    private var textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textPaint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var bounds: Rect = Rect()


    var calendar: Calendar = Calendar.getInstance()
    var now: Date = calendar.time
    var nowHours: Int = now.hours
    var nowMinutes: Int = now.minutes
    var nowSeconds: Int = now.seconds

    var hourText = nowHours.toString().padStart(2, '0')
    var minText = nowMinutes.toString().padStart(2, '0')
    var secText = nowSeconds.toString().padStart(2, '0')
    var currentTime = "$hourText:$minText:$secText"

    init {
        degreesColor = DEFAULT_PRIMARY_COLOR
        needlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        needlePaint.style = Paint.Style.FILL_AND_STROKE
        needlePaint.strokeCap = Paint.Cap.ROUND

        textPaint.color = Color.WHITE
        textPaint.textSize = 160f
        textPaint2.color = Color.WHITE
        textPaint2.textSize = 60f



        // todo 1: 每一秒刷新一次，让指针动起来
        Thread {
            while (true) {
                Thread.sleep(1000)
                calendar = Calendar.getInstance()
                now = calendar.time
                nowHours = now.hours
                nowMinutes = now.minutes
                nowSeconds = now.seconds
                hourText = nowHours.toString().padStart(2, '0')
                minText = nowMinutes.toString().padStart(2, '0')
                secText = nowSeconds.toString().padStart(2, '0')
                currentTime = "$hourText:$minText:$secText"

                invalidate()
            }
        }.start()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // EXACTLY 984 1968
        val size: Int
        val width = measuredWidth
        val height = measuredHeight

        setMeasuredDimension(width, height)

//        val widthWithoutPadding = width - paddingLeft - paddingRight
//        val heightWithoutPadding = height - paddingTop - paddingBottom
//        size = if (widthWithoutPadding > heightWithoutPadding) {
//            heightWithoutPadding
//        } else {
//            widthWithoutPadding
//        }
//        setMeasuredDimension(size + paddingLeft + paddingRight, size + paddingTop + paddingBottom)


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        resultWidth = if (height > width) width else height
//        val halfWidth = resultWidth / 2
//        centerX = halfWidth
//        centerY = halfWidth
//        radius = halfWidth
        resultWidth = width
        resultHeight = height
        val halfWidth = resultWidth / 2
        val halfHeight = resultHeight / 2
        centerX = halfWidth
        centerY = (halfHeight * 1.1f).roundToInt()
        radius = halfWidth

        panelRadius = radius.toFloat()
//        hourPointerLength = panelRadius - 400
//        minutePointerLength = panelRadius - 250
//        secondPointerLength = panelRadius - 150
        hourPointerLength = panelRadius * 0.5f
        minutePointerLength = panelRadius * 0.75f
        secondPointerLength = panelRadius
        drawDegrees(canvas)
        drawHoursValues(canvas)
        drawNeedles(canvas)
        drawDigitalClock(canvas)

    }

    private fun drawDegrees(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = resultWidth * DEFAULT_DEGREE_STROKE_WIDTH
            color = degreesColor
        }
        val rPadded: Int = radius - (resultWidth * 0.01f).toInt()
        val rEnd: Int = radius - (resultWidth * 0.05f).toInt()
        var i = 0

        while (i < FULL_ANGLE) {
            if (i % RIGHT_ANGLE != 0 && i % 15 != 0) {
                paint.alpha = CUSTOM_ALPHA
            } else {
                paint.alpha = FULL_ALPHA
            }
            val startX = (centerX + rPadded * cos(Math.toRadians(i.toDouble())))
            val stopX = (centerX + rEnd * cos(Math.toRadians(i.toDouble())))
            val startY = (centerY - rPadded * sin(Math.toRadians(i.toDouble())))
            val stopY = (centerY - rEnd * sin(Math.toRadians(i.toDouble())))
            canvas.drawLine(
                startX.toFloat(),
                startY.toFloat(),
                stopX.toFloat(),
                stopY.toFloat(),
                paint
            )
            i += 6
        }
    }

    /**
     * Draw Digital Clock
     *
     * @param canvas
     */

    private fun getContentSize(paint: Paint, str: String): Pair<Int, Int> {

        paint.getTextBounds(str, 0, str.length, bounds)
        val textWith = bounds.width()
        val textHeight = bounds.height()
        return textWith to textHeight

    }

    private fun drawDigitalClock(canvas: Canvas) {
        // todo 4: 电子时钟
        val (textWidth, textHeight) = getContentSize(textPaint, currentTime)
        canvas.drawText(
            currentTime,
            width / 2f - textWidth / 2f,
            height / 6f + textHeight / 2f,
            textPaint
        )

    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */


    private fun drawHoursValues(canvas: Canvas) {
        // Default Color:
        // - hoursValuesColor
        // todo 3: 标小时数字
        val (numWidth, numHeight) = getContentSize(textPaint2, "12")
        canvas.drawText(
            "12",
            centerX.toFloat() - numWidth / 2,
            (centerY - radius + numHeight * 3).toFloat(),
            textPaint2
        )
        val (numWidth2, numHeight2) = getContentSize(textPaint2, "6")
        canvas.drawText(
            "6",
            centerX.toFloat() - numWidth2 / 2,
            (centerY + radius - numHeight * 2).toFloat(),
            textPaint2
        )

        canvas.drawText(
            "3",
            (centerX + radius*0.75).toFloat(),
            centerY + numHeight / 2f,
            textPaint2
        )

        canvas.drawText(
            "9",
            (centerX - radius*0.85).toFloat(),
            centerY + numHeight / 2f,
            textPaint2
        )

    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private fun drawNeedles(canvas: Canvas) {

        // 画分针
        // todo 2: 画分针
        val minPart = nowSeconds / 60
        drawPointer(canvas, POINTER_TYPE_MINUTES, nowMinutes + minPart)
        // 画时针
        val hourPart = nowMinutes / 12
        drawPointer(canvas, POINTER_TYPE_HOURS, 5 * nowHours + hourPart)
        // 画秒针
        drawPointer(canvas, POINTER_TYPE_SECOND, nowSeconds)
    }


    private fun drawPointer(canvas: Canvas, pointerType: Int, value: Int) {
        val degree: Float
        var pointerHeadXY = FloatArray(2)

        when (pointerType) {
            POINTER_TYPE_HOURS -> {
                degree = value * UNIT_DEGREE
                needlePaint.color = Color.WHITE
                pointerHeadXY = getPointerHeadXY(hourPointerLength, degree)
                needlePaint.strokeWidth = resultWidth * THICK_DEGREE_STROKE_WIDTH
            }
            POINTER_TYPE_MINUTES -> {
                degree = value * UNIT_DEGREE
                needlePaint.color = Color.GRAY
                pointerHeadXY = getPointerHeadXY(minutePointerLength, degree)
                needlePaint.strokeWidth = resultWidth * DEFAULT_DEGREE_STROKE_WIDTH

            }
            POINTER_TYPE_SECOND -> {
                degree = value * UNIT_DEGREE
                needlePaint.color = Color.parseColor("#00A9D3")
                pointerHeadXY = getPointerHeadXY(secondPointerLength, degree)
                needlePaint.strokeWidth = resultWidth * THIN_DEGREE_STROKE_WIDTH
            }
        }
        canvas.drawLine(
            centerX.toFloat(), centerY.toFloat(),
            pointerHeadXY[0], pointerHeadXY[1], needlePaint
        )
    }

    private fun getPointerHeadXY(pointerLength: Float, degree: Float): FloatArray {
        val xy = FloatArray(2)
        xy[0] = centerX + pointerLength * sin(degree)
        xy[1] = centerY - pointerLength * cos(degree)
        return xy
    }
}