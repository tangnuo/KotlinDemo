package com.example.caowj.kotlintest

import java.util.*
import kotlin.collections.ArrayList

/**
 * package: com.example.caowj.kotlintest
 * author: Administrator
 * date: 2017/9/21 14:00
 */

fun main(args: Array<String>) {
//    println("caowj")

    val testMethod = TestMethod()

//   println("结果:${testMethod.testLet2()}")
//    testMethod.testApply()
//    testMethod.testWith()
    testMethod.testRun()
}


class TestMethod {
    fun testRun() {
        "testRun".run { println("this = " + this) }.let { println(it) }
    }

    fun testWith() {
        with(ArrayList<String>()) {
            add("testWith1")
            add("testWith2")
            add("testWith3")
            println("this = " + this)
        }.let { println(it) }
    }

    fun testApply() {
        ArrayList<String>().apply {
            add("testApplay1")
            add("testApplay2")
            add("testApplay3")

            println("this=$this")
        }.let { println(it) }
    }

    fun testLet2(): Int {
        "testLet".let {
            if (Random().nextBoolean()) {
                println(it)
                return 1
            } else {
                println(it)
                return 2
            }
        }
    }


    fun testLet(): Int {
        "testLet".let {
            println(it)
            println(it)
            println(it)
            return 1
        }
    }


    fun main(args: Array<String>) {

//        println("sun of 3 and 5=" + sum(3, 5))
//
//        var demo1 = Demo1()
//        demo1.testFun("content")

//        printProduct("6", "7")
//        printProduct("a", "7")


//        fun printLength(obj: Any) {
//            println("$obj string length is ${getStringLength(obj)}")
//        }
//
//        printLength(1000)

    }


    fun testWhen(obj: Any): String {
        var ss: String = ""
        when (obj) {
            1 -> ss = "one"
            "hello" -> ss = "Greeting"
            is Long -> ss = "Long"

        }

        return ss
    }

    fun testArray() {
        val asc = Array(5, { i -> (i * i).toString() })
    }

    fun testFor() {
        val item1 = listOf("apple", "kiwi")
        val item2 = listOfNotNull("apple", "banana")
        for (index in item1) {
            println(index)
            println("$index")
        }

        for (index in item2.indices) {
            println("item at $index is ${item2[index]}")
        }

        var index = 0
        while (index < item1.size) {
            println("item at $index is ${item1[index]}")
            index++
        }

        val x = 10
        val y = 9
        if (x in 1..y + 1) {
            println("fits in range")
        }


        for (x in 1..5) {
            println("" + x + "ss+${x}" + x)
        }
    }


    fun getStringLength(obj: Any): Int? {
        if (obj is String) {
            return obj.length
        } else {
            return null
        }
    }


    /**
     * 定义函数
     */
    fun sum(a: Int, b: Int): Int {

        return a + b
    }


    /**
     * 函数返回无意义的值<br/>
     *
     * Unit 返回类型可以省略：
     */
    fun printSum(a: Int, b: Int): Unit {
        val a: Int = 1  // 立即赋值
        val b = 2   // 自动推断出 `Int` 类型
        val c: Int  // 如果没有初始值类型不能省略
        c = 3       // 明确赋值

        println("a = $a, b = $b, c = $c")
        println(" 函数返回无意义的值")
    }

    fun mString() {
        var a = 1
        var s1 = "a is $a"
        a = 2
        var s2 = "${s1.replace("is", "was")}, but now is $a"
        println(s2)
    }

    fun parseInt(str: String): Int? {
        return str.toIntOrNull()
    }

    fun printProduct(arg1: String, arg2: String) {
        val x = parseInt(arg1)
        val y = parseInt(arg2)

        // 直接使用 `x * y` 可能会报错，因为他们可能为 null
        if (x != null && y != null) {
            // 在空检测后，x 和 y 会自动转换为非空值（non-nullable）
            println(x * y)
        } else {
            println("either '$arg1' or '$arg2' is not a number")
        }
    }


    class Demo1 {
        var name: String = ""

        fun testFun(name: String) {
            this.name = name
            println(this.name)
        }

    }
}

