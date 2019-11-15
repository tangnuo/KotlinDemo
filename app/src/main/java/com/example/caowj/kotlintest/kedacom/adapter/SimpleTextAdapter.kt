package com.example.caowj.kotlintest.kedacom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

abstract class SimpleTextAdapter(private val context: Context) : RecyclerView.Adapter<SimpleTextVH>() {
    protected val mFunctionList = arrayListOf<String>()
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleTextVH {
        val itemView = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        return SimpleTextVH(itemView)
    }

    override fun getItemCount(): Int {
        return mFunctionList.size
    }

    override fun onBindViewHolder(vh: SimpleTextVH, position: Int) {
        (vh.itemView as TextView).text = mFunctionList[position]
        vh.itemView.setOnClickListener { view ->
            listener?.onItemClick((view as TextView).text.toString())
        }
    }

    interface OnItemClickListener {
        fun onItemClick(function: String)
    }

}

class SimpleTextVH(itemView: View) : RecyclerView.ViewHolder(itemView)