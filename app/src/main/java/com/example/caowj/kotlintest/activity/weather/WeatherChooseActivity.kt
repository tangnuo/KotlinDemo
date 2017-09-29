package com.example.caowj.kotlintest.activity.weather

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.common.BaseActivity

/**
 * 天气<p/>
 *
 * 根据郭林的第一行代码：https://github.com/myloften/KWeather  <p/>
 *
 * 注意：
 * 1、OKhttp3数据请求
 * 2、litepal操作本地数据库
 *
 *
 */
class WeatherChooseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_choose)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        //如果缓存的有城市信息，直接跳转
        if (prefs.getString("weather", null) != null) {
            val intent = Intent(this, WeatherDetailActivity::class.java)
            startActivity(intent)
            finish()
        } else {
//            通过Fragment页面选择城市信息
        }
    }
}
