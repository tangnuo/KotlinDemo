package com.kedacom.widget.appbar.entity

import android.graphics.drawable.Drawable

/**
 * 菜单项修改使用，在BarType.WEB情况下调用
 */
data class MenuAttributes(var moreDrawable: Drawable? = null, var closeDrawable: Drawable? = null)