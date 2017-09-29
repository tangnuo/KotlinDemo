package com.example.caowj.kotlintest.extension.bean

/**
 * package: com.example.caowj.kotlintest.extension.bean
 * author: Administrator
 * date: 2017/9/28 18:14
 */
data class Weather(
        val aqi: Aqi,
        val basic: Basic,
        val daily_forecast: List<DailyForecast>,
        val hourly_forecast: List<HourlyForecast>,
        val now: Now,
        val status: String, //ok
        val suggestion: Suggestion
)


data class Aqi(
        val city: City2
)

data class City2(
        val aqi: String, //17
        val co: String, //1
        val no2: String, //13
        val o3: String, //47
        val pm10: String, //17
        val pm25: String, //8
        val qlty: String, //优
        val so2: String //5
)

data class HourlyForecast(
        val cond: Cond,
        val date: String, //2017-09-25 19:00
        val hum: String, //81
        val pop: String, //0
        val pres: String, //1012
        val tmp: String, //29
        val wind: Wind
)

data class Wind(
        val deg: String, //258
        val dir: String, //西南风
        val sc: String, //微风
        val spd: String //5
)

data class Cond(
        val code: String, //103
        val txt: String //晴间多云
)

data class Basic(
        val city: String, //南投
        val cnty: String, //中国
        val id: String, //CN101340404
        val lat: String, //23.91600037
        val lon: String, //120.68499756
        val update: Update
)

data class Update(
        val loc: String, //2017-09-25 16:46
        val utc: String //2017-09-25 08:46
)

data class Now(
        val cond: Cond,
        val fl: String, //39
        val hum: String, //73
        val pcpn: String, //0.00
        val pres: String, //1012
        val tmp: String, //32
        val vis: String, //20
        val wind: Wind
)

data class Suggestion(
        val air: Air,
        val comf: Comf,
        val cw: Cw,
        val drsg: Drsg,
        val flu: Flu,
        val sport: Sport,
        val trav: Trav,
        val uv: Uv
)

data class Sport(
        val brf: String, //较不宜
        val txt: String //阴天，且天气较热，请减少运动时间并降低运动强度。
)

data class Air(
        val brf: String, //良
        val txt: String //气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。
)

data class Comf(
        val brf: String, //较不舒适
        val txt: String //白天天气多云，同时会感到有些热，不很舒适。
)

data class Uv(
        val brf: String, //弱
        val txt: String //紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。
)

data class Drsg(
        val brf: String, //炎热
        val txt: String //天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
)

data class Flu(
        val brf: String, //少发
        val txt: String //各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。
)

data class Trav(
        val brf: String, //适宜
        val txt: String //天气较好，风稍大，稍热，总体来说还是好天气。适宜旅游，可不要错过机会呦！
)

data class Cw(
        val brf: String, //较不宜
        val txt: String //较不宜洗车，未来一天无雨，风力较大，如果执意擦洗汽车，要做好蒙上污垢的心理准备。
)

data class DailyForecast(
        val astro: Astro,
        val cond: Cond,
        val date: String, //2017-09-25
        val hum: String, //69
        val pcpn: String, //3.8
        val pop: String, //10
        val pres: String, //1012
        val tmp: Tmp,
        val uv: String, //10
        val vis: String, //15
        val wind: Wind
)

data class Tmp(
        val max: String, //33
        val min: String //26
)

data class Astro(
        val mr: String, //10:00
        val ms: String, //21:24
        val sr: String, //05:46
        val ss: String //17:50
)