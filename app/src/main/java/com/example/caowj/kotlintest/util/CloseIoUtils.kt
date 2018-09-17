package com.example.caowj.kotlintest.util

import java.io.Closeable
import java.io.IOException

/**
 *@Dec ：
 *@Author : Caowj
 *@Date : 2018/9/14 16:05
 */
/**
 * Created by laijian on 2017/8/10.
 * 关闭io 工具类
 */
object CloseIoUtils {

    /**
     * 关闭IO
     * @param closeables closeables
     */
    fun closeIO(vararg closeables: Closeable) {
        if (closeables == null) return
        closeables
                .filterNotNull()
                .forEach {
                    try {
                        it!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
    }

}