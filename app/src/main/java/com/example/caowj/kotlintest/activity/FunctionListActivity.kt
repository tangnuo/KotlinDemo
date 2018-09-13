package com.example.caowj.kotlintest.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.adapter.FunctionListAdapter
import com.example.caowj.kotlintest.common.BaseActivity
import com.example.caowj.kotlintest.extension.method.toast
import com.example.caowj.kotlintest.test.TestUtil
import kotlinx.android.synthetic.main.activity_third.*

/**
 * RecyclerView列表展示
 */
class FunctionListActivity : BaseActivity() {

    val testUtil = TestUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        initDate()

        val adapter = FunctionListAdapter(initDate())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClickListener {

            pos ->
            //            toast("$pos")

            when (pos) {
                0 -> Log.d("caowj", testUtil.sum(3, 5).toString())
                1 -> testUtil.defineVariables()
                2 -> testUtil.StringTemplate()
                3 -> toast(testUtil.maxOf(3, 7).toString())
                4 -> {
                    testUtil.printProduct("6", "7")
                    testUtil.printProduct("a", "7")
                    testUtil.printProduct("a", "b")
                }
                5 -> Log.d("caowj", testUtil.getStringLength("caowj").toString())
                else -> { // 注意这个块
                    print("未知的调用")
                }
            }

        }
    }

    /**
     * 初始化数据(用于标题)
     */
    private fun initDate(): List<String> {

        val titles = listOf<String>("0基础语法", "1定义变量", "2字符串模板",
                "3条件表达式", "4用返回可空值的函数", "5使用is检测符")
        return titles
    }
}
