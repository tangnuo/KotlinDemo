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
 * 原生页面AppBar内组件创建
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class NativeBarComponentFactory : BaseBarComponentFactory() {

    override fun parseAttributes(appBar: AppBar, ta: TypedArray): AppBarAttributeResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createTitle(appBar: AppBar, params: TitleAttributes): View? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createMenu(appBar: AppBar, params: MenuAttributes) : Menu? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createNavigation(appBar: AppBar, params: NaviAttributes) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
