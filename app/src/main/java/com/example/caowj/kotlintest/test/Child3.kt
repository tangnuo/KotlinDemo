package com.example.caowj.kotlintest.test

/**
 *@Dec ：当这个父类中有2个方法同名，参数相同时，kotlin中使用尖括号来进行标记父类，以示区分。
 *@Author : Caowj
 *@Date : 2018/9/14 16:17
 */
class Child3(name: String, age: Int) : ParentClass(name), ParentInterface {
    override fun publicMethod() {
        super<ParentClass>.publicMethod()
        super<ParentInterface>.publicMethod()
    }
}