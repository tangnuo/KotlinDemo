package com.example.caowj.kotlintest.util

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * package: com.example.caowj.kotlintest.util
 * author: Administrator
 * date: 2017/9/29 09:16
 */
object HttpUtil {
    fun sendOkHttpRequest(address: String, callback: Callback) {
        val client = OkHttpClient()
        val request = Request.Builder().url(address).build()
        client.newCall(request).enqueue(callback)
    }
}