package com.example.caowj.kotlintest.test

/**
 *@Dec ：子类，//无次级构造函数的写法
 *@Author : Caowj
 *@Date : 2018/9/14 16:11
 */
class Child1(name: String, age: Int) : ParentClass(name) {

    var allBy: Int = 0
    var initi = 1
    var stub = ""

    var str: String = ""
        get() = field
        set(value) {
            field = value+"keda"
        }

    private fun setDateFormString(value: String) {
        value + "caowj"
    }

    override fun publicMethod() {
        super.publicMethod()
    }
}

fun main() {
    var c = Child1("456", 1)
    c.str = "123"
    c.stub = "1234"
    println(c.str)
    println(c.stub)
}