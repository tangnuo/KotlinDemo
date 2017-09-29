package com.example.caowj.kotlintest.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.preference.PreferenceManager
import com.example.caowj.kotlintest.util.HttpUtil
import com.example.caowj.kotlintest.util.Utility
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * package: com.example.caowj.kotlintest.service
 * author: Administrator
 * date: 2017/9/29 09:32
 */
class AutoUpdateService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        updateWeather()
        updateBingPic()
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val anHour = 8 * 60 * 60 * 1000 // 这是8小时的毫秒数
        val triggerAtTime = SystemClock.elapsedRealtime() + anHour
        val i = Intent(this, AutoUpdateService::class.java)
        val pi = PendingIntent.getService(this, 0, i, 0)
        manager.cancel(pi)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi)
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 更新天气信息。
     */
    private fun updateWeather() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val weatherString = prefs.getString("weather", null)
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            val weather = Utility.handleWeatherResponse(weatherString)
            val weatherId = weather!!.basic.id
            val weatherUrl = "http://guolin.tech/api/weather?cityid=$weatherId&key=bc0418b57b2d4918819d3974ac1285d9"
            HttpUtil.sendOkHttpRequest(weatherUrl, object : Callback {
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val responseText = response.body()!!.string()
                    val weather = Utility.handleWeatherResponse(responseText)
                    if (weather != null && "ok" == weather.status) {
                        val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                        editor.putString("weather", responseText)
                        editor.apply()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })
        }
    }

    /**
     * 更新必应每日一图
     */
    private fun updateBingPic() {
        val requestBingPic = "http://guolin.tech/api/bing_pic"
        HttpUtil.sendOkHttpRequest(requestBingPic, object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val bingPic = response.body()!!.string()
                val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                editor.putString("bing_pic", bingPic)
                editor.apply()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}