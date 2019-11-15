package com.kedacom.widget.appbar.menu

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.support.annotation.RestrictTo
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.PopupWindow
import com.kedacom.widget.appbar.BiswitchAppBar
import com.kedacom.widget.appbar.DEFAULT_BACKGROUND
import com.kedacom.widget.appbar.menu.build.MicroMenuAdapter

/**
 * 将NORMAL的转换成MICRO的菜单
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class MicroMenuHelper(val context: Context) {

    fun showMenu(menu: Menu, listener: BiswitchAppBar.OnMenuItemClickListener?) {
        if (context !is Activity) {
            return
        }
        val menuItemList: ArrayList<MenuItem> = arrayListOf()
        val size = menu.size()
        for (index in 0 until size) {
            menuItemList.add(menu.getItem(index))
        }
        val popupMenu = PopupWindow()
        popupMenu.animationStyle = android.R.style.Animation_InputMethod
        popupMenu.setBackgroundDrawable(ColorDrawable(DEFAULT_BACKGROUND))
        popupMenu.isFocusable = true
        popupMenu.isOutsideTouchable = true
        popupMenu.width = Resources.getSystem().displayMetrics.widthPixels
        popupMenu.height = ViewGroup.LayoutParams.WRAP_CONTENT

        val adapter = MicroMenuAdapter(context, menuItemList, listener, popupMenu)
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter


        popupMenu.contentView = recyclerView
        popupMenu.setOnDismissListener {
            restoreWindow(context)
        }

        val rootView = context.findViewById<View>(android.R.id.content)
        if (rootView.windowToken == null) {
            return
        }
        dimWindow(context)
        popupMenu.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
    }

    fun showMenu(menuList: ArrayList<MenuItem>, listener: BiswitchAppBar.OnMenuItemClickListener?) {
        if (context !is Activity) {
            return
        }
        val popupMenu = PopupWindow()
        popupMenu.animationStyle = android.R.style.Animation_InputMethod
        popupMenu.setBackgroundDrawable(ColorDrawable(DEFAULT_BACKGROUND))
        popupMenu.isFocusable = true
        popupMenu.isOutsideTouchable = true
        popupMenu.width = Resources.getSystem().displayMetrics.widthPixels
        popupMenu.height = ViewGroup.LayoutParams.WRAP_CONTENT

        val adapter = MicroMenuAdapter(context, menuList, listener, popupMenu)
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        popupMenu.contentView = recyclerView
        popupMenu.setOnDismissListener {
            restoreWindow(context)
        }

        val rootView = context.findViewById<View>(android.R.id.content)
        if (rootView.windowToken == null) {
            return
        }
        dimWindow(context)
        popupMenu.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
    }

    private fun dimWindow(activity: Activity) {
        val wlp = activity.window.attributes
        wlp.alpha = 0.4f
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        activity.window.attributes = wlp
    }

    private fun restoreWindow(activity: Activity) {
        val wlp = activity.window.attributes
        wlp.alpha = 1.0f
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        activity.window.attributes = wlp
    }
}