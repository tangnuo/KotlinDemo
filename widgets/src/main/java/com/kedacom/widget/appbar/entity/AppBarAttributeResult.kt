package com.kedacom.widget.appbar.entity

import android.support.annotation.RestrictTo


/**
 * 菜单项
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class AppBarAttributeResult(val titleAttributes: TitleAttributes?, val menuAttributes: MenuAttributes?, val naviAttributes: NaviAttributes?) {
    operator fun component1(): TitleAttributes? {
        return titleAttributes
    }

    operator fun component2(): MenuAttributes? {
        return menuAttributes
    }

    operator fun component3(): NaviAttributes? {
        return naviAttributes
    }
}
