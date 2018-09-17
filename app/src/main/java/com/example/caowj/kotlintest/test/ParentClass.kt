package com.example.caowj.kotlintest.test

import android.util.Log
import com.example.caowj.kotlintest.util.LogUtils

/**
 *@Dec ：
 *@Author : Caowj
 *@Date : 2018/9/14 15:57
 */
open class ParentClass(name: String) {
    var a = 1

    init {
//        Log.d("caowj","This is --> primary constructor, a=$a, name=$name")
        LogUtils.d("caowj2", "This is --> primary constructor, a=$a, name=$name")
    }

    open fun publicMethod() {
        LogUtils.d("caowj2", "I am Public")
    }
}

