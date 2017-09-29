package com.example.caowj.kotlintest

/**
 * 语法测试联系
 * package: com.example.caowj.kotlintest
 * author: Administrator
 * date: 2017/9/25 10:27
 */


//object HelloJava{
//    @JvmStatic fun main(args: Array<String>){
//        println("Hello World")
//    }
//}


//fun main(args: Array<out String>):Unit {
////    print("Hellow world1")
//
//    args.map { println(it) }
//
//    args.map(::println)
//
//    for (arg in args){
//        println(arg)
//    }
//}


//fun main(vararg args:  String) {
//    args.flatMap { it.split("_") }.map {
//        println("$it+${it.length}  ")
//    }
//}
//


enum class Lang2(val hello: String) {
    ENGLISH("Hello"), CHINA("你好"), JAPANESE("sjlsd");

    fun sayHello() {
        println(hello)
    }

    /**
     * 转换成大写
     */
    companion object {
        fun parse(name: String): Lang2 {
            return Lang2.valueOf(name.toUpperCase())
        }
    }
}

/************************************************************/

fun main(args: Array<String>) {
    if (args.size == 0) return
    val lang = Lang2.parse(args[0])
    println(lang)
    lang.sayHello()
    lang.sayBye()
}


/**
 * 扩展Lang2，新增方法
 */
fun Lang2.sayBye() {
    val bye = when (this) {

        Lang2.ENGLISH -> "Bye"
        Lang2.CHINA -> "再见"
        Lang2.JAPANESE -> "sdafds"
    }
    println(bye)
}