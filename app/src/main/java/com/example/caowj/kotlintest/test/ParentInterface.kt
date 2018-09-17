package com.example.caowj.kotlintest.test

import com.example.caowj.kotlintest.util.LogUtils

/**
 *@Dec ：
 *@Author : Caowj
 *@Date : 2018/9/14 16:16
 */
interface ParentInterface {
    fun publicMethod() {
        LogUtils.d("caowj2", "I am public from ParentInterface")
    }
}