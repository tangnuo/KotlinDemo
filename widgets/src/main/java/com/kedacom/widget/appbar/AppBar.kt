package com.kedacom.widget.appbar

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.RestrictTo
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.util.TypedValue
import com.kedacom.util.RefUtil
import com.kedacom.widget.appbar.entity.MenuAttributes
import com.kedacom.widget.appbar.entity.NaviAttributes
import com.kedacom.widget.appbar.entity.TitleAttributes
import com.kedacom.widget.appbar.factory.BarComponentFactory

/**
 * 统一风格的TitleBar
 */
open class AppBar : Toolbar {
    companion object {
        const val TAG: String = "AppBar"
    }
    /**
     * appbar类型
     */
    protected var barMode: BarMode = BarMode.NORMAL
    /**
     * 创建标题栏初始化内相关组件工厂对象
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    internal var mComponentFactory: BarComponentFactory? = null

    protected var mTitleAttributes: TitleAttributes? = null
    protected var mMenuAttributes: MenuAttributes? = null
    protected var mNaviAttributes: NaviAttributes? = null

    @TargetApi(Build.VERSION_CODES.N)
    constructor(context: Context) : super(context) {
        initCommon()
    }

    @TargetApi(Build.VERSION_CODES.N)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initCommon()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun initCommon() {
        elevation = 0F
//        background = background ?: ColorDrawable(DEFAULT_BACKGROUND)
        setContentInsetsRelative(0, 0)

        contentInsetStartWithNavigation = 0
        contentInsetEndWithActions = 0
        setNavigationOnClickListener {
            if (context is Activity) {
                (context as Activity).finish()
            }
        }
    }

    /**
     * 设置文字大小
     */
    open fun setTitleTextSize(size: Float) {
        val toolbarClazz = javaClass.superclass
        val viewTitle = RefUtil.getFieldNoException(toolbarClazz, this, "mTitleTextView")
        (viewTitle as AppCompatTextView).setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

}