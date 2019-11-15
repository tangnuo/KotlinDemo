package com.kedacom.widget.appbar.factory

import android.content.res.TypedArray
import android.support.annotation.RestrictTo
import android.view.Menu
import android.view.View
import com.kedacom.widget.appbar.AppBar
import com.kedacom.widget.appbar.entity.AppBarAttributeResult
import com.kedacom.widget.appbar.entity.MenuAttributes
import com.kedacom.widget.appbar.entity.NaviAttributes
import com.kedacom.widget.appbar.entity.TitleAttributes

/**
 * 创建AppBar内对应组件
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal interface BarComponentFactory {

    fun parseAttributes(appBar: AppBar, ta: TypedArray): AppBarAttributeResult

    fun createMenu(appBar: AppBar, params: MenuAttributes): Menu?

    fun createTitle(appBar: AppBar, params: TitleAttributes): View?

    fun createNavigation(appBar: AppBar, params: NaviAttributes)
}