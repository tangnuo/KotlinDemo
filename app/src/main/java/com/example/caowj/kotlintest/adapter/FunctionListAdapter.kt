package com.example.caowj.kotlintest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.caowj.kotlintest.R
import kotlinx.android.synthetic.main.item_third.view.*

/**
 * package: com.example.caowj.kotlintest.adapter
 * author: Administrator
 * date: 2017/9/26 17:11
 */
class FunctionListAdapter(val objs: List<String>) : RecyclerView.Adapter<FunctionListAdapter.mViewHolder>() {

    var mListener: ((pos: Int) -> Unit)? = null

    fun setOnItemClickListener(mListener: ((pos: Int) -> Unit)) {
        this.mListener = mListener
    }


    override fun getItemCount(): Int {
        return objs?.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        return mViewHolder(View.inflate(parent?.context, R.layout.item_third, null))
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {

        /**
         * 代码注释：
         * with()：返回是最后一行。holder可能为空；!!表示有空指针异常的可能。
         * {}内部：可以直接使用holder.itemView
         */

        with(holder?.itemView!!) {
            //            tv_content.text = "第 $position 条数据"
            tv_content.text = objs.get(position)
            setOnClickListener { mListener?.invoke(position) }
        }
    }


    class mViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)
}