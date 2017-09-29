package com.example.caowj.kotlintest.common

import android.support.v7.app.AppCompatActivity

/**
 * package: com.example.caowj.kotlintest.common
 * author: Administrator
 * date: 2017/9/27 11:35
 */
open class BaseActivity : AppCompatActivity() {
    val tag = this.javaClass.simpleName
}