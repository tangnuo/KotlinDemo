package com.example.caowj.kotlintest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.caowj.kotlintest.activity.*
import com.example.caowj.kotlintest.activity.fanfou.FanfouActivity
import com.example.caowj.kotlintest.kedacom.activity.LibTestActivity
import com.example.caowj.kotlintest.activity.weather.WeatherChooseActivity
import com.example.caowj.kotlintest.adapter.MainAdapter
import com.example.caowj.kotlintest.data.FunctionInfo
import com.example.caowj.kotlintest.mInterface.OnRecyclerViewOnClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mainList = ArrayList<FunctionInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        val mainAdapter = MainAdapter(this, mainList)
        mRecyclerView.adapter = mainAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mainAdapter.setItemClickListener(object : OnRecyclerViewOnClickListener {
            override fun OnItemClick(v: View, position: Int) {
                val intent = Intent(this@MainActivity, mainList[position].className)
                startActivity(intent)
            }
        })
    }

    private fun initData() {
        mainList.add(FunctionInfo(FristActivity::class.java, "小功能展示"))
        mainList.add(FunctionInfo(BlogDemo1Activity::class.java, "博客练习1"))
        mainList.add(FunctionInfo(BlogDemo2Activity::class.java, "博客练习2"))
        mainList.add(FunctionInfo(FunctionListActivity::class.java, "简单的RecyclerView列表"))
        mainList.add(FunctionInfo(FanfouActivity::class.java, "饭否APP"))
        mainList.add(FunctionInfo(WeatherChooseActivity::class.java, "天气"))
        mainList.add(FunctionInfo(LibTestActivity::class.java, "kedacom_lib调用"))
        mainList.add(FunctionInfo(FileUriActivity::class.java, "File Uri解析"))
    }

}
