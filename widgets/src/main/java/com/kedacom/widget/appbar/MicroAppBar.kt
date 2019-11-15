package com.kedacom.widget.appbar

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Menu
import android.view.View
import com.kedacom.widget.R
import com.kedacom.widget.appbar.callback.OnMenuMoreClickListener
import com.kedacom.widget.appbar.entity.MenuAttributes
import com.kedacom.widget.appbar.entity.NaviAttributes
import com.kedacom.widget.appbar.entity.TitleAttributes
import com.kedacom.widget.appbar.factory.SimpleComponentFactory


/**
 * 小程序风格AppBar
 */
class MicroAppBar : AppBar {
    private companion object {
        const val TAG = "MicroAppBar"
    }

    /**
     * 菜单View
     */
    private var mViewMenu: Menu? = null
    /**
     * 标题栏
     */
    private var mViewTitle: View? = null
    /**
     * 是否显示返回按钮
     */
    private var mIsShowBackEnabled: Boolean = true

    @TargetApi(Build.VERSION_CODES.N)
    constructor(context: Context) : super(context) {
        barMode = BarMode.MICRO

        setTitleAttributes(null)
        setMenuAttributes(null)
        setNaviAttributes(null)
    }

    @TargetApi(Build.VERSION_CODES.N)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        barMode = BarMode.MICRO
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MicroAppBar)
        mComponentFactory = SimpleComponentFactory.createComponentFactory(barMode)
        val (titleAttributes, menuAttributes, naviAttributes) = mComponentFactory!!.parseAttributes(this, ta)
        ta.recycle()

        setTitleAttributes(titleAttributes)
        setMenuAttributes(menuAttributes)
        setNaviAttributes(naviAttributes)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle("")
        // 规避在超累Toolbar中解析到title时调用此方法时，MicroAppBar构造还未调用
        // barType还未赋值
        if (barMode != BarMode.MICRO) return

        mComponentFactory
                ?: SimpleComponentFactory.createComponentFactory(barMode).also { mComponentFactory = it }
        mViewTitle ?: mComponentFactory!!
                .createTitle(this, mTitleAttributes ?: TitleAttributes()).also { mViewTitle = it }
        (mViewTitle as AppCompatTextView).text = title ?: ""
    }

    /**
     * 设置标题大小，单位px
     */
    override fun setTitleTextSize(size: Float) {
        (mViewTitle as AppCompatTextView).setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    override fun setTitleTextColor(color: Int) {
        super.setTitleTextColor(color)
        if (barMode != BarMode.MICRO) return
        (mViewTitle as AppCompatTextView).setTextColor(color)
    }

    /**
     * 设置标题属性
     */
    fun setTitleAttributes(attr: TitleAttributes?) {
        mTitleAttributes ?: TitleAttributes().also { mTitleAttributes = it }
        mTitleAttributes!!.title = attr?.title ?: ""
        mTitleAttributes!!.titleSize = attr?.titleSize ?: DEFAULT_FONT_SIZE.toFloat()
        mTitleAttributes!!.titleColor = attr?.titleColor ?: DEFAULT_FONT_COLOR

        mComponentFactory
                ?: SimpleComponentFactory.createComponentFactory(barMode).also { mComponentFactory = it }
        mViewTitle ?: mComponentFactory!!.createTitle(this, mTitleAttributes
                ?: TitleAttributes()).also { mViewTitle = it }
        (mViewTitle as AppCompatTextView).setTextColor(mTitleAttributes!!.titleColor)
        (mViewTitle as AppCompatTextView)
                .setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleAttributes!!.titleSize)
        (mViewTitle as AppCompatTextView).text = mTitleAttributes!!.title
    }

    /**
     * 设置导航按钮
     */
    fun setNaviAttributes(attr: NaviAttributes?) {
        mNaviAttributes ?: NaviAttributes().also { mNaviAttributes = it }
        mNaviAttributes!!.naviDrawable = attr?.naviDrawable
        mNaviAttributes!!.showBack = attr?.showBack ?: true

        mComponentFactory
                ?: SimpleComponentFactory.createComponentFactory(barMode).also { mComponentFactory = it }
        mComponentFactory!!.createNavigation(this, mNaviAttributes!!)
    }

    /**
     * 菜单项修改
     */
    fun setMenuAttributes(attr: MenuAttributes?) {
        mMenuAttributes ?: MenuAttributes().also { mMenuAttributes = it }
        mMenuAttributes!!.closeDrawable = attr?.closeDrawable
        mMenuAttributes!!.moreDrawable = attr?.moreDrawable

        mComponentFactory
                ?: SimpleComponentFactory.createComponentFactory(barMode).also { mComponentFactory = it }
        mViewMenu = mComponentFactory!!.createMenu(this, mMenuAttributes!!)
    }

    /**
     * 在barType = BarMode.WEB时是使用此方法，否则抛出异常
     * 设置“更多”菜单项回调。
     */
    fun setOnMenuMoreClickListener(listener: OnMenuMoreClickListener?) {
        if (barMode != BarMode.MICRO) {
            throw IllegalArgumentException("web风格AppBar勿使用此回调")
        }
        mViewMenu?.let {
            val size = it.size()
            for (index in 0 until size) {
                val item = it.getItem(index)
                if (item.itemId == R.id.menu_more) {
                    item.setOnMenuItemClickListener {
                        listener?.onMenuMoreClick() ?: false
                    }
                }
            }
        }
    }

    /**
     * 设置是否显示“返回”图标
     */
    fun setShowBackEnabled(enabled: Boolean) {
        mIsShowBackEnabled = enabled
    }


    /**
     * 设置关闭图标
     */
    fun setCloseIcon(drawable: Drawable?) {
        val size = mViewMenu?.size() ?: 2
        for (index in 0 until size) {
            val item = mViewMenu?.getItem(index)
            if (item?.itemId == R.id.menu_close) {
                item.icon = drawable ?: context.getDrawable(DEFAULT_MICRO_CLOSE_ICON)
                return
            }
        }
    }

    /**
     * 设置更多图标
     */
    fun setMoreIcon(drawable: Drawable?) {
        val size = mViewMenu?.size() ?: 2
        for (index in 0 until size) {
            val item = mViewMenu?.getItem(index)
            if (item?.itemId == R.id.menu_more) {
                item.icon = drawable ?: context.getDrawable(DEFAULT_MICRO_MORE_ICON)
                return
            }
        }
    }
}