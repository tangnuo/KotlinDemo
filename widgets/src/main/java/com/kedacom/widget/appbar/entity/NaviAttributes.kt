package com.kedacom.widget.appbar.entity

import android.graphics.drawable.Drawable

/**
 * 返回按钮位置，在BarType.WEB情况下调用
 */
data class NaviAttributes(var naviDrawable: Drawable? = null, var showBack: Boolean = true)
