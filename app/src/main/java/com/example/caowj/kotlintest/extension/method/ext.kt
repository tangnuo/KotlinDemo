package com.example.caowj.kotlintest.extension.method

import android.content.Context
import android.widget.Toast

/**
 * 函数扩展
 * package: com.example.caowj.kotlintest
 * author: Administrator
 * date: 2017/9/26 15:22
 */
fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    if (message == null) Toast.makeText(this, "空提示信息", length).show() else Toast.makeText(this, message, length).show()

}