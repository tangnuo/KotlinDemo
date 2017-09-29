package com.example.caowj.kotlintest.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.adapter.ThirdAdapter
import com.example.caowj.kotlintest.common.BaseActivity
import com.example.caowj.kotlintest.extension.method.toast
import kotlinx.android.synthetic.main.activity_third.*

class ThirdActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)


        val adapter = ThirdAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClickListener { pos -> toast("$pos") }
    }
}
