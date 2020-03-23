package com.example.caowj.kotlintest.test


/**
 *@Dec ：继承抽象类，实现接口
 *@Author : Caowj
 *@Date : 2018/9/13 13:56
 */
class Rectangle(
        var height: Double,
        var length: Double
) : Shape(listOf(height, length, height, length)), RectangleProperties {

    override val isSquare: Boolean get() = length == height
    override fun calculateArea(): Double = height * length
    override fun draw() {
        super.draw()
    }
}
