package com.example.caowj.kotlintest.test

/**
 *@Dec ：定义抽象类和抽象方法
 *@Author : Caowj
 *@Date : 2018/9/13 13:55
 */
abstract class Shape(val sides: List<Double>) {
    val perimeter: Double get() = sides.sum()
    abstract fun calculateArea(): Double

}