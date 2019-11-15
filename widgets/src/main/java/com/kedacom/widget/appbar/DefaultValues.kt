@file:JvmName("DefaultValues")

package com.kedacom.widget.appbar

import android.graphics.Color
import android.support.annotation.RestrictTo
import com.kedacom.util.SystemUtil
import com.kedacom.widget.R

/**
 * 默认背景色
 */
val DEFAULT_BACKGROUND = Color.parseColor("#EDEDED")

/**
 * 默认字体色值
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
val DEFAULT_FONT_COLOR = Color.parseColor("#000000")

/**
 * 默认字体大小
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
val DEFAULT_FONT_SIZE = SystemUtil.sp2px(18.toFloat())

/**
 * 默认的返回导航按钮
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
val DEFAULT_NAVI_ICON = R.drawable.ic_back

/**
 * 小程序风格关闭图标
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
val DEFAULT_MICRO_CLOSE_ICON = R.drawable.ic_web_close

/**
 * 小程序风格更多图标
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
val DEFAULT_MICRO_MORE_ICON = R.drawable.ic_web_more


