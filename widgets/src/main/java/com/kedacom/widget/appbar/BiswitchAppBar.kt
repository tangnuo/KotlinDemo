package com.kedacom.widget.appbar

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.MenuRes
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.FrameLayout
import com.kedacom.util.SystemUtil
import com.kedacom.widget.R
import com.kedacom.widget.appbar.callback.OnMenuMoreClickListener
import com.kedacom.widget.appbar.menu.MicroMenuHelper

/**
 * 风格可切换AppBar
 */
class BiswitchAppBar : FrameLayout {

    private companion object {
        const val TAG = "BiswitchAppBar"
    }

    private lateinit var mAppBar: AppBar
    /**
     * 菜单项
     */
    private var mMenu: Menu? = null
    private var mOnMenuItemClickListener: OnMenuItemClickListener? = null

    private var mOnNavigationClickListener: OnNavigationClickListener? = null

    @get:JvmName("getMode")
    var barMode: BarMode = BarMode.NORMAL
        private set
    var naviDrawable: Drawable? = null
        private set
    var naviIconShow: Boolean = true
        private set
    var title: CharSequence? = " "
        private set
    @ColorInt
    var titleColor: Int? = null
        private set
    var titleSize: Float = DEFAULT_FONT_SIZE.toFloat()
        private set
    var closeDrawable: Drawable? = null
        private set
    var moreDrawable: Drawable? = null
        private set
    @set:JvmName("setMenu")
    @get:JvmName("getMenuId")
    var menuResId: Int = 0
        private set
    var toolbarBackground: Drawable? = null
        private set

    constructor(context: Context) : super(context) {
        naviDrawable = context.getDrawable(DEFAULT_NAVI_ICON)
        closeDrawable = context.getDrawable(DEFAULT_MICRO_CLOSE_ICON)
        moreDrawable = context.getDrawable(DEFAULT_MICRO_MORE_ICON)
        titleColor = DEFAULT_FONT_COLOR
        internalInitAppBar()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        val ta = context.obtainStyledAttributes(attr, R.styleable.BiswitchAppBar)
        val modeVal = ta.getInteger(R.styleable.BiswitchAppBar_mode, 1)
        this.barMode = if (modeVal == 1) BarMode.NORMAL else BarMode.MICRO
        naviDrawable = ta.getDrawable(R.styleable.BiswitchAppBar_naviIcon)
                ?: context.getDrawable(DEFAULT_NAVI_ICON)
        naviIconShow = ta.getInt(R.styleable.BiswitchAppBar_naviIconShow, 1) == 1
        title = ta.getText(R.styleable.BiswitchAppBar_title) ?: ""
        titleColor = ta.getColor(R.styleable.BiswitchAppBar_titleTextColor, DEFAULT_FONT_COLOR)
        titleSize = ta.getDimensionPixelSize(R.styleable.BiswitchAppBar_titleTextSize, DEFAULT_FONT_SIZE).toFloat()
        closeDrawable = ta.getDrawable(R.styleable.BiswitchAppBar_close)
        moreDrawable = ta.getDrawable(R.styleable.BiswitchAppBar_more)
        menuResId = ta.getResourceId(R.styleable.BiswitchAppBar_menu, 0)
        ta.recycle()

        internalInitAppBar()
    }

    private fun internalInitAppBar() {
        if (toolbarBackground == null) {
            toolbarBackground = background ?: ColorDrawable(DEFAULT_BACKGROUND)
        }
        background = null
        mAppBar = if (barMode == BarMode.NORMAL) AppBar(context) else MicroAppBar(context)
        mAppBar.title = title
        mAppBar.setTitleTextColor(titleColor ?: DEFAULT_FONT_COLOR)
        mAppBar.setTitleTextSize(titleSize)
        mAppBar.background = toolbarBackground
        mAppBar.navigationIcon = if (naviIconShow) naviDrawable else null
        mAppBar.setNavigationOnClickListener {
            if (mOnNavigationClickListener == null && context is Activity) {
                (context as Activity).finish()
            }
            mOnNavigationClickListener?.onNavigationClick()
        }
        if (barMode == BarMode.MICRO) {
            (mAppBar as MicroAppBar).setCloseIcon(closeDrawable)
            (mAppBar as MicroAppBar).setMoreIcon(moreDrawable)
        }
        internalHandleMenu()

        addView(mAppBar, LayoutParams(LayoutParams.MATCH_PARENT, SystemUtil.getActionBarHeight(context)))
    }

    fun setTextTitleSize(size: Float) {
        this.titleSize = size
        mAppBar.setTitleTextSize(size)
    }

    /**
     * 设值小程序风格下关闭图标，只在BarMode.MICRO起作用
     */
    fun setCloseIcon(closeDrawable: Drawable?) {
        this.closeDrawable = closeDrawable
        if (barMode == BarMode.NORMAL) {
            return
        }
        (mAppBar as MicroAppBar).setCloseIcon(closeDrawable)
    }

    /**
     * 设值小程序风格下更多图标，只在BarMode.MICRO起作用
     */
    fun setMoreIcon(moreDrawable: Drawable?) {
        this.moreDrawable = moreDrawable
        if (barMode == BarMode.NORMAL) {
            return
        }
        (mAppBar as MicroAppBar).setMoreIcon(moreDrawable)
    }

    fun setMode(mode: BarMode) {
        if (mode == this.barMode) {
            return
        }
        this.barMode = mode
        mAppBar.let {
            removeView(it)
            internalInitAppBar()
        }
    }

    fun inflateMenu(@MenuRes menuId: Int) {
        this.menuResId = menuId
        internalHandleMenu()
    }

    fun setTitleText(value: CharSequence?) {
        title = if (TextUtils.isEmpty(value)) " " else value
        mAppBar.title = title
    }

    fun setTitleTextColor(@ColorInt color: Int?) {
        titleColor = color ?: DEFAULT_FONT_COLOR
        mAppBar.setTitleTextColor(titleColor!!)
    }

    fun setNavigationIcon(drawable: Drawable?) {
        drawable?.also { naviDrawable = it }
        mAppBar.navigationIcon = drawable
    }

    fun setShowNavigationIcon(enable: Boolean) {
        this.naviIconShow = enable
        mAppBar.navigationIcon = if (enable) naviDrawable else null
    }

    fun setOnNavigationClickListener(listener: OnNavigationClickListener?) {
        this.mOnNavigationClickListener = listener
    }

    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener?) {
        this.mOnMenuItemClickListener = listener
    }

    interface OnMenuItemClickListener {
        fun onMenuItemClick(item: MenuItem?)
    }

    interface OnNavigationClickListener {
        fun onNavigationClick()
    }

    /**
     * 处理菜单实例化
     */
    private fun internalHandleMenu() {
        if (barMode == BarMode.NORMAL && menuResId != 0) {
            MenuInflater(context).inflate(menuResId, mAppBar.menu)
            mMenu = mAppBar.menu
            mAppBar.setOnMenuItemClickListener { menuItem ->
                mOnMenuItemClickListener?.onMenuItemClick(menuItem)
                true
            }
        }
        val menuSize = mMenu?.size() ?: 0
        if (barMode == BarMode.MICRO && menuSize > 0) {
            // 在NORMAL模式下创建了菜单
            (mAppBar as MicroAppBar).setOnMenuMoreClickListener(object : OnMenuMoreClickListener {
                override fun onMenuMoreClick(): Boolean {
                    MicroMenuHelper(context).showMenu(mMenu!!, mOnMenuItemClickListener)
                    return true
                }
            })
        }
        if (barMode == BarMode.MICRO && menuResId != 0 && mMenu == null) {
            MenuInflater(context).inflate(menuResId, mAppBar.menu)
            val menu = mAppBar.menu
            val size = menu.size()
            val menuList: ArrayList<MenuItem> = arrayListOf()
            for (index in 2 until size) {
                val item = menu.getItem(index)
                menuList.add(item)
            }
            menuList.forEach {
                menu.removeItem(it.itemId)
            }

            (mAppBar as MicroAppBar).setOnMenuMoreClickListener(object : OnMenuMoreClickListener {
                override fun onMenuMoreClick(): Boolean {
                    MicroMenuHelper(context).showMenu(menuList, mOnMenuItemClickListener)
                    return true
                }
            })
        }
    }
}