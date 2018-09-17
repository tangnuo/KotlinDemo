package com.example.caowj.kotlintest.test

/**
 *@Dec ：子类，//无次级构造函数的写法
 *@Author : Caowj
 *@Date : 2018/9/14 16:11
 */
class Child1(name: String, age: Int) : ParentClass(name) {

    override fun publicMethod() {
        super.publicMethod()
    }
}