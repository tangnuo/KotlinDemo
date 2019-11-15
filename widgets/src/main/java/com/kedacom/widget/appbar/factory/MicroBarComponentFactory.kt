package com.kedacom.widget.appbar.factory

import android.app.Activity
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.annotation.RestrictTo
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.View
import com.kedacom.util.RefUtil
import com.kedacom.widget.R
import com.kedacom.widget.appbar.AppBar
import com.kedacom.widget.appbar.DEFAULT_FONT_COLOR
import com.kedacom.widget.appbar.DEFAULT_FONT_SIZE
import com.kedacom.widget.appbar.DEFAULT_NAVI_ICON
import com.kedacom.widget.appbar.entity.AppBarAttributeResult
import com.kedacom.widget.appbar.entity.MenuAttributes
import com.kedacom.widget.appbar.entity.NaviAttributes
import com.kedacom.widget.appbar.entity.TitleAttributes

/**
 * 创建混合页面，微应用页面的AppBar
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class MicroBarComponentFactory : BaseBarComponentFactory() {

    override fun parseAttributes(appBar: AppBar, ta: TypedArray): AppBarAttributeResult {
        titleAttributes.title = appBar.title ?: ""
        val textSize = ta.getDimensionPixelSize(R.styleable.MicroAppBar_titleTextSize, DEFAULT_FONT_SIZE)
        if (textSize != DEFAULT_FONT_SIZE) {
            titleAttributes.titleSize = textSize.toFloat()
        }
        val textColor = ta.getColor(R.styleable.MicroAppBar_titleTextColor, DEFAULT_FONT_COLOR)
        if (textColor != DEFAULT_FONT_COLOR) {
            titleAttributes.titleColor = textColor
        }

        menuAttributes.closeDrawable = ta.getDrawable(R.styleable.MicroAppBar_close)
        menuAttributes.moreDrawable = ta.getDrawable(R.styleable.MicroAppBar_more)

        naviAttributes.naviDrawable = appBar.navigationIcon
                ?: appBar.context.getDrawable(DEFAULT_NAVI_ICON)
        val flagShowNavi = ta.getInt(R.styleable.MicroAppBar_naviIconShow, 1)
        naviAttributes.showBack = flagShowNavi == 1
        return AppBarAttributeResult(titleAttributes, menuAttributes, naviAttributes)
    }

    override fun createTitle(appBar: AppBar, params: TitleAttributes): View? {
        removeEmbeddedTitle(appBar)
        val textView = AppCompatTextView(appBar.context)
        textView.setSingleLine()
        textView.setLines(1)
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.setTextColor(params.titleColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, params.titleSize)
        textView.gravity = Gravity.CENTER
        textView.background = ColorDrawable(Color.TRANSPARENT)
        textView.includeFontPadding = false
        textView.text = params.title

        val tlp = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
        )
        tlp.gravity = Gravity.CENTER
        appBar.addView(textView, tlp)
        return textView
    }

    override fun createMenu(appBar: AppBar, params: MenuAttributes): Menu? {
        val appBarContext = appBar.context as? Activity ?: return null
        val menu = appBar.menu
        var size = menu.size()
        if (size == 0) {
            appBar.inflateMenu(R.menu.menu_web)
            size = appBar.menu.size()
        }

        for (index in 0 until size) {
            val item = menu.getItem(index)
            when (item.itemId) {
                R.id.menu_close -> { // 关闭菜单
                    params.closeDrawable?.let { item.setIcon(it) }
                    item.setOnMenuItemClickListener {
                        appBarContext.finish()
                        true
                    }
                }

                R.id.menu_more -> {
                    params.moreDrawable?.let { item.setIcon(it) }
                }
            }
        }

        return appBar.menu
    }

    override fun createNavigation(appBar: AppBar, params: NaviAttributes) {
        appBar.navigationIcon ?: if (!params.showBack) return
        if (!params.showBack) {
            appBar.navigationIcon = null
            appBar.setNavigationOnClickListener(null)
            return
        }

        appBar.navigationIcon = params.naviDrawable
                ?: appBar.context.getDrawable(DEFAULT_NAVI_ICON)
//        appBar.setNavigationOnClickListener {
//            if (appBar.context is Activity) {
//                (appBar.context as Activity).onBackPressed()
//            }
//        }
    }

    fun removeEmbeddedTitle(appBar: AppBar) {
        val clazz = appBar.javaClass.superclass.superclass
        Log.d(AppBar.TAG, "MicroBarComponentFactory::removeEmbeddedTitle => clazz=${clazz.name}")

        try {
            val concreteTitleView = RefUtil.getField(clazz, appBar, "mTitleTextView") as View
            appBar.removeView(concreteTitleView)
        } catch (e: Exception) {
            Log.v(AppBar.TAG, "title没有创建，不需要移除")
        }

        try {
            val concreteSubtitleView = RefUtil.getField(clazz, appBar, "mSubtitleTextView")
            appBar.removeView(concreteSubtitleView as View)
        } catch (e: Exception) {
            Log.v(AppBar.TAG, "subtitle没有创建，不需要移除")
        }

    }
}