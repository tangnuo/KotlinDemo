package com.example.caowj.kotlintest.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.data.FanfouPost
import com.example.caowj.kotlintest.mInterface.OnRecyclerViewOnClickListener
import kotlinx.android.synthetic.main.fanfou_item_layout.view.*

/**
 * package: com.example.caowj.kotlintest.adapter
 * author: Administrator
 * date: 2017/9/26 17:50
 */
class FanfouPostAdapter(val context: Context, val list: List<FanfouPost>) : RecyclerView.Adapter<FanfouPostAdapter.FanfouPostsViewHolder>() {

    private val inflater: LayoutInflater

    private var mListener: OnRecyclerViewOnClickListener? = null

    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FanfouPostsViewHolder {
        val view: View = inflater.inflate(R.layout.fanfou_item_layout, parent, false)
        return FanfouPostsViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(holder: FanfouPostsViewHolder, position: Int) {

        val item = list[position]

        Glide.with(context).load(item.avatarUrl).asBitmap().into(holder.itemView.avatar)

        if (!item.imgUrl.isNullOrEmpty()) {
            holder.itemView.iv_main.visibility = View.VISIBLE
        } else {
            holder.itemView.iv_main.visibility = View.INVISIBLE
        }

        holder.itemView.tv_author.text = item.author
        holder.itemView.tv_content.text = android.text.Html.fromHtml(item.content).toString()
        holder.itemView.tv_time.text = item.time
    }

    override fun getItemCount(): Int {
        return if (list != null) list.size else 0
    }


    /**
     * 设置监听器
     */
    fun setItemClickListener(listener: OnRecyclerViewOnClickListener) {
        this.mListener = listener
    }

    inner class FanfouPostsViewHolder(itemView: View, listener: OnRecyclerViewOnClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var listener: OnRecyclerViewOnClickListener

        init {
            this.listener = listener
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.OnItemClick(p0!!, layoutPosition)
        }

    }
}
