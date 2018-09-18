package com.example.caowj.kotlintest.blog

/**
 *@Dec ：操作符简单示例
 *@Author : Caowj
 *@Date : 2018/9/17 15:29
 */


fun main() {

    test1(4, "123", "456")

    test2("123", "346", a = 4)

//    给可变参数传入数组（在数组参数前加“*”运算符）
    val books = arrayOf("123", "456", "789")
    test2(*books, a = 4)


    fun square(n: Int): Int {
        return n * n
    }

    val data = arrayOf(3, 2, 4, 5, 6)
    test5(data, ::square)


    println("输出：" + test6("cube"))
}

/**
 * 形参个数可变的函数
 *
 * 测试： test1(4, "123", "456")
 */
fun test1(a: Int, vararg books: String) {
    for (b in books) {
        println(b)
    }

    println(a)
}

/**
 * 可变参数的后续参数，需要使用命名参数
 *
 * test2("123", "346", a = 4)
 */
fun test2(vararg books: String, a: Int) {
    for (b in books) {
        println(b)
    }

    println(a)
}

/**
 * 尾递归函数
 */
tailrec fun test3(n: Int, total: Int = 1): Int =
        if (n == 1)
            total
        else
            test3(n - 1, total * n)

/**
 * 函数类型
 */
fun test4() {
    // 定义一个变量，其类型为(Int , Int) -> Int
    var myfun: (Int, Int) -> Int
    // 定义一个变量，其类型为(String) -> Unit
    var test: (String)

    // 定义一个计算乘方的函数
    fun pow(base: Int, exponent: Int): Int {
        var result = 1
        for (i in 1..exponent) {
            result *= base
        }
        return result
    }

// 将::pow函数赋值给myfun，则myfun可当成pow使用
    myfun = ::pow
    println(myfun(3, 4)) // 输出81

    // 定义一个计算面积的函数
    fun area(width: Int, height: Int): Int {
        return width * height
    }
// 将area函数赋值给myfun，则myfun可当成area使用
    myfun = ::area
    println(myfun(3, 4)) // 输出12
}

/**
 * 函数类型作为形参
 *
 * test5(data, ::square)
 */
fun test5(data: Array<Int>, fn: (Int) -> Int): Array<Int> {
    var result = Array<Int>(data.size, { 0 })
    for (i in data.indices) {
        result[i] = fn(data[i])
    }

    return result
}

/**
 * 定义函数，该函数的返回值类型为(Int) -> Int
 */
fun test6(type: String): (Int) -> Int {
    // 定义一个计算平方的局部函数
    fun square(n: Int): Int {  // ①
        return n * n
    }

    // 定义一个计算立方的局部函数
    fun cube(n: Int): Int {  // ②
        return n * n * n
    }

    // 定义一个计算阶乘的局部函数
    fun factorial(n: Int): Int {  // ③
        var result = 1
        for (index in 2..n) {
            result *= index
        }
        return result
    }
    when (type) {
    // 返回局部函数
        "square" -> return ::square
        "cube" -> return ::cube
        else -> return ::factorial
    }
}

/**
 * 使用lambda表达式简化函数类型的返回值
 *
 * 简化了test6 的写法
 */
fun test7(type: String): (Int) -> Int {
    when (type) {
        "quare" -> return { n: Int -> n * n }
        "cube" -> return { n: Int -> n * n * n }
        else -> return { n: Int ->
            var result = 1
            for (index in 2..n) {
                result *= index
            }
            result
        }
    }
}

var lambdaList = ArrayList<(Int) -> Int>()
/**
 * 定义一个函数，形参未函数类型
 *
 * 将函数类型放入集合，这样Lambda表达式可以脱离函数独立使用。
 */
fun test8(fn: (Int) -> Int) {
    lambdaList.add(fn)
}