package com.kedacom.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.IntRange
import com.kedacom.util.SystemUtil

/**
 * 水印背景
 *
 * @param context 上下文
 * @param labels 水印文字列表 多行显示支持
 * @param degree 水印角度
 * @param fontSize 水印文字大小，单位sp
 */
class WaterMarkDrawable(private val context: Context,
                        var labels: List<String>,
                        var fontSize: Int = 19,
                        var subFontSize: Int = 16,
                        var backgroundColor: Int = Color.WHITE,
                        var fontColor: Int = Color.parseColor("#50AEAEAE"),
                        var subFontColor: Int = Color.parseColor("#50AEAEAE"),
                        var degree: Int = 45,
                        var useSub: Boolean = true) : Drawable() {

    private val mPaint = Paint()
    private val mPaintSub = Paint()

    override fun draw(canvas: Canvas) {
        canvas.drawColor(backgroundColor)
        // 无绘制内容，只绘制背景色
        if (labels.isEmpty()) return

        val width = bounds.right
        val height = bounds.bottom

        mPaint.color = fontColor
        mPaint.isAntiAlias = true
        mPaint.textSize = SystemUtil.sp2px(fontSize.toFloat()).toFloat()

        mPaintSub.color = subFontColor
        mPaintSub.isAntiAlias = true
        mPaintSub.textSize = SystemUtil.sp2px(subFontSize.toFloat()).toFloat()

        // 开始绘制水印文字
        canvas.save()
        canvas.rotate(-degree.toFloat())
        // 获取文字长度最长的值
        var textWidth = 0F
        labels.forEach {
            textWidth = Math.max(textWidth, mPaint.measureText(it))
        }
        var index = 0
        var positionY = height / 10
        while (positionY <= height) {
            val fromX = -width + index++ % 2 * textWidth
            var positionX = fromX
            while (positionX < width) {
                var spacing = 0//间距
                labels.forEachIndexed { idx, value ->
                    val currPaint = if (idx == 0) mPaint else if (useSub) mPaintSub else mPaint
                    canvas.drawText(value, positionX, (positionY + spacing).toFloat(), currPaint)
                    spacing += SystemUtil.sp2px(fontSize.toFloat())
                }
                positionX += textWidth * 2
            }
            positionY += height / 10 + 80
        }
        canvas.restore()
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}
