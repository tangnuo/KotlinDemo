package com.kedacom.widget.appbar.entity

import android.support.annotation.ColorInt
import com.kedacom.widget.appbar.DEFAULT_FONT_COLOR
import com.kedacom.widget.appbar.DEFAULT_FONT_SIZE

/**
 * 标题属性，在BarType.WEB情况下调用
 */
data class TitleAttributes(
        var title: CharSequence = "",
        var titleSize: Float = DEFAULT_FONT_SIZE.toFloat(),
        @ColorInt var titleColor: Int = DEFAULT_FONT_COLOR
)