package com.example.caowj.kotlintest.activity

import android.os.Bundle
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.common.BaseActivity
import com.example.caowj.kotlintest.data.UserInfo
import com.example.caowj.kotlintest.extension.method.toast
import kotlinx.android.synthetic.main.activity_frist.*

class FristActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frist)

        initView()
    }


    private fun initView() {
        textView.text = "页面跳转，传值、序列化"
        button.setOnClickListener {
            val user = UserInfo("caowj", 18, 10)
            user.id = 10
            SecondActivity.mStartActivity(this, user)
        }


        button3.text = "函数扩展"
        button3.setOnClickListener { toast("函数扩展") }

    }
}
