package com.kedacom.widget

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kedacom.TAG_WIDGET

/**
 * Toast简单显示处理
 */
object SimpleToast {

    /**
     * 显示位置选择后对应Gravity值
     */
    private val position = { pos: Position ->
        val gravity = when (pos) {
            SimpleToast.Position.TOP -> 0x00000031
            SimpleToast.Position.CENTER -> 0x00000011
            SimpleToast.Position.BOTTOM -> 0x00000051
        }
        gravity
    }

    /**
     * 竖直位置偏移值，与平台有关
     */
    private val yOffset = { context: Context, pos: Position ->
        val offsetId = context.resources.getIdentifier("toast_y_offset", "dimen", "android")
        val yOffset = if (offsetId > 0) context.resources.getDimensionPixelOffset(offsetId) else 0

        val realOffset = when (pos) {
            SimpleToast.Position.TOP -> yOffset
            SimpleToast.Position.CENTER -> 0
            SimpleToast.Position.BOTTOM -> yOffset
        }
        realOffset
    }

    /**
     * 显示位置
     */
    enum class Position {
        TOP, CENTER, BOTTOM
    }

    /**
     * 显示Toast
     *
     * @param context 上下文
     * @title 显示内容
     */
    @JvmStatic
    fun show(context: Context, text: CharSequence) {
        show(context, text, Position.BOTTOM)
    }

    /**
     * 显示Toast
     *
     * @param context 上下文
     * @resId 显示资源id
     */
    @JvmStatic
    fun show(context: Context, resId: Int) {
        if (resId == 0) {
            throw IllegalArgumentException("string resource id cannot be 0")
        }
        val text = context.getString(resId)
        show(context, text)
    }

    /**
     * 在指定个位置显示Toast
     *
     * @param context 上下文
     * @param text 显示内容
     * @param position 显示位置 {@link com.kedacom.widget.SimpleToast.Position}
     */
    @JvmStatic
    fun show(context: Context, text: CharSequence, position: Position) {
        internalShow(context, text, position, Toast.LENGTH_LONG)
    }

    /**
     * 短时间显示Toast
     */
    @JvmStatic
    fun flash(context: Context, text: CharSequence) {
        flash(context, text, SimpleToast.Position.BOTTOM)
    }

    /**
     * 显示Toast
     *
     * @param context 上下文
     * @param text 显示内容
     * @param position 显示位置
     */
    @JvmStatic
    fun flash(context: Context, text: CharSequence, position: Position) {
        internalShow(context, text, position, Toast.LENGTH_SHORT)
    }

    /**
     * @param context 上下文
     * @param resId 显示内容的资源id
     */
    @JvmStatic
    fun flash(context: Context, resId: Int) {
        flash(context, resId, SimpleToast.Position.BOTTOM)
    }

    /**
     * @param context 上下文
     * @param resId 显示内容的资源id
     * @param position 显示位置
     */
    @JvmStatic
    fun flash(context: Context, resId: Int, position: Position) {
        if (resId == 0) {
            throw IllegalArgumentException("string resource id cannot be 0")
        }
        val text = context.getString(resId)
        internalShow(context, text, position, Toast.LENGTH_SHORT)
    }

    /**
     * @param context 上下文
     * @param layoutId 自定义View layout id
     */
    @JvmStatic
    fun custom(context: Context, layoutId: Int) {
        custom(context, layoutId, Position.BOTTOM)
    }


    /**
     * @param context 上下文
     * @param layoutId 自定义View layout id
     * @param position 显示位置
     */
    @JvmStatic
    fun custom(context: Context, layoutId: Int, position: Position) {
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT

        toast.setGravity(position(position), 0, yOffset(context, position))
        toast.view = View.inflate(context, layoutId, null)
        toast.show()
    }

    /**
     * 实际显示Toast
     *
     * @param context 上下文
     * @param text  显示内容
     * @param position 显示位置
     * @param duration 时间长度
     */
    private fun internalShow(context: Context, text: CharSequence, position: Position, duration: Int) {
        val toast = Toast.makeText(context, text, duration)
        val yOffset = yOffset(context, position)
        Log.i(TAG_WIDGET, "yOffset: $yOffset")
        toast.setGravity(position(position), 0, yOffset)
        toast.show()
    }
}