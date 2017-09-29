package com.example.caowj.kotlintest.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.data.FunctionInfo
import com.example.caowj.kotlintest.mInterface.OnRecyclerViewOnClickListener
import kotlinx.android.synthetic.main.item_main.view.*

/**
 * package: com.example.caowj.kotlintest.adapter
 * author: Administrator
 * date: 2017/9/27 10:00
 */
class MainAdapter(val mContext: Context, val list: List<FunctionInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater
    private var mListener: OnRecyclerViewOnClickListener? = null

    init {
        this.inflater = LayoutInflater.from(mContext)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view: View = inflater.inflate(R.layout.item_main, parent, false)
        return MainViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]

        Glide.with(mContext).load(R.mipmap.ic_launcher).asBitmap().into(holder.itemView.avatar)
        holder.itemView.tv_author.text = item.title
    }

    /****************************************************/

    fun setItemClickListener(listener: OnRecyclerViewOnClickListener) {
        this.mListener = listener
    }

    class MainViewHolder(itemView: View, listener: OnRecyclerViewOnClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var listener: OnRecyclerViewOnClickListener

        init {
            this.listener = listener
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.OnItemClick(v!!, layoutPosition)
        }

    }
}