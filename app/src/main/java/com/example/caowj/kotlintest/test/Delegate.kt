package com.example.caowj.kotlintest.test

/**
 * <pre>
 *     委托
 *     作者：Caowj
 *     邮箱：caoweijian@kedacom.com
 *     日期：2020/3/23 14:07
 * </pre>
 */
interface Base2 {
    fun print()
}

class Base2Impl(val x: Int) : Base2 {
    override fun print() {
        print(x)
    }
}

class Derived2(b: Base2) : Base2 by b

fun main() {
    val b = Base2Impl(10)
    Derived2(b).print()
}