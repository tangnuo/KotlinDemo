package com.kedacom.widget.appbar.factory

import android.support.annotation.RestrictTo
import com.kedacom.widget.appbar.entity.MenuAttributes
import com.kedacom.widget.appbar.entity.NaviAttributes
import com.kedacom.widget.appbar.entity.TitleAttributes

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal abstract class BaseBarComponentFactory : BarComponentFactory {
    protected var titleAttributes = TitleAttributes()

    protected var menuAttributes = MenuAttributes()

    protected var naviAttributes = NaviAttributes()
}