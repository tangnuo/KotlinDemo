package com.example.caowj.kotlintest.extension.bean

import org.litepal.crud.DataSupport

/**
 * package: com.example.caowj.kotlintest.extension.bean
 * author: Administrator
 * date: 2017/9/28 18:09
 */

data class Province(var id: Int = 0, var provinceName: String, var provinceCode: Int) : DataSupport()

data class County(var id: Int = 0, var countyName: String, var weatherId: String, var cityId: Int = 0) : DataSupport()

data class City(var id: Int = 0, var cityName: String, var cityCode: Int, var provinceId: Int = 0) : DataSupport()