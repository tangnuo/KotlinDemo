package com.example.caowj.kotlintest.test

/**
 * <pre>
 *     派生类初始化顺序
 *     作者：Caowj
 *     邮箱：caoweijian@kedacom.com
 *     日期：2020/3/13 13:58
 * </pre>
 */

open class Base(val name: String) {
    init {
        println("1111111111")
    }

    open val size: Int = name.length.also {
        println("22222222222:$it")
    }
}

class Derived(name: String, val lastName: String) : Base(name.capitalize().also { println("3333333333:$it") }) {
    init {
        println("4444444444")
    }

    override val size: Int = (super.size + lastName.length).also { println("5555555555555:$it") }
}

fun main() {
    println("6666666666")
    val d = Derived("hello", "world")
}

//【派生类初始化顺序】执行结果输出：
//    6666666666
//    3333333333:Hello
//    1111111111
//    22222222222:5
//    4444444444
//    5555555555555:10

//------------------------------------------------------------------------------------------------

