package com.example.caowj.kotlintest.test

/**
 * <pre>
 *     内联类
 *     作者：Caowj
 *     邮箱：caoweijian@kedacom.com
 *     日期：2020/3/23 11:05
 * </pre>
 */

inline class Name(private val s: String) : Printable {
    override fun prettyPrint(): String {
        return "Let's $s"
    }

    val length: Int
        get() = s.length

    fun greet() {
        println("Hello, $s")
    }
}

fun main() {
    val name = Name("Kotlin")
    name.greet()
    println(name.length)
    println(name.prettyPrint())
}

interface Printable {
    fun prettyPrint(): String
}