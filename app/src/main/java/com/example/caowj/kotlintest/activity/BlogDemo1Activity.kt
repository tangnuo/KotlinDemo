package com.example.caowj.kotlintest.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.common.BaseActivity
import kotlinx.android.synthetic.main.activity_blog_demo1.*

/**
 * 按照blog中的案例练习写法1
 *
 * https://blog.csdn.net/clandellen/article/details/76283527
 */
class BlogDemo1Activity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_demo1)

        setClick()
    }

    private fun setClick() {
        btn_1.setText("类型转换-as")
        btn_1.setOnClickListener(this)

        btn_2.setText("别名机制")
        btn_2.setOnClickListener(this)

        btn_3.setText("Kotlin的“区间-Range”")
        btn_3.setOnClickListener(this)

        btn_4.setText("Kotlin的数组")
        btn_4.setOnClickListener(this)

        btn_5.setText("kotlin中的 “==” 与 “===”")
        btn_5.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.btn_1 -> {
                showToast("类型转换")
                var p: Person = Student("caowj", 18)
                var s: Student = p as Student  //把Person类型的p转换为Student类型
                Log.d("caowj", s.toString())
            }

            R.id.btn_2 -> {
                var str: String = "如同Java当中在一个类中同时使用了多次名字相同但是包名路径不一样的类，Java当中是这么处理的，就是带上包路径来区分;" +
                        "这样真是太繁琐麻烦了，每次使用都还要加上包路径，简直都不能好好敲代码了，" +
                        "Kotlin就不存在这种麻烦，Kotlin处理这种情况的方法叫别名机制。"
                Log.d("caowj", str)
            }

            R.id.btn_3 -> {
                var intRange: IntRange = 0..1024//区间为[0,1024]，注意是全闭的
                var intRange2: IntRange = 0 until 1024//区间为[0,1024),注意是半闭半开的，包头不包尾

                println("50是否存在与区间[0,1024]:" + intRange.contains(50))
                println("-3是否存在与区间[0,1024):" + intRange.contains(-3))

                //遍历区间
                for (i in intRange2) {
                    println(i)
                }
            }

            R.id.btn_4 -> {
                var intArray: IntArray = intArrayOf(2, 3, 4, 5, 6)
                var studentArray: Array<Student> = arrayOf(
                        Student("夫子", 135),
                        Student("李慢慢", 36),
                        Student("君陌", 30),
                        Student("余帘", 29))

                //数组的修改
                studentArray[0] = Student("昊天", 1000)


                for (teacher in studentArray) {
                    println(teacher.name + ":" + teacher.age)
                }
            }

            R.id.btn_5 -> {
                var intRange1: IntRange = 0..1024
                var intRange2: IntRange = 0..1024

                println(intRange1 == intRange2)

                println(intRange1 === intRange2)
            }

            else -> showToast("其他情况")
        }

    }

    open class Person {
        public var name: String = ""
        public var age: Int = 0

        constructor(name: String, age: Int) {
            this.name = name
            this.age = age
        }
    }

    class Student(name: String, age: Int) : Person(name, age)
}
