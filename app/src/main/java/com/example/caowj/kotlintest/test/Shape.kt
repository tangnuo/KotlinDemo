package com.example.caowj.kotlintest.test

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 *@Dec ：定义抽象类和抽象方法
 *@Author : Caowj
 *@Date : 2018/9/13 13:55
 */
abstract class Shape(val sides: List<Double>) {
    val perimeter: Double get() = sides.sum()
    abstract fun calculateArea(): Double

    //open 修饰可重写的方法
    open fun draw() {}
}


/**
 * 类的继承
 */
class MyView : View {
    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
}