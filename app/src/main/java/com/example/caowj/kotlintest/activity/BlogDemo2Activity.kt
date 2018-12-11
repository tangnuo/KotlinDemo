package com.example.caowj.kotlintest.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.common.BaseActivity
import kotlinx.android.synthetic.main.item_third.view.*

/**
 * 按照blog中的案例练习写法2
 *
 * https://www.jianshu.com/p/847dbc10e60f
 */
class BlogDemo2Activity : BaseActivity() {

    val items = listOf("1111", "2222", "3333")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_demo2)
        val recyclerView = findViewById(R.id.mRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        //写法一：
        //recyclerView.adapter = BlogDemoAdapter(items)

        //写法二：
        val itemClickListener: (title1: String) -> Unit = {
            showToast("点击了：" + it)
            Log.d("caowj", "" + it)
        }

        recyclerView.adapter = BlogDemoAdapter2(items, itemClickListener)

//        //写法三（同2）：
//        recyclerView.adapter = BlogDemoAdapter2(items) {
//            showToast(it)
//            Log.d("caowj", "" + it)
//        }
    }

    class BlogDemoAdapter(val items: List<String>) : RecyclerView.Adapter<BlogDemoAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(TextView(p0.context))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            p0.textView.text = items[p1]
        }

        class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
    }

    class BlogDemoAdapter2(val items: List<String>, val itemClickListener: (String) -> Unit) : RecyclerView.Adapter<BlogDemoAdapter2.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            val view = LayoutInflater.from(p0.context).inflate(R.layout.item_third, p0, false)
            return ViewHolder(view, itemClickListener)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            p0.bind(items[p1])
        }

        class ViewHolder(val view: View, val itemClickListener: (String) -> Unit) : RecyclerView.ViewHolder(view) {
            fun bind(title1: String) {
                view.tv_content.text = title1
                view.setOnClickListener {
                    itemClickListener(title1)
                }
            }
        }
    }
}
