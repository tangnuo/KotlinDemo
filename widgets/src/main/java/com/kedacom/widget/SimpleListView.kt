package com.kedacom.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.widget.BaseAdapter

/**
 * 简单的ListView模仿组件
 */
class SimpleListView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayoutCompat(context, attrs, defStyleAttr) {
    @get:JvmName("getAdapter")
    var mAdapter: BaseAdapter? = null
        private set

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    init {
        orientation = VERTICAL
    }

    /**
     * 设置Adapter数据
     *
     * @param adapter 数据适配器
     */
    fun setAdapter(adapter: BaseAdapter?) {
        this.mAdapter = adapter
        removeAllViews()
        adapter?.let { addItems(it) }
    }

    /**
     * 添加item组件
     *
     * @param adapter 数据适配器
     */
    private fun addItems(adapter: BaseAdapter) {
        val size = adapter.count
        var position = 0
        while (position < size) {
            val itemView = adapter.getView(position, null, this)
            addView(itemView, LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
            ))
            position++
        }
    }

}