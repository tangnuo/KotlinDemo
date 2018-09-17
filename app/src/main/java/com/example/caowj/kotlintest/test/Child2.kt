package com.example.caowj.kotlintest.test

/**
 *@Dec ：子类，有次级构造函数的写法：
 *@Author : Caowj
 *@Date : 2018/9/14 16:12
 */
class Child2 : ParentClass {
    constructor(name: String) : super(name) {

    }

    constructor(name: String, age: Int) : super(name) {

    }
}