package com.example.caowj.kotlintest.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.common.BaseActivity
import com.example.caowj.kotlintest.data.UserInfo
import com.example.caowj.kotlintest.extension.method.toast

class SecondActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        toast(intent.getParcelableExtra<UserInfo>(USER_TAG).toString())
    }

    //创建一个伴生对象
    companion object {
        val USER_TAG = "USER"

        fun mStartActivity(context: Context, userInfo: UserInfo) {
            val intent = Intent(context, SecondActivity::class.java)
            intent.putExtra(USER_TAG, userInfo)
            context.startActivity(intent)
        }

    }
}
