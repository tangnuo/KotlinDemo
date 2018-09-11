package com.example.caowj.kotlintest.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.activity.weather.WeatherChooseActivity
import com.example.caowj.kotlintest.activity.weather.WeatherDetailActivity
import com.example.caowj.kotlintest.extension.bean.City
import com.example.caowj.kotlintest.extension.bean.County
import com.example.caowj.kotlintest.extension.bean.Province
import com.example.caowj.kotlintest.util.HttpUtil
import com.example.caowj.kotlintest.util.Utility
import kotlinx.android.synthetic.main.activity_weather_detail.*
import kotlinx.android.synthetic.main.choose_area.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.litepal.crud.DataSupport
import java.io.IOException

/**
 * 城市选择
 * package: com.example.caowj.kotlintest.fragment
 * author: Administrator
 * date: 2017/9/28 18:02
 */
class ChooseAreaFragment : Fragment() {
    companion object {
        val LEVEL_PROVINCE = 0
        val LEVEL_CITY = 1
        val LEVEL_COUNTY = 2
    }

    private var progressDialog: ProgressDialog? = null
    private var adapter: ArrayAdapter<String>? = null
    private val dataList = ArrayList<String>()

    private var provinceList: List<Province>? = null
    private var cityList: List<City>? = null
    private var countyList: List<County>? = null
    private var selectedProvince: Province? = null
    private var selectedCity: City? = null
    private var currentLevel: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.choose_area, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }


    /**
     * 初始化
     */
    private fun init() {
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, dataList)
        list_view.adapter = adapter
        list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (currentLevel == LEVEL_PROVINCE) {
                selectedProvince = provinceList!![position]
                queryCities()
            } else if (currentLevel == LEVEL_CITY) {
                selectedCity = cityList!!.get(position)
                queryCounties()
            } else if (currentLevel == LEVEL_COUNTY) {
                val weatherId = countyList!!.get(position).weatherId
                Log.d(tag, "weatherId：" + weatherId)
                if (activity is WeatherChooseActivity) {
                    val intent = Intent(activity, WeatherDetailActivity::class.java)
                    intent.putExtra("weather_id", weatherId)
                    startActivity(intent)
                    (activity as WeatherChooseActivity).finish()
                } else if (activity is WeatherDetailActivity) {
                    val activity = activity as WeatherDetailActivity
                    activity.drawer_layout.closeDrawers()
                    activity.swipe_refresh.isRefreshing = true
                    activity.requestWeather(weatherId)
                }
            }
        }

        back_button.setOnClickListener {
            if (currentLevel == LEVEL_COUNTY) {
                queryCities()
            } else if (currentLevel == LEVEL_CITY) {
                queryProvinces()
            }
        }
        queryProvinces()
    }


    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private fun queryProvinces() {
        title_text.text = "中国"
        back_button.visibility = View.GONE
        provinceList = DataSupport.findAll(Province::class.java)
        if (provinceList!!.size > 0) {
            dataList.clear()
            for (province in provinceList!!) {
                dataList.add(province.provinceName)
            }
            adapter!!.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_PROVINCE
        } else {
            val address = "http://guolin.tech/api/china"
            queryFromServer(address, "province")
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private fun queryCities() {
        title_text.text = selectedProvince!!.provinceName
        back_button.visibility = View.VISIBLE
        cityList = DataSupport.where("provinceid = ?", selectedProvince!!.id.toString()).find(City::class.java)
        if (cityList!!.size > 0) {
            dataList.clear()
            for (city in cityList!!) {
                dataList.add(city.cityName)
            }
            adapter!!.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_CITY
        } else {
            val provinceCode = selectedProvince!!.provinceCode
            val address = "http://guolin.tech/api/china/" + provinceCode
            queryFromServer(address, "city")
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private fun queryCounties() {
        title_text.text = selectedCity!!.cityName
        back_button.visibility = View.VISIBLE
        countyList = DataSupport.where("cityid = ?", selectedCity!!.id.toString()).find(County::class.java)
        if (countyList!!.size > 0) {
            dataList.clear()
            for (county in countyList!!) {
                dataList.add(county.countyName)
            }
            adapter!!.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_COUNTY
        } else {
            val provinceCode = selectedProvince!!.provinceCode
            val cityCode = selectedCity!!.cityCode
            val address = "http://guolin.tech/api/china/$provinceCode/$cityCode"
            queryFromServer(address, "county")
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据。
     */
    private fun queryFromServer(address: String, type: String) {
        showProgressDialog()
        HttpUtil.sendOkHttpRequest(address, object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                activity?.runOnUiThread {
                    closeProgressDialog()
                    Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                val responseText = response!!.body()!!.string()
                var result = false
                if ("province" == type) {
                    result = Utility.handleProvinceResponse(responseText)
                } else if ("city" == type) {
                    result = Utility.handleCityResponse(responseText, selectedProvince!!.id)
                } else if ("county" == type) {
                    result = Utility.handleCountyResponse(responseText, selectedCity!!.id)
                }
                if (result) {
                    activity?.runOnUiThread {
                        closeProgressDialog()
                        if ("province" == type) {
                            queryProvinces()
                        } else if ("city" == type) {
                            queryCities()
                        } else if ("county" == type) {
                            queryCounties()
                        }
                    }
                }
            }

        })
    }

    /**
     * 显示进度对话框
     */
    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setMessage("正在加载...")
            progressDialog!!.setCanceledOnTouchOutside(false)
        }
        progressDialog!!.show()
    }

    /**
     * 关闭进度对话框
     */
    private fun closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}