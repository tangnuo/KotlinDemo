package com.example.caowj.kotlintest.test

/**
 *@Dec ：
 *@Author : Caowj
 *@Date : 2018/9/13 9:31
 */
class TestUtil {

    /**
     * 基础语法
     */
    fun sum(a: Int, b: Int): Int {
        return a + b
    }


    fun sum2(a: Int, b: Int) = a + b

    /**
     * 定义变量
     */
    fun defineVariables() {
        val a: Int = 1 // 立即赋值
        val b = 2 // 自动推断出 `Int` 类型
        val c: Int // 如果没有初始值类型不能省略
        c = 3 // 明确赋值


        println("a = $a, b = $b, c = $c")

        var x = 5 // 自动推断出 `Int` 类型
        x += 1
        println("x = $x")
    }

    /**
     * 字符串模板
     */
    fun StringTemplate() {
        var a = 1
// 模板中的简单名称：
        val s1 = "a is $a"

        a = 2
// 模板中的任意表达式：
        val s2 = "${s1.replace("is", "was")}, but now is $a"
//sampleEnd
        println(s2)
    }


    /**
     * 条件表达式
     *
     * println("max of 0 and 42 is ${maxOf(0, 42)}")
     */
    fun maxOf(a: Int, b: Int): Int {
        if (a > b) {
            return a
        } else {
            return b
        }
    }

    /**
     * 条件表达式简单写法
     */
    fun maxOf2(a: Int, b: Int) = if (a > b) a else b

    /**
     * 如果 str 的内容不是数字返回 null
     */
    fun parseInt(str: String): Int? {
        return str.toIntOrNull()
    }

    /**
     * 用返回可空值的函数
     *
     * printProduct("6", "7")
     * printProduct("a", "7")
     * printProduct("a", "b")
     */
    fun printProduct(arg1: String, arg2: String) {
        val x = parseInt(arg1)
        val y = parseInt(arg2)
// 直接使用 `x * y` 会导致编译错误，因为他们可能为 null
        if (x != null && y != null) {
// 在空检测后，x 与 y 会自动转换为非空值（non-nullable）
            println(x * y)
        } else {
            println("either '$arg1' or '$arg2' is not a number")
        }
    }

    fun printProduct2(arg1: String, arg2: String) {
        val x = parseInt(arg1)
        val y = parseInt(arg2)

        if (x == null) {
            println("Wrong number format in arg1: '$arg1'")
            return
        }
        if (y == null) {
            println("Wrong number format in arg2: '$arg2'")
            return
        }
// 在空检测后，x 与 y 会自动转换为非空值
        println(x * y)

    }

    /**
     * 使用is检测符
     */
    fun getStringLength(obj: Any): Int? {
        if (obj is String) {
            return obj.length
        }

        return null
    }

    /**
     * 使用 for 循环
     */
    fun useFor() {
        val items = listOf("apple", "banana", "kiss")
        for (item in items) {
            println(item)
        }

        for (index in items.indices) {
            println("item at $index is $ ${items[index]}")
        }
    }

    /**
     * 使用while循环
     */
    fun useWhile() {
        val items = listOf("aaa", "bbbb", "ccccc")

        var index = 0

        while (index < items.size) {
            println("item at $index is ${items[index]}")
            index++
        }
    }

    /**
     * 使用when表达式
     *
     * println(describe(1))
    println(describe("Hello"))
    println(describe(1000L))
    println(describe(2))
    println(describe("other"))
     */
    fun useWhen(obj: Any): String =
            when (obj) {
                1 -> "one"
                is Long -> "Long"
                !is String -> "Not a string"
                else -> "Unknown"

            }

    /**
     * 使用 in 运算符来检测某个数字是否在指定区间内：
     */
    fun useIn() {
        val x = 10
        val y = 9
        if (x in 1..y + 1) {
            println("fits in range")
        } else {
            println("x = $x 不属于这个区间。")
        }


        val list = listOf("a", "b", "c")
        if (-1 !in 0..list.lastIndex) {
            println("-1 is out of range")
        }

        if (list.size !in list.indices) {
            println("list size is out of valid list indices range too")
        }


        val items = setOf("apple", "banana", "kiwifruit")
//sampleStart
        when {
            "orange" in items -> println("juicy")
            "apple" in items -> println("apple is fine too")
        }

    }

    /**
     * 区间迭代或数列迭代
     * for (i in 1..100) { …… } // 闭区间：包含 100
     * for (i in 1 until 100) { …… } // 半开区间：不包含 100
     * for (x in 2..10 step 2) { …… }
     * for (x in 10 downTo 1) { …… }
     * if (x in 1..10) { …… }
     */
    fun rangeIteration() {

        for (x in 1..5) {
            print("x is $x")
        }
        println()

        for (x in 1..20 step 2) {
            print("x is $x")
        }
        println()

        for (x in 9 downTo 0 step 3) {
            print(x)
        }

    }

    /**
     * 使用 lambda 表达式来过滤（filter）与映射（map）集合
     */
    fun useLambda() {
        val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
        fruits
                .filter { it.startsWith("a") }
                .sortedBy { it }
                .map { it.toUpperCase() }
                .forEach { println(it) }

    }

//遍历map
//for ((k, v) in map) {
//    println("$k -> $v")
//}


//////////////////////////////////////////////////////

}
