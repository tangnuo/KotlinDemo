package com.example.caowj.kotlintest.test

/**
 *@Dec ：继承抽象类
 *@Author : Caowj
 *@Date : 2018/9/13 14:02
 */
class Triangle(
        var sideA: Double,
        var sideB: Double,
        var sideC: Double

) : Shape(listOf(sideA, sideB, sideC)) {
    override fun calculateArea(): Double {
        val s = perimeter / 2
        return Math.sqrt(s * (s - sideA) * (s - sideB) * (s - sideC))

    }
}